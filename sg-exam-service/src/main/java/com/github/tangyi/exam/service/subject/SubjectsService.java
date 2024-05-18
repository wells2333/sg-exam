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

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.NextSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectCategoryDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.api.exam.service.ISubjectsService;
import com.github.tangyi.api.exam.thread.IExecutorHolder;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.base.TreeEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.ExecutorUtils;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.mapper.SubjectsMapper;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.MaterialSubjectService;
import com.github.tangyi.exam.service.data.SubjectViewCounterService;
import com.github.tangyi.exam.service.fav.SubjectFavService;
import com.github.tangyi.exam.service.subject.converter.*;
import com.github.tangyi.exam.utils.ExamUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SubjectsService extends CrudService<SubjectsMapper, Subjects> implements ISubjectsService {
	private final MaterialSubjectService msService;
	private final ExaminationSubjectService esService;
	private final SubjectCategoryService subjectCategoryService;
	private final SubjectChoicesConverter subjectChoicesConverter;
	private final SubjectShortAnswerConverter subjectShortAnswerConverter;
	private final SubjectJudgementConverter judgementConverter;
	private final SubjectFillBlankConverter fillBlankConverter;
	private final SubjectMaterialConverter subjectMaterialConverter;
	private final SubjectFavService subjectFavService;
	private final SubjectViewCounterService subjectViewCounterService;
	private final AttachmentManager attachmentManager;
	private final IExecutorHolder executorHolder;

	private ISubjectService getService(Long subjectId) {
		return SubjectServiceFactory.getService(getSubjectType(subjectId));
	}

	/**
	 * 根据单个题目 ID 查询题目信息，会有缓存
	 */
	public SubjectDto getSubject(Long id) {
		return getSubject(id, false);
	}

	public SubjectDto getSubject(Long id, boolean isView) {
		SubjectDto dto = null;
		Subjects sub = this.findBySubjectId(id);
		if (sub != null) {
			dto = getSubject(sub.getSubjectId(), sub.getType());
			if (dto != null && isView) {
				dto.setViews(subjectViewCounterService.viewSubject(id) + "");
			}
		}
		return dto;
	}

	/**
	 * 批量查询题目信息，无缓存
	 */
	public Collection<SubjectDto> getSubjects(List<Long> ids) {
		List<Subjects> sub = this.findBySubjectIds(ids.toArray(new Long[0]));
		return SubjectServiceFactory.batchGetSubjects(sub);
	}

	public Collection<SubjectDto> getSubjectsBySubjects(List<Subjects> sub) {
		return SubjectServiceFactory.batchGetSubjects(sub);
	}

	public SubjectDto getSubject(Long subjectId, Integer type) {
		SubjectDto dto = SubjectServiceFactory.getService(type).getSubject(subjectId);
		if (StringUtils.isEmpty(dto.getSubjectVideoUrl()) && dto.getSubjectVideoId() != null) {
			dto.setSubjectVideoUrl(attachmentManager.getPreviewUrlIgnoreException(dto.getSubjectVideoId()));
		}
		if (StringUtils.isEmpty(dto.getSpeechUrl()) && dto.getSpeechId() != null) {
			dto.setSpeechUrl(attachmentManager.getPreviewUrlIgnoreException(dto.getSpeechId()));
		}
		return dto;
	}

	@Cacheable(cacheNames = ExamCacheName.SUBJECTS, key = "#subjectId")
	public Subjects findBySubjectId(Long subjectId) {
		return super.dao.findBySubjectId(subjectId);
	}

	public List<Subjects> findByCategoryIdAndMaxSort(Long categoryId, Integer sort) {
		return this.dao.findByCategoryIdAndMaxSort(categoryId, sort);
	}

	public PageInfo<SubjectDto> findPage(Map<String, Object> params, int pageNum, int pageSize, boolean findFav,
			boolean findView, SubjectDto subjectDto) {
		if (subjectDto.getCategoryId() != null) {
			params.put("categoryId", subjectDto.getCategoryId());
		}
		PageInfo<Subjects> pageInfo = findPage(params, pageNum, pageSize);
		List<SubjectDto> dtoList = Lists.newArrayListWithExpectedSize(pageSize);
		List<Long> categoryIds = Lists.newArrayList();
		Collection<SubjectDto> list = this.getSubjectsBySubjects(pageInfo.getList());
		for (SubjectDto dto : list) {
			//这个是根据 categoryIdj 进行查找的
			if (params.get("subjectName") == null) {
				dtoList.add(dto);
			} else {
				//根据 subjectName 进行查找
				String subjectName = (String) params.get("subjectName");
				boolean contains = dto.getSubjectName().contains(subjectName);
				if (contains)
					dtoList.add(dto);
			}
			if (dto.getCategoryId() != null) {
				categoryIds.add(dto.getCategoryId());
			}

		}
		if (CollectionUtils.isNotEmpty(dtoList)) {
			dtoList = dtoList.stream().sorted(Comparator.comparing(SubjectDto::getSort)).collect(Collectors.toList());
		}
		initCategoryInfo(categoryIds, dtoList);
		if (findFav) {
			subjectFavService.fillUserFavorites(dtoList);
		}
		if (findView) {
			subjectViewCounterService.getSubjectsView(dtoList);
		}
		return PageUtil.newPageInfo(pageInfo, dtoList);
	}

	public PageInfo<?> findUserFavoritesPage(PageInfo<ExamUserFav> page) {
		PageInfo<SubjectDto> pageInfo = new PageInfo<>();
		BeanUtils.copyProperties(page, pageInfo);
		List<Long> ids = page.getList().stream().map(ExamUserFav::getTargetId).collect(Collectors.toList());
		Collection<SubjectDto> list = getSubjects(ids);
		if (CollectionUtils.isNotEmpty(list)) {
			for (SubjectDto sub : list) {
				sub.setFavorite(true);
			}
			pageInfo.setList(Lists.newArrayList(list));
		}
		return pageInfo;
	}

	public Long findAllSubjectCount() {
		return this.dao.findAllSubjectCount();
	}

	public Integer findSubjectCountByCategoryId(Long categoryId) {
		return this.dao.findSubjectCountByCategoryId(categoryId);
	}

	public void findAndFillSubjectCnt(List<SubjectCategoryDto> categories) {
		Map<Long, Integer> cntMap = this.findSubjectCountByCategories(categories);
		this.fillSubjectCnt(categories, cntMap);
	}

	/**
	 * 根据分类 ID 查询全部题目
	 */
	public List<Subjects> findListByCategoryId(Long categoryId) {
		return this.dao.findByCategoryId(categoryId);
	}

	/**
	 * 根据分类 ID 查询全部题目
	 */
	public List<Subjects> findIdAndTypeByCategoryId(Long categoryId) {
		return this.dao.findIdAndTypeByCategoryId(categoryId);
	}

	@Transactional
	public SubjectDto insert(SubjectDto dto) {
		BaseEntity<?> entity = SubjectServiceFactory.getService(dto.getType()).insertSubject(dto);
		Long subjectId = entity.getId();
		dto.setId(subjectId);
		Subjects sub = new Subjects();
		sub.setNewRecord(true);
		sub.setCommonValue(dto.getCreator(), dto.getTenantCode());
		sub.setSubjectId(subjectId);
		sub.setCategoryId(dto.getCategoryId());
		sub.setType(dto.getType());
		sub.setSort(dto.getSort());
		sub.setParentId(dto.getParentId());
		insert(sub);
		if (dto.getExaminationId() != null && dto.getMaterialId() == null) {
			insertEs(dto, subjectId, dto.getCreator(), dto.getTenantCode());
		}
		if (dto.getMaterialId() != null){
			insertMs(dto, subjectId, dto.getCreator(), dto.getTenantCode());
			// 查询
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

		// 序号重复时，尝试递增插入
		for (int i = 1; i < 10; i++) {
			try {
				if (esService.insert(es) > 0) {
					return;
				}
			} catch (DuplicateKeyException e) {
				es.setSort(dto.getSort() + i);
				log.warn("Duplicate subject sort, retry {}", es.getSort());
			}
		}

		throw new IllegalArgumentException("The subject repeated, please modify.");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertMs(SubjectDto dto, Long subjectId, String creator, String tenantCode) {
		MaterialSubject ms = new MaterialSubject();
		ms.setNewRecord(true);
		ms.setCommonValue(creator, tenantCode);
		ms.setSubjectId(subjectId);
		ms.setMaterialId(dto.getMaterialId());
		ms.setSort(dto.getSort());
		ms.setExaminationId(dto.getExaminationId());
		// 序号重复时，尝试递增插入
		for (int i = 1; i < 10; i++) {
			try {
				if (msService.insert(ms) > 0) {
					return;
				}
			} catch (DuplicateKeyException e) {
				ms.setSort(dto.getSort() + i);
				log.warn("Duplicate subject sort, retry {}", ms.getSort());
			}
		}

		throw new IllegalArgumentException("The subject repeated, please modify.");
	}

	@Transactional
	@CacheEvict(cacheNames = ExamCacheName.SUBJECTS, key = "#subjectDto.id")
	public SubjectDto update(SubjectDto subjectDto) {
		if ((SubjectServiceFactory.getService(getSubjectType(subjectDto.getId())).updateSubject(subjectDto)) == 0) {
			return this.insert(subjectDto);
		}

		this.esService.updateSort(subjectDto.getExaminationId(), subjectDto.getId(), subjectDto.getSort());
		return subjectDto;
	}

	@Override
	@Transactional
	public void resetSortByExaminationId(Long eId, Integer maxSort) {
		List<ExaminationSubject> sub = this.esService.findListByExaminationIdAndMaxSort(eId, maxSort);
		if (CollectionUtils.isEmpty(sub)) {
			return;
		}

		long startNs = System.nanoTime();
		for (ExaminationSubject es : sub) {
			es.setSort(es.getSort() - 1);
			this.esService.update(es);
			this.getService(es.getSubjectId()).updateSubjectSort(es.getSubjectId(), es.getSort());
		}
		long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
		log.info("Reset subject sort finished, eId: {}, maxSort: {}, update size: {}, took: {}ms", eId, maxSort,
				CollectionUtils.size(sub), took);
	}

	@Override
	@Transactional
	public void resetSortByCategoryId(Long categoryId, Integer maxSort) {
		List<Subjects> sub = this.findByCategoryIdAndMaxSort(categoryId, maxSort);
		if (CollectionUtils.isEmpty(sub)) {
			return;
		}

		long startNs = System.nanoTime();
		for (Subjects s : sub) {
			s.setSort(s.getSort() - 1);
			this.update(s);
			this.getService(s.getSubjectId()).updateSubjectSort(s.getSubjectId(), s.getSort());
		}
		long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
		log.info("Reset subject sort finished, categoryId: {}, maxSort: {}, update size: {}, took: {}ms", categoryId,
				maxSort, CollectionUtils.size(sub), took);
	}

	@Transactional
	@CacheEvict(cacheNames = ExamCacheName.SUBJECTS, key = "#id")
	public Subjects physicalDelete(Long id) {
		SubjectDto dto = new SubjectDto();
		dto.setId(id);
		Subjects sub = this.findBySubjectId(id);
		ISubjectService service = this.getService(dto.getId());
		if (sub != null && service.physicalDeleteSubject(dto) > 0) {
			this.dao.deleteByPrimaryKey(sub.getId());
			ExaminationSubject examinationSubject = new ExaminationSubject();
			examinationSubject.setSubjectId(dto.getId());
			esService.deleteBySubjectId(examinationSubject);
			MaterialSubject materialSubject = new MaterialSubject();
			materialSubject.setMaterialId(dto.getId());
			msService.deleteByMaterialId(materialSubject);
		}
		return sub;
	}

	@Transactional
	public boolean physicalDeleteAndResetSort(Long id, Long eId, Long categoryId) {
		Subjects sub = this.physicalDelete(id);
		if (sub != null) {
			if (eId != null) {
				this.resetSortByExaminationId(eId, sub.getSort());
			}
			if (categoryId != null) {
				this.resetSortByCategoryId(categoryId, sub.getSort());
			}
		}
		return true;
	}

	/**
	 * 查询第一题
	 */
	public SubjectDto findFirstSubjectByExaminationId(Long eId) {
		ExaminationSubject es = esService.findMinSortByExaminationId(eId);
		SgPreconditions.checkNull(es, "Empty subjects.");
		SubjectDto dto = this.getSubject(es.getSubjectId());
		// 题目数量
		Integer subjectCount = esService.findSubjectCount(eId);
		dto.setTotal(subjectCount);
		dto.setHasMore(subjectCount != null && dto.getSort() < subjectCount);
		return dto;
	}

	public SubjectDto getNextByCurrentIdAndType(Long eId, Long previousSubjectId, Integer nextType) {
		return SubjectServiceFactory.getService(getSubjectType(previousSubjectId))
				.getNextByCurrentIdAndType(eId, previousSubjectId, nextType);
	}

	public SubjectDto nextSubjectByCategoryId(NextSubjectDto next) {
		Subjects sub = this.findBySubjectId(next.getSubjectId());
		if (sub == null) {
			return null;
		}

		if (next.isRandom()) {
			return nextRandomSubjectByCategoryId(next, sub.getSort());
		}

		return nextSortedSubjectByCategoryId(next, sub);
	}

	public SubjectDto nextSortedSubjectByCategoryId(NextSubjectDto next, Subjects sub) {
		Integer sort = Math.max(0,
				AnswerConstant.NEXT.equals(next.getNextType()) ? sub.getSort() + 1 : sub.getSort() - 1);
		Subjects nextSubjects = this.dao.findByCategoryIdAndSort(next.getCategoryId(), sort);
		if (nextSubjects != null) {
			return this.getSubject(nextSubjects.getSubjectId(), nextSubjects.getType());
		}

		return null;
	}

	public SubjectDto nextRandomSubjectByCategoryId(NextSubjectDto next, long currentSort) {
		Long categoryId = next.getCategoryId();
		int max = this.dao.findMaxSortByCategoryId(categoryId);
		if (max != currentSort) {
			long sort;
			int retry = 3;
			Set<Long> realTimeIds = next.getRealTimeIds() == null ? Collections.emptySet() : next.getRealTimeIds();
			do {
				sort = ThreadLocalRandom.current().nextLong(max);
				retry--;
			} while (sort == currentSort && realTimeIds.contains(sort) && retry > 0);
			if (sort != currentSort) {
				Subjects sub = this.dao.findByCategoryIdAndSort(categoryId, (int) sort);
				if (sub != null) {
					return this.getSubject(sub.getSubjectId(), sub.getType());
				}
			}
		}
		return null;
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> sub) {
		return findSubjectDtoList(sub, false);
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> sub, boolean findOptions) {
		return findSubjectDtoList(sub, findOptions, true);
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> sub, boolean findOptions, boolean findAnswer) {
		Map<Integer, List<Long>> idMap = ExamUtil.groupByType(sub);
		List<SubjectDto> list = Lists.newArrayList();
		for (Map.Entry<Integer, List<Long>> entry : idMap.entrySet()) {
			Long[] ids = entry.getValue().toArray(new Long[0]);
			List<SubjectDto> c = null;
			if (SubjectType.CHOICES.getValue() == entry.getKey()
					|| SubjectType.MULTIPLE_CHOICES.getValue() == entry.getKey()) {
				List<SubjectChoices> subjects = SubjectServiceFactory.getChoicesService().findListById(ids);
				if (CollectionUtils.isNotEmpty(subjects)) {
					if (findOptions) {
						subjects = subjects.stream().map(SubjectChoices::getId)
								.map(SubjectServiceFactory.getChoicesService()::get).collect(Collectors.toList());
					}
					c = subjectChoicesConverter.convert(subjects, findAnswer);
				}
			} else if (SubjectType.SHORT_ANSWER.getValue() == entry.getKey()) {
				List<SubjectShortAnswer> subjects = SubjectServiceFactory.getShortAnswerService().findListById(ids);
				if (CollectionUtils.isNotEmpty(subjects)) {
					c = subjectShortAnswerConverter.convert(subjects, findAnswer);
				}
			} else if (SubjectType.JUDGEMENT.getValue() == entry.getKey()) {
				List<SubjectJudgement> subjects = SubjectServiceFactory.getJudgementService().findListById(ids);
				if (CollectionUtils.isNotEmpty(subjects)) {
					c = judgementConverter.convert(subjects, findAnswer);
				}
			} else if (SubjectType.FILL_BLANK.getValue() == entry.getKey()) {
				List<SubjectFillBlank> subjects = SubjectServiceFactory.getFillBlankService().findListById(ids);
				if (CollectionUtils.isNotEmpty(subjects)) {
					c = fillBlankConverter.convert(subjects, findAnswer);
				}
			}else if (SubjectType.MATERIAL.getValue() == entry.getKey()) {
				List<SubjectMaterial> subjects = SubjectServiceFactory.getSubjectMaterialService().findListById(ids);
				// 寻找附属题目
				if (CollectionUtils.isNotEmpty(subjects)) {
					c = subjectMaterialConverter.convert(subjects, findAnswer);
					for (SubjectDto subjectDto : c) {
						List<MaterialSubject> listByMaterial = msService.findListByMaterialId(subjectDto.getId());
						Long[] subjectIds = listByMaterial.stream().map(MaterialSubject::getSubjectId).toArray(Long[]::new);
						subjectDto.setChildLength(subjectIds.length);
						if (subjectIds.length > 0){
							List<Subjects> childSubjects = findBySubjectIds(subjectIds);
							List<SubjectDto> subjectDtoList = findSubjectDtoList(childSubjects, findOptions, findAnswer);
							subjectDto.setChildSubjects(subjectDtoList);
						}

					}
				}
			}

			if (CollectionUtils.isNotEmpty(c)) {
				list.addAll(c);
			}
		}

		// 获取语音和视频的 URL
		for (SubjectDto dto : list) {
			if (dto.getSpeechId() != null) {
				dto.setSpeechUrl(attachmentManager.getPreviewUrlIgnoreException(dto.getSpeechId()));
			}
			if (dto.getSubjectVideoId() != null) {
				dto.setSubjectVideoUrl(attachmentManager.getPreviewUrlIgnoreException(dto.getSubjectVideoId()));
			}
		}
		return list;
	}

	@Transactional
	public int physicalDeleteAll(Long[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				this.physicalDelete(id);
			}
		}
		return 1;
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

	public Integer findMaxSortByCategoryId(Long categoryId) {
		return this.dao.findMaxSortByCategoryId(categoryId);
	}

	public Integer nextSubjectNo(Long categoryId) {
		Integer no = this.dao.findMaxSortByCategoryId(categoryId);
		return no == null ? 1 : no + 1;
	}

	private void initCategoryInfo(List<Long> categoryIds, List<SubjectDto> sub) {
		if (CollectionUtils.isEmpty(categoryIds)) {
			return;
		}

		List<SubjectCategory> categories = subjectCategoryService.findListById(categoryIds.toArray(new Long[0]));
		if (CollectionUtils.isEmpty(categories)) {
			return;
		}

		Map<Long, SubjectCategory> map = categories.stream().collect(Collectors.toMap(SubjectCategory::getId, e -> e));
		for (SubjectDto subject : sub) {
			SubjectCategory category = map.get(subject.getCategoryId());
			if (category != null) {
				subject.setCategoryName(category.getCategoryName());
			}
		}
	}

	private int getSubjectType(Long subjectId) {
		Subjects sub = this.findBySubjectId(subjectId);
		SgPreconditions.checkNull(sub, "subjects is null");
		return sub.getType();
	}

	private Map<Long, Integer> findSubjectCountByCategories(List<SubjectCategoryDto> categories) {
		if (CollectionUtils.isEmpty(categories)) {
			return Collections.emptyMap();
		}

		List<Long> ids = Lists.newArrayList();
		for (SubjectCategoryDto category : categories) {
			ids.add(category.getId());
			addChildId(category.getChildren(), ids);
		}

		Map<Long, Integer> map = Maps.newConcurrentMap();
		ListeningExecutorService executor = executorHolder.getSubjectExecutor();
		List<ListenableFuture<?>> futures = Lists.newArrayListWithExpectedSize(categories.size());
		for (Long id : ids) {
			ListenableFuture<?> future = executor.submit(() -> {
				Integer cnt = findSubjectCountByCategoryId(id);
				if (cnt != null) {
					map.put(id, cnt);
				}
			});
			futures.add(future);
		}
		ExecutorUtils.waitFutures(futures);
		return map;
	}

	private void fillSubjectCnt(List<SubjectCategoryDto> categories, Map<Long, Integer> cntMap) {
		for (SubjectCategoryDto dto : categories) {
			Integer cnt = cntMap.getOrDefault(dto.getId(), 0);
			cnt = cnt + getTreeCnt(dto.getChildren(), cntMap);
			dto.setSubjectCnt(cnt);
		}
	}

	@SuppressWarnings("rawtypes")
	private int getTreeCnt(List<TreeEntity> entities, Map<Long, Integer> cntMap) {
		int cnt = 0;
		if (CollectionUtils.isNotEmpty(entities)) {
			for (TreeEntity<?> entity : entities) {
				cnt = cnt + cntMap.getOrDefault(entity.getId(), 0);
				cnt = cnt + getTreeCnt(entity.getChildren(), cntMap);
			}
		}
		return cnt;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void addChildId(List<TreeEntity> entities, List<Long> ids) {
		if (CollectionUtils.isNotEmpty(entities)) {
			for (TreeEntity child : entities) {
				ids.add(child.getId());
				addChildId(child.getChildren(), ids);
			}
		}
	}
}
