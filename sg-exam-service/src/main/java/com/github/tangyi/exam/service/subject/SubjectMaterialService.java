/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.exam.service.subject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.RandomSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SnowFlakeId;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectMaterialMapper;
import com.github.tangyi.exam.service.MaterialSubjectService;
import com.github.tangyi.exam.service.subject.converter.SubjectMaterialConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 填空题
 */
@Slf4j
@AllArgsConstructor
@Service
public class SubjectMaterialService extends CrudService<SubjectMaterialMapper, SubjectMaterial>
		implements ISubjectService {

	private final SubjectMaterialConverter converter;

	private final SubjectsService subjectsService;
	private final MaterialSubjectService materialSubjectService;

	private final SubjectCategoryService subjectCategoryService;

	public PageInfo<SubjectDto> findSubjectPageById(SubjectDto subjectDto, Map<String, Object> params, int pageNum,
			int pageSize) {
		// 查询材料题目关联表
		MaterialSubject ms = new MaterialSubject();
		ms.setTenantCode(SysUtil.getTenantCode());
		ms.setMaterialId(subjectDto.getMaterialId());
		PageInfo<MaterialSubject> materialSubjects = materialSubjectService.findPage(params, pageNum, pageSize);
		List<SubjectDto> subjectDtoList = Lists.newArrayList();
		// 根据题目 ID 查询题目信息
		if (CollectionUtils.isNotEmpty(materialSubjects.getList())) {
			Long[] subjectIds = materialSubjects.getList().stream().map(MaterialSubject::getSubjectId)
					.toArray(Long[]::new);
			List<Subjects> subjects = subjectsService.findBySubjectIds(subjectIds);
			subjectDtoList = subjectsService.findSubjectDtoList(subjects);
			// 是否需要对题目信息进行查询
			if (params.get("subjectName") != null) {
				String subjectName = params.get("subjectName").toString();
				subjectDtoList = subjectDtoList.stream()
						.filter(subject -> subject.getSubjectName().contains(subjectName)).collect(Collectors.toList());
			}
		}
		// 按序号排序
		if (CollectionUtils.isNotEmpty(subjectDtoList)) {
			subjectDtoList = subjectDtoList.stream().sorted(Comparator.comparing(SubjectDto::getSort))
					.collect(Collectors.toList());
		}
		return PageUtil.newPageInfo(materialSubjects, subjectDtoList);
	}

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#id")
	public SubjectMaterial get(Long id) {
		return super.get(id);
	}

	@Override
	public SubjectDto getSubject(Long id) {
		return converter.convert(this.get(id));
	}

	@Override
	public List<SubjectDto> getSubjects(List<Long> ids) {
		List<SubjectDto> list = Lists.newArrayListWithExpectedSize(ids.size());
		for (Long id : ids) {
			SubjectDto dto = getSubject(id);
			if (dto != null) {
				// 找到材料题附属题目
				List<MaterialSubject> listByMaterial = materialSubjectService.findListByMaterialId(id);
				Long[] subjectIds = listByMaterial.stream().map(MaterialSubject::getSubjectId).toArray(Long[]::new);
				if (subjectIds.length > 0) {
					List<Subjects> childSubjects = subjectsService.findBySubjectIds(subjectIds);
					List<SubjectDto> subjectDtoList = subjectsService.findSubjectDtoList(childSubjects, true, true);
					dto.setChildSubjects(subjectDtoList);
				}
				list.add(dto);
			}
		}
		return list;
	}


	@Override
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType) {
		return null;
	}

	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		return null;
	}

	@Override
	public PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto) {
		return null;
	}

	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return converter.convert(this.findListById(ids), true);
	}

	@Override
	@Transactional
	public BaseEntity<?> insertSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		s.setAnswer("");
		this.insert(s);
		return s;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		s.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectId")
	public int updateSubjectSort(Long subjectId, Integer sort) {
		SubjectMaterial s = new SubjectMaterial();
		s.setId(subjectId);
		s.setSort(sort);
		return this.update(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		return this.delete(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectMaterial s = new SubjectMaterial();
		BeanUtils.copyProperties(subjectDto, s);
		return this.physicalDelete(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#s.id")
	public int physicalDelete(SubjectMaterial s) {
		return this.dao.physicalDelete(s);
	}

	/**
	 * 查询下一题的序号
	 */

	public Integer nextSubjectNo(Long examinationId) {
		return materialSubjectService.nextSubjectNo(examinationId);
	}

	/**
	 * 根据材料 ID 批量添加题目
	 */
	@Transactional
	public Boolean batchAddSubjects(Long id, HashMap<String, Object> map) {
		List list = (List) map.get("examinationId");
		Long examinationId = null;
		if (list != null)
			examinationId = Long.valueOf(list.get(0).toString());
		List<HashMap> maps = (List<HashMap>) map.get("subjects");
		ObjectMapper objectMapper = new ObjectMapper();
		Integer nextNo = nextSubjectNo(id);
		for (int i = 0; i < maps.size(); i++) {
			HashMap sub = maps.get(i);
			SubjectDto subject = objectMapper.convertValue(sub, SubjectDto.class);
			subject.setId(null);
			subject.setCategoryId(null);
			subject.setCategoryName(null);
			subject.setNewRecord(true);
			subject.setCommonValue();
			// 自定义 ID
			subject.setId(SnowFlakeId.newId());
			subject.setSort(nextNo++);
			if (CollectionUtils.isNotEmpty(subject.getOptions())) {
				for (SubjectOption option : subject.getOptions()) {
					option.setId(null);
					option.setSubjectChoicesId(null);
				}
			}
			// 关联考试 ID
			subject.setMaterialId(id);
			if (examinationId != null && !"".equals(examinationId))
				subject.setExaminationId(examinationId);
			subjectsService.insert(subject);
		}
		return Boolean.TRUE;
	}

	/**
	 * 根据考试 ID 批量添加题目
	 */
	@Transactional
	public Boolean batchAddSubjects(Long materialId, Long examinationId, List<SubjectDto> subjects) {
		Integer nextNo = nextSubjectNo(materialId);
		for (SubjectDto subject : subjects) {
			subject.setId(null);
			subject.setCategoryId(null);
			subject.setCategoryName(null);
			subject.setNewRecord(true);
			subject.setCommonValue();
			// 自定义 ID
			subject.setId(SnowFlakeId.newId());
			subject.setSort(nextNo++);
			if (CollectionUtils.isNotEmpty(subject.getOptions())) {
				for (SubjectOption option : subject.getOptions()) {
					option.setId(null);
					option.setSubjectChoicesId(null);
				}
			}
			// 关联考试 ID
			subject.setExaminationId(examinationId);
			subject.setMaterialId(materialId);
			subjectsService.insert(subject);
		}
		return Boolean.TRUE;
	}

	/**
	 * 随机选取题目
	 */
	private List<SubjectDto> randomAddSubject(List<Subjects> subjects, int cnt) {
		StopWatch start = StopWatchUtil.start();
		// 已经选取的题目 ID，用于去重
		Set<Long> idSet = Sets.newHashSetWithExpectedSize(cnt);
		// 已经选取的题目
		List<Subjects> tmpSubjects = Lists.newArrayListWithExpectedSize(cnt);
		int itCnt = 0;
		while (tmpSubjects.size() < cnt) {
			itCnt++;
			if (itCnt > 500) {
				throw new CommonException("Failed to compose examination，itCnt：" + itCnt);
			}

			int index = ThreadLocalRandom.current().nextInt(0, subjects.size());
			Subjects tmp = subjects.get(index);
			if (!idSet.contains(tmp.getId())) {
				idSet.add(tmp.getId());
				tmpSubjects.add(tmp);
				log.info("select subject: {}, size: {}, target size: {}", tmp.getId(), tmpSubjects.size(), cnt);
			}
		}

		List<SubjectDto> result = Lists.newArrayListWithExpectedSize(cnt);
		for (Subjects sub : tmpSubjects) {
			SubjectDto temp = subjectsService.getSubject(sub.getSubjectId(), sub.getType());
			result.add(temp);
		}
		log.info("randomAddSubject success, itCnt: {}, took: {}", itCnt, StopWatchUtil.stop(start));
		return result;
	}

	@Transactional
	public Boolean randomAddSubjects(Long id, RandomSubjectDto params) {
		// 校验分类是否存在
		SubjectCategory category = this.subjectCategoryService.get(params.getCategoryId());
		SgPreconditions.checkNull(category, "The subject category does not exists.");
		// 根据分类查询题目
		List<Subjects> subjects = this.subjectsService.findIdAndTypeByCategoryId(category.getId());
		SgPreconditions.checkCollectionEmpty(subjects, "The category's subject is empty");
		// 数量校验
		Integer cnt = params.getTarget();
		SgPreconditions.checkBoolean(cnt > subjects.size(), "The category's subject is not enough.");
		List<SubjectDto> result = this.randomAddSubject(subjects, cnt);
		Long examinationId = params.getExaminationId();
		if (CollectionUtils.isNotEmpty(result)) {
			this.batchAddSubjects(id, examinationId, result);
		}
		return Boolean.TRUE;
	}
}
