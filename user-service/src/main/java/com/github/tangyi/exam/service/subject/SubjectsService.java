package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.mapper.SubjectsMapper;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.subject.converter.*;
import com.github.tangyi.exam.utils.SubjectUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author tangyi
 * @date 2022/4/13 2:03 下午
 */
@AllArgsConstructor
@Slf4j
@Service
public class SubjectsService extends CrudService<SubjectsMapper, Subjects> {

	private final SubjectServiceFactory subjectServiceFactory;

	private final ExaminationSubjectService examinationSubjectService;

	private final SubjectCategoryService subjectCategoryService;

	private final SubjectChoicesConverter subjectChoicesConverter;

	private final SubjectShortAnswerConverter subjectShortAnswerConverter;

	private final SubjectJudgementConverter subjectJudgementConverter;

	private final SubjectSpeechConverter subjectSpeechConverter;

	private final SubjectVideoConverter subjectVideoConverter;

	public SubjectDto getSubject(Long id) {
		Subjects subjects = this.findBySubjectId(id);
		if (subjects != null) {
			return subjectServiceFactory.service(subjects.getType()).getSubject(subjects.getSubjectId());
		}
		return null;
	}

	@Cacheable(cacheNames = ExamCacheName.SUBJECTS, key = "#subjectId")
	public Subjects findBySubjectId(Long subjectId) {
		return super.dao.findBySubjectId(subjectId);
	}

	public PageInfo<SubjectDto> findPage(Map<String, Object> params, int pageNum, int pageSize, SubjectDto subjectDto) {
		if (subjectDto.getCategoryId() != null) {
			params.put("categoryId", subjectDto.getCategoryId());
		}
		PageInfo<Subjects> pageInfo = findPage(params, pageNum, pageSize);
		List<SubjectDto> dtoList = Lists.newArrayListWithExpectedSize(pageSize);
		List<Long> categoryIds = Lists.newArrayList();
		List<Subjects> subjects = pageInfo.getList();
		if (CollectionUtils.isNotEmpty(subjects)) {
			for (Subjects es : subjects) {
				SubjectDto temp = subjectServiceFactory.service(es.getType()).getSubject(es.getSubjectId());
				dtoList.add(temp);
				if (temp.getCategoryId() != null) {
					categoryIds.add(temp.getCategoryId());
				}
			}
		}
		// 按序号排序
		if (CollectionUtils.isNotEmpty(dtoList)) {
			dtoList = dtoList.stream().sorted(Comparator.comparing(SubjectDto::getSort)).collect(Collectors.toList());
		}
		initCategoryInfo(categoryIds, dtoList);
		return PageUtil.newPageInfo(pageInfo, dtoList);
	}

	public void initCategoryInfo(List<Long> categoryIds, List<SubjectDto> subjects) {
		if (CollectionUtils.isEmpty(categoryIds)) {
			return;
		}
		List<SubjectCategory> categories = subjectCategoryService.findListById(categoryIds.toArray(new Long[0]));
		if (CollectionUtils.isEmpty(categories)) {
			return;
		}
		for (SubjectDto subject : subjects) {
			for (SubjectCategory category : categories) {
				if (Objects.equals(subject.getCategoryId(), category.getId())) {
					subject.setCategoryName(category.getCategoryName());
				}
			}
		}
	}

	public Integer findSubjectCountByCategoryId(Long categoryId) {
		return this.dao.findSubjectCountByCategoryId(categoryId);
	}

	/**
	 * 根据分类ID查询全部题目
	 * @param categoryId categoryId
	 * @return List
	 */
	public List<SubjectDto> findListByCategoryId(Long categoryId) {
		List<Subjects> subjects = this.dao.findByCategoryId(categoryId);
		if (CollectionUtils.isEmpty(subjects)) {
			return Collections.emptyList();
		}
		List<SubjectDto> dtoList = Lists.newArrayListWithExpectedSize(subjects.size());
		for (Subjects es : subjects) {
			SubjectDto temp = subjectServiceFactory.service(es.getType()).getSubject(es.getSubjectId());
			dtoList.add(temp);
		}
		return dtoList;
	}

	/**
	 * 保存
	 *
	 * @param dto dto
	 * @return int
	 * @author tangyi
	 * @date 2019/06/16 17:59
	 */
	@Transactional
	public SubjectDto insert(SubjectDto dto) {
		BaseEntity<?> entity = subjectServiceFactory.service(dto.getType()).insertSubject(dto);
		Long subjectId = entity.getId();
		dto.setId(subjectId);
		Subjects subjects = new Subjects();
		subjects.setNewRecord(true);
		subjects.setCommonValue(dto.getCreator(), dto.getTenantCode());
		subjects.setSubjectId(subjectId);
		subjects.setCategoryId(dto.getCategoryId());
		subjects.setType(dto.getType());
		subjects.setSort(dto.getSort());
		insert(subjects);
		if (dto.getExaminationId() != null) {
			insertEs(dto, subjectId, dto.getCreator(), dto.getTenantCode());
		}
		return dto;
	}

	@Transactional
	public void insertEs(SubjectDto dto, Long subjectId, String creator, String tenantCode) {
		ExaminationSubject es = new ExaminationSubject();
		es.setNewRecord(true);
		es.setCommonValue(creator, tenantCode);
		es.setSubjectId(subjectId);
		es.setExaminationId(dto.getExaminationId());
		es.setSort(dto.getSort());
		try {
			examinationSubjectService.insert(es);
		} catch (DuplicateKeyException e) {
			throw new CommonException("序号重复，请修改题目序号");
		}
	}

	/**
	 * 更新
	 *
	 * @param subjectDto subjectDto
	 * @return int
	 * @author tangyi
	 * @date 2019/06/16 18:01
	 */
	@Transactional
	@CacheEvict(cacheNames = ExamCacheName.SUBJECTS, key = "#subjectDto.id")
	public SubjectDto update(SubjectDto subjectDto) {
		// 更新题目信息
		if ((subjectServiceFactory.service(getSubjectType(subjectDto.getId())).updateSubject(subjectDto)) == 0) {
			return this.insert(subjectDto);
		}
		// 更新题目序号
		examinationSubjectService.updateSort(subjectDto.getExaminationId(), subjectDto.getId(), subjectDto.getSort());
		return subjectDto;
	}

	/**
	 * 物理删除
	 *
	 * @param id id
	 * @return int
	 * @author tangyi
	 * @date 2019/06/16 22:51
	 */
	@Transactional
	@CacheEvict(cacheNames = ExamCacheName.SUBJECTS, key = "#id")
	public int physicalDelete(Long id) {
		SubjectDto subjectDto = new SubjectDto();
		subjectDto.setId(id);
		Subjects subjects = this.findBySubjectId(id);
		if (subjects != null
				&& subjectServiceFactory.service(getSubjectType(subjectDto.getId())).physicalDeleteSubject(subjectDto)
				> 0) {
			this.dao.deleteByPrimaryKey(subjects.getId());
			ExaminationSubject examinationSubject = new ExaminationSubject();
			examinationSubject.setSubjectId(subjectDto.getId());
			return examinationSubjectService.deleteBySubjectId(examinationSubject);
		}
		return -1;
	}

	/**
	 * 查询第一题
	 *
	 * @param examinationId examinationId
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/10/13 18:36:58
	 */
	public SubjectDto findFirstSubjectByExaminationId(Long examinationId) {
		// 第一题
		ExaminationSubject es = new ExaminationSubject();
		es.setExaminationId(examinationId);
		// 根据考试ID查询考试题目管理关系，题目ID递增
		List<ExaminationSubject> subjects = examinationSubjectService.findList(es);
		SgPreconditions.checkCollectionEmpty(subjects, "该考试未录入题目");
		subjects = subjects.stream().sorted(Comparator.comparing(ExaminationSubject::getSort))
				.collect(Collectors.toList());
		// 第一题的ID
		es = subjects.get(0);
		// 根据题目ID，类型获取题目的详细信息
		SubjectDto dto = this.getSubject(es.getSubjectId());
		// 题目数量
		Integer subjectCount = examinationSubjectService.findSubjectCount(examinationId);
		dto.setTotal(subjectCount);
		dto.setHasMore(subjectCount != null && es.getSort() < subjectCount);
		return dto;
	}

	/**
	 * 根据上一题ID查找
	 *
	 * @param examinationId     examinationId
	 * @param previousSubjectId previousSubjectId
	 * @param nextType          0：下一题，1：上一题
	 * @return SubjectDto
	 * @author tangyi
	 * @date 2019/06/18 13:49
	 */
	@Transactional
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousSubjectId, Integer nextType) {
		return subjectServiceFactory.service(getSubjectType(previousSubjectId))
				.getNextByCurrentIdAndType(examinationId, previousSubjectId, nextType);
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 *
	 * @param subjects subjects
	 * @return List
	 * @author tangyi
	 * @date 2019/06/17 10:54
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> subjects) {
		return findSubjectDtoList(subjects, false);
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 *
	 * @param subjects subjects
	 * @param findOptions         findOptions
	 * @return List
	 * @author tangyi
	 * @date 2019/06/17 11:54
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> subjects, boolean findOptions) {
		return findSubjectDtoList(subjects, findOptions, true);
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 *
	 * @param subjects subjects
	 * @param findOptions         findOptions
	 * @param findAnswer          findAnswer
	 * @return List
	 * @author tangyi
	 * @date 2019/06/17 11:54
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> subjects, boolean findOptions, boolean findAnswer) {
		Map<String, Long[]> idMap = SubjectUtil.groupByType(subjects);
		List<SubjectDto> dtoList = Lists.newArrayList();
		if (idMap.containsKey(SubjectTypeEnum.CHOICES.name())) {
			List<SubjectChoices> choices = subjectServiceFactory.getSubjectChoicesService()
					.findListById(idMap.get(SubjectTypeEnum.CHOICES.name()));
			if (CollectionUtils.isNotEmpty(choices)) {
				if (findOptions) {
					choices = choices.stream().map(SubjectChoices::getId)
							.map(subjectServiceFactory.getSubjectChoicesService()::get).collect(Collectors.toList());
				}
				dtoList.addAll(subjectChoicesConverter.toDto(choices, findAnswer));
			}
		}
		if (idMap.containsKey(SubjectTypeEnum.MULTIPLE_CHOICES.name())) {
			List<SubjectChoices> choices = subjectServiceFactory.getSubjectChoicesService()
					.findListById(idMap.get(SubjectTypeEnum.MULTIPLE_CHOICES.name()));
			if (CollectionUtils.isNotEmpty(choices)) {
				// 查找选项信息
				if (findOptions) {
					choices = choices.stream().map(SubjectChoices::getId)
							.map(subjectServiceFactory.getSubjectChoicesService()::get).collect(Collectors.toList());
				}
				dtoList.addAll(subjectChoicesConverter.toDto(choices, findAnswer));
			}
		}
		if (idMap.containsKey(SubjectTypeEnum.SHORT_ANSWER.name())) {
			List<SubjectShortAnswer> shortAnswers = subjectServiceFactory.getSubjectShortAnswerService()
					.findListById(idMap.get(SubjectTypeEnum.SHORT_ANSWER.name()));
			if (CollectionUtils.isNotEmpty(shortAnswers)) {
				dtoList.addAll(subjectShortAnswerConverter.toDto(shortAnswers, findAnswer));
			}
		}
		if (idMap.containsKey((SubjectTypeEnum.JUDGEMENT.name()))) {
			List<SubjectJudgement> judgements = subjectServiceFactory.getSubjectJudgementService()
					.findListById(idMap.get(SubjectTypeEnum.JUDGEMENT.name()));
			if (CollectionUtils.isNotEmpty(judgements)) {
				dtoList.addAll(subjectJudgementConverter.toDto(judgements, findAnswer));
			}
		}
		if (idMap.containsKey((SubjectTypeEnum.SPEECH.name()))) {
			List<SubjectSpeech> speeches = subjectServiceFactory.getSubjectSpeechService()
					.findListById(idMap.get(SubjectTypeEnum.SPEECH.name()));
			if (CollectionUtils.isNotEmpty(speeches)) {
				dtoList.addAll(subjectSpeechConverter.toDto(speeches, findAnswer));
			}
		}
		if (idMap.containsKey((SubjectTypeEnum.VIDEO.name()))) {
			List<SubjectVideo> subjectVideos = subjectServiceFactory.getSubjectVideoService()
					.findListById(idMap.get(SubjectTypeEnum.VIDEO.name()));
			if (CollectionUtils.isNotEmpty(subjectVideos)) {
				dtoList.addAll(subjectVideoConverter.toDto(subjectVideos, findAnswer));
			}
		}
		return dtoList;
	}

	/**
	 * 物理批量删除
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019/06/16 22:52
	 */
	@Transactional
	public int physicalDeleteAll(Long[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				this.physicalDelete(id);
			}
		}
		return 1;
	}

	public int getSubjectType(Long subjectId) {
		Subjects subjects = this.findBySubjectId(subjectId);
		SgPreconditions.checkNull(subjects, "subjects is null");
		return subjects.getType();
	}

	public List<Subjects> findBySubjectIds(Long[] subjectIds) {
		return super.dao.findBySubjectIds(subjectIds);
	}

	public List<Subjects> findByCategoryId(Long categoryId) {
		return super.dao.findByCategoryId(categoryId);
	}

	public List<Subjects> findByCategoryIds(Long[] categoryIds) {
		return super.dao.findByCategoryIds(categoryIds);
	}

	public List<Subjects> findByType(Integer type) {
		return super.dao.findByType(type);
	}

	public Integer nextSubjectNo(Long categoryId) {
		Integer no = this.dao.findMaxSortByCategoryId(categoryId);
		return no == null ? 1 : no + 1;
	}
}
