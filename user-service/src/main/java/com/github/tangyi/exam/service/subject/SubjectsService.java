package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.executor.ExecutorUtils;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.mapper.SubjectsMapper;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.subject.converter.*;
import com.github.tangyi.exam.utils.SubjectUtil;
import com.github.tangyi.user.service.CommonExecutorService;
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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class SubjectsService extends CrudService<SubjectsMapper, Subjects> {

	private final SubjectServiceFactory subjectServiceFactory;

	private final ExaminationSubjectService esService;

	private final SubjectCategoryService subjectCategoryService;

	private final SubjectChoicesConverter subjectChoicesConverter;

	private final SubjectShortAnswerConverter subjectShortAnswerConverter;

	private final SubjectJudgementConverter subjectJudgementConverter;

	private final SubjectSpeechConverter subjectSpeechConverter;

	private final SubjectVideoConverter subjectVideoConverter;

	private final CommonExecutorService commonExecutorService;

	/**
	 * 根据单个题目ID查询题目信息，会有缓存
	 */
	public SubjectDto getSubject(Long id) {
		Subjects subjects = this.findBySubjectId(id);
		if (subjects != null) {
			return getSubject(subjects.getSubjectId(), subjects.getType());
		}
		return null;
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

	public PageInfo<SubjectDto> findPage(Map<String, Object> params, int pageNum, int pageSize, SubjectDto subjectDto) {
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
		// 按序号排序
		if (CollectionUtils.isNotEmpty(dtoList)) {
			dtoList = dtoList.stream().sorted(Comparator.comparing(SubjectDto::getSort)).collect(Collectors.toList());
		}
		initCategoryInfo(categoryIds, dtoList);
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

	public void initCategoryInfo(List<Long> categoryIds, List<SubjectDto> subjects) {
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

	public Integer findSubjectCountByCategoryId(Long categoryId) {
		return this.dao.findSubjectCountByCategoryId(categoryId);
	}

	/**
	 * 并发根据类目ID查询题目数量
	 */
	public Map<Long, Integer> findSubjectCountByCategoryIds(List<Long> categoryIds) {
		Map<Long, Integer> map = Maps.newConcurrentMap();
		ListeningExecutorService executor = commonExecutorService.getSubjectExecutor();
		List<ListenableFuture<?>> futures = Lists.newArrayListWithExpectedSize(categoryIds.size());
		for (Long categoryId : categoryIds) {
			ListenableFuture<?> future = executor.submit(() -> {
				Integer cnt = findSubjectCountByCategoryId(categoryId);
				if (cnt != null) {
					map.put(categoryId, cnt);
				}
			});
			futures.add(future);
		}
		ExecutorUtils.waitFutures(futures);
		return map;
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
	 * @param categoryId categoryId
	 * @return List
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
		// 更新题目信息
		if ((subjectServiceFactory.service(getSubjectType(subjectDto.getId())).updateSubject(subjectDto)) == 0) {
			return this.insert(subjectDto);
		}
		// 更新题目序号
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

	/**
	 * 根据上一题ID查找
	 *
	 * @param examinationId     examinationId
	 * @param previousSubjectId previousSubjectId
	 * @param nextType          0：下一题，1：上一题
	 */
	@Transactional
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousSubjectId, Integer nextType) {
		return subjectServiceFactory.service(getSubjectType(previousSubjectId))
				.getNextByCurrentIdAndType(examinationId, previousSubjectId, nextType);
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
