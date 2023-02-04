package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.NextSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectCategoryDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.api.exam.service.IExecutorService;
import com.github.tangyi.api.exam.service.ISubjectsService;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.base.TreeEntity;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.ExecutorUtils;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.mapper.SubjectsMapper;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.data.SubjectViewCounterService;
import com.github.tangyi.exam.service.fav.SubjectFavoritesService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SubjectsService extends CrudService<SubjectsMapper, Subjects> implements ISubjectsService {

	private final SubjectServiceFactory subjectServiceFactory;

	private final ExaminationSubjectService esService;

	private final SubjectCategoryService subjectCategoryService;

	private final SubjectChoicesConverter subjectChoicesConverter;

	private final SubjectShortAnswerConverter subjectShortAnswerConverter;

	private final SubjectJudgementConverter subjectJudgementConverter;

	private final SubjectSpeechConverter subjectSpeechConverter;

	private final SubjectVideoConverter subjectVideoConverter;

	private final SubjectFavoritesService subjectFavoritesService;

	private final SubjectViewCounterService subjectViewCounterService;

	private final IExecutorService executorService;

	/**
	 * 根据单个题目ID查询题目信息，会有缓存
	 */
	public SubjectDto getSubject(Long id) {
		return getSubject(id, false);
	}

	public SubjectDto getSubject(Long id, boolean isView) {
		SubjectDto dto = null;
		Subjects subjects = this.findBySubjectId(id);
		if (subjects != null) {
			dto = getSubject(subjects.getSubjectId(), subjects.getType());
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
		List<Subjects> subjects = this.findBySubjectIds(ids.toArray(new Long[0]));
		return subjectServiceFactory.batchGetSubjects(subjects);
	}

	public Collection<SubjectDto> getSubjectsBySubjects(List<Subjects> subjects) {
		return subjectServiceFactory.batchGetSubjects(subjects);
	}

	public SubjectDto getSubject(Long subjectId, Integer type) {
		return subjectServiceFactory.service(type).getSubject(subjectId);
	}

	@Cacheable(cacheNames = ExamCacheName.SUBJECTS, key = "#subjectId")
	public Subjects findBySubjectId(Long subjectId) {
		return super.dao.findBySubjectId(subjectId);
	}

	public PageInfo<SubjectDto> findPage(Map<String, Object> params, int pageNum, int pageSize, boolean findFav,
			boolean findView, SubjectDto subjectDto) {
		if (subjectDto.getCategoryId() != null) {
			params.put("categoryId", subjectDto.getCategoryId());
		}
		PageInfo<Subjects> pageInfo = findPage(params, pageNum, pageSize);
		List<SubjectDto> dtoList = Lists.newArrayListWithExpectedSize(pageSize);
		List<Long> categoryIds = Lists.newArrayList();
		Collection<SubjectDto> list = getSubjectsBySubjects(pageInfo.getList());
		for (SubjectDto dto : list) {
			dtoList.add(dto);
			if (dto.getCategoryId() != null) {
				categoryIds.add(dto.getCategoryId());
			}
		}
		if (CollectionUtils.isNotEmpty(dtoList)) {
			dtoList = dtoList.stream().sorted(Comparator.comparing(SubjectDto::getSort)).collect(Collectors.toList());
		}
		initCategoryInfo(categoryIds, dtoList);
		if (findFav) {
			subjectFavoritesService.findUserFavorites(dtoList);
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

	public Integer findSubjectCountByCategoryId(Long categoryId) {
		return this.dao.findSubjectCountByCategoryId(categoryId);
	}

	public void findAndFillSubjectCnt(List<SubjectCategoryDto> categories) {
		Map<Long, Integer> cntMap = this.findSubjectCountByCategories(categories);
		this.fillSubjectCnt(categories, cntMap);
	}

	/**
	 * 根据分类ID查询全部题目
	 * @param categoryId categoryId
	 * @return List
	 */
	public List<Subjects> findListByCategoryId(Long categoryId) {
		return this.dao.findByCategoryId(categoryId);
	}

	/**
	 * 根据分类ID查询全部题目
	 */
	public List<Subjects> findIdAndTypeByCategoryId(Long categoryId) {
		return this.dao.findIdAndTypeByCategoryId(categoryId);
	}

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
			esService.insert(es);
		} catch (DuplicateKeyException e) {
			throw new CommonException("序号重复，请修改题目序号");
		}
	}

	@Transactional
	@CacheEvict(cacheNames = ExamCacheName.SUBJECTS, key = "#subjectDto.id")
	public SubjectDto update(SubjectDto subjectDto) {
		if ((subjectServiceFactory.service(getSubjectType(subjectDto.getId())).updateSubject(subjectDto)) == 0) {
			return this.insert(subjectDto);
		}
		esService.updateSort(subjectDto.getExaminationId(), subjectDto.getId(), subjectDto.getSort());
		return subjectDto;
	}

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
			return esService.deleteBySubjectId(examinationSubject);
		}
		return -1;
	}

	/**
	 * 查询第一题
	 */
	public SubjectDto findFirstSubjectByExaminationId(Long examinationId) {
		// 第一题的ID
		ExaminationSubject es = esService.findMinSortByExaminationId(examinationId);
		SgPreconditions.checkNull(es, "该考试未录入题目");
		SubjectDto dto = this.getSubject(es.getSubjectId());
		// 题目数量
		Integer subjectCount = esService.findSubjectCount(examinationId);
		dto.setTotal(subjectCount);
		dto.setHasMore(subjectCount != null && dto.getSort() < subjectCount);
		return dto;
	}

	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousSubjectId, Integer nextType) {
		return subjectServiceFactory.service(getSubjectType(previousSubjectId))
				.getNextByCurrentIdAndType(examinationId, previousSubjectId, nextType);
	}

	public SubjectDto nextSubjectByCategoryId(NextSubjectDto next) {
		Subjects subjects = this.findBySubjectId(next.getSubjectId());
		if (subjects == null) {
			return null;
		}
		if (next.isRandom()) {
			return nextRandomSubjectByCategoryId(next, subjects.getSort());
		}
		return nextSortedSubjectByCategoryId(next, subjects);
	}

	public SubjectDto nextSortedSubjectByCategoryId(NextSubjectDto next, Subjects subjects) {
		Integer sort = Math.max(0,
				AnswerConstant.NEXT.equals(next.getNextType()) ? subjects.getSort() + 1 : subjects.getSort() - 1);
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
				Subjects subjects = this.dao.findByCategoryIdAndSort(categoryId, (int) sort);
				if (subjects != null) {
					return this.getSubject(subjects.getSubjectId(), subjects.getType());
				}
			}
		}
		return null;
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> subjects) {
		return findSubjectDtoList(subjects, false);
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> subjects, boolean findOptions) {
		return findSubjectDtoList(subjects, findOptions, true);
	}

	/**
	 * 根据关系列表查询对应的题目的详细信息
	 */
	public List<SubjectDto> findSubjectDtoList(List<Subjects> subjects, boolean findOptions, boolean findAnswer) {
		Map<String, Long[]> idMap = ExamUtil.groupByType(subjects);
		List<SubjectDto> dtoList = Lists.newArrayList();
		if (idMap.containsKey(SubjectType.CHOICES.name())) {
			List<SubjectChoices> choices = subjectServiceFactory.getSubjectChoicesService()
					.findListById(idMap.get(SubjectType.CHOICES.name()));
			if (CollectionUtils.isNotEmpty(choices)) {
				if (findOptions) {
					choices = choices.stream().map(SubjectChoices::getId)
							.map(subjectServiceFactory.getSubjectChoicesService()::get).collect(Collectors.toList());
				}
				dtoList.addAll(subjectChoicesConverter.toDto(choices, findAnswer));
			}
		}
		if (idMap.containsKey(SubjectType.MULTIPLE_CHOICES.name())) {
			List<SubjectChoices> choices = subjectServiceFactory.getSubjectChoicesService()
					.findListById(idMap.get(SubjectType.MULTIPLE_CHOICES.name()));
			if (CollectionUtils.isNotEmpty(choices)) {
				// 查找选项信息
				if (findOptions) {
					choices = choices.stream().map(SubjectChoices::getId)
							.map(subjectServiceFactory.getSubjectChoicesService()::get).collect(Collectors.toList());
				}
				dtoList.addAll(subjectChoicesConverter.toDto(choices, findAnswer));
			}
		}
		if (idMap.containsKey(SubjectType.SHORT_ANSWER.name())) {
			List<SubjectShortAnswer> shortAnswers = subjectServiceFactory.getSubjectShortAnswerService()
					.findListById(idMap.get(SubjectType.SHORT_ANSWER.name()));
			if (CollectionUtils.isNotEmpty(shortAnswers)) {
				dtoList.addAll(subjectShortAnswerConverter.toDto(shortAnswers, findAnswer));
			}
		}
		if (idMap.containsKey((SubjectType.JUDGEMENT.name()))) {
			List<SubjectJudgement> judgements = subjectServiceFactory.getSubjectJudgementService()
					.findListById(idMap.get(SubjectType.JUDGEMENT.name()));
			if (CollectionUtils.isNotEmpty(judgements)) {
				dtoList.addAll(subjectJudgementConverter.toDto(judgements, findAnswer));
			}
		}
		if (idMap.containsKey((SubjectType.SPEECH.name()))) {
			List<SubjectSpeech> speeches = subjectServiceFactory.getSubjectSpeechService()
					.findListById(idMap.get(SubjectType.SPEECH.name()));
			if (CollectionUtils.isNotEmpty(speeches)) {
				dtoList.addAll(subjectSpeechConverter.toDto(speeches, findAnswer));
			}
		}
		if (idMap.containsKey((SubjectType.VIDEO.name()))) {
			List<SubjectVideo> subjectVideos = subjectServiceFactory.getSubjectVideoService()
					.findListById(idMap.get(SubjectType.VIDEO.name()));
			if (CollectionUtils.isNotEmpty(subjectVideos)) {
				dtoList.addAll(subjectVideoConverter.toDto(subjectVideos, findAnswer));
			}
		}
		return dtoList;
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

	public Integer nextSubjectNo(Long categoryId) {
		Integer no = this.dao.findMaxSortByCategoryId(categoryId);
		return no == null ? 1 : no + 1;
	}

	private void initCategoryInfo(List<Long> categoryIds, List<SubjectDto> subjects) {
		if (CollectionUtils.isEmpty(categoryIds)) {
			return;
		}
		List<SubjectCategory> categories = subjectCategoryService.findListById(categoryIds.toArray(new Long[0]));
		if (CollectionUtils.isEmpty(categories)) {
			return;
		}
		Map<Long, SubjectCategory> map = categories.stream().collect(Collectors.toMap(SubjectCategory::getId, e -> e));
		for (SubjectDto subject : subjects) {
			SubjectCategory category = map.get(subject.getCategoryId());
			if (category != null) {
				subject.setCategoryName(category.getCategoryName());
			}
		}
	}

	private int getSubjectType(Long subjectId) {
		Subjects subjects = this.findBySubjectId(subjectId);
		SgPreconditions.checkNull(subjects, "subjects is null");
		return subjects.getType();
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
		ListeningExecutorService executor = executorService.getSubjectExecutor();
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
