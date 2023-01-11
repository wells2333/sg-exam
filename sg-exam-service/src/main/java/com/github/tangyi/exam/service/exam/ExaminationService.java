package com.github.tangyi.exam.service.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.RandomSubjectDto;
import com.github.tangyi.api.exam.dto.SimpleSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.api.user.service.IQiNiuService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.*;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.ExaminationType;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.course.CourseService;
import com.github.tangyi.exam.service.fav.ExamFavoritesService;
import com.github.tangyi.exam.service.image.RandomImageService;
import com.github.tangyi.exam.service.subject.SubjectCategoryService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.ExamUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ExaminationService extends CrudService<ExaminationMapper, Examination>
		implements IExaminationService, ExamCacheName {

	private final SubjectsService subjectsService;

	private final ExaminationSubjectService examinationSubjectService;

	private final SysProperties sysProperties;

	private final IQiNiuService qiNiuService;

	private final SubjectCategoryService subjectCategoryService;

	private final CourseService courseService;

	private final ExamFavoritesService examFavoritesService;

	private final RandomImageService randomImageService;

	@Override
	@Cacheable(value = ExamCacheName.EXAMINATION, key = "#id")
	public Examination get(Long id) {
		return super.get(id);
	}

	public ExaminationDto getDetail(Long id) {
		Examination examination = super.get(id);
		List<Course> courses = courseService.findListById(
				Collections.singletonList(examination.getId()).toArray(Long[]::new));
		return toDto(examination, courses);
	}

	public List<Long> findAllIds() {
		return this.dao.findAllIds();
	}

	public int insert(ExaminationDto examinationDto) {
		Examination examination = new Examination();
		BeanUtils.copyProperties(examinationDto, examination);
		if (examinationDto.getCourse() != null) {
			examination.setCourseId(examinationDto.getCourse().getId());
		}
		examination.setCommonValue();
		// 初始化图片
		if (examination.getImageId() == null) {
			examination.setImageId(qiNiuService.createRandomImage(Group.DEFAULT));
		}
		return super.insert(examination);
	}

	public PageInfo<ExaminationDto> examinationList(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<Examination> page = findPage(params, pageNum, pageSize);
		PageInfo<ExaminationDto> pageInfo = new PageInfo<>();
		BeanUtils.copyProperties(page, pageInfo);
		if (CollectionUtils.isNotEmpty(page.getList())) {
			List<ExaminationDto> dtoList = toDtoList(page.getList());
			if (Status.OPEN.equals(params.get("favorite"))) {
				examFavoritesService.findExamStartCounts(dtoList);
				examFavoritesService.findUserFavorites(dtoList);
				examFavoritesService.findFavCount(dtoList);
			}
			pageInfo.setList(dtoList);
		}
		return pageInfo;
	}

	public PageInfo<?> findUserFavoritesPage(PageInfo<ExamUserFav> page) {
		PageInfo<ExaminationDto> pageInfo = new PageInfo<>();
		BeanUtils.copyProperties(page, pageInfo);
		List<Long> examIds = page.getList().stream().map(ExamUserFav::getTargetId).collect(Collectors.toList());
		List<Examination> examinations = findListById(examIds.toArray(Long[]::new));
		List<ExaminationDto> dtoList = toDtoList(examinations);
		for (ExaminationDto dto : dtoList) {
			dto.setFavorite(true);
		}
		examFavoritesService.findExamStartCounts(dtoList);
		examFavoritesService.findFavCount(dtoList);
		pageInfo.setList(dtoList);
		return pageInfo;
	}

	public List<ExaminationDto> toDtoList(List<Examination> examinations) {
		List<Course> courses = courseService.findListById(
				examinations.stream().map(Examination::getCourseId).distinct().toArray(Long[]::new));
		return examinations.stream().map(exam -> toDto(exam, courses)).collect(Collectors.toList());
	}

	public ExaminationDto toDto(Examination exam, List<Course> courses) {
		ExaminationDto dto = new ExaminationDto();
		BeanUtils.copyProperties(exam, dto);
		// 设置考试所属课程
		courses.stream().filter(tempCourse -> tempCourse.getId().equals(exam.getCourseId())).findFirst()
				.ifPresent(dto::setCourse);
		initExaminationImage(dto);
		dto.setTypeLabel(ExaminationType.matchByValue(dto.getType()).getName());
		return dto;
	}

	/**
	 * 获取考试封面
	 */
	public void initExaminationImage(ExaminationDto examinationDto) {
		try {
			if (examinationDto.getImageId() != null && examinationDto.getImageId() != 0L) {
				String imageUrl = qiNiuService.getPreviewUrl(examinationDto.getImageId());
				examinationDto.setImageUrl(StringUtils.getIfEmpty(imageUrl, randomImageService::randomImage));
			}
		} catch (Exception e) {
			log.error("initExaminationImage failed", e);
		}
	}

	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, key = "#examinationDto.id")
	public int update(ExaminationDto examinationDto) {
		Examination examination = new Examination();
		BeanUtils.copyProperties(examinationDto, examination);
		if (examinationDto.getCourse() != null) {
			examination.setCourseId(examinationDto.getCourse().getId());
		}
		examination.setCommonValue();
		return super.update(examination);
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, key = "#examination.id")
	public int delete(Examination examination) {
		this.deleteExaminationSubject(new Long[]{examination.getId()});
		return super.delete(examination);
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, allEntries = true)
	public int deleteAll(Long[] ids) {
		this.deleteExaminationSubject(ids);
		return super.deleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, allEntries = true)
	public void deleteExaminationSubject(Long[] ids) {
		for (Long id : ids) {
			List<ExaminationSubject> examinationSubjects = examinationSubjectService.findListByExaminationId(id);
			if (CollectionUtils.isNotEmpty(examinationSubjects)) {
				ExaminationSubject es = new ExaminationSubject();
				examinationSubjects.forEach(examinationSubject -> {
					subjectsService.physicalDelete(examinationSubject.getSubjectId());
					es.setSubjectId(examinationSubject.getSubjectId());
					examinationSubjectService.deleteBySubjectId(es);
				});
			}
		}
	}

	/**
	 * 查询考试数量
	 */
	public int findExaminationCount(Examination examination) {
		return this.dao.findExaminationCount(examination);
	}

	public PageInfo<SubjectDto> findSubjectPageById(SubjectDto subjectDto, Map<String, Object> params, int pageNum,
			int pageSize) {
		// 返回结果
		PageInfo<SubjectDto> subjectDtoPageInfo = new PageInfo<>();
		// 查询考试题目关联表
		ExaminationSubject es = new ExaminationSubject();
		es.setTenantCode(SysUtil.getTenantCode());
		es.setExaminationId(subjectDto.getExaminationId());
		PageInfo<ExaminationSubject> examinationSubjects = examinationSubjectService.findPage(params, pageNum,
				pageSize);
		// 根据题目ID查询题目信息
		List<SubjectDto> subjectDtoList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(examinationSubjects.getList())) {
			Long[] subjectIds = examinationSubjects.getList().stream().map(ExaminationSubject::getSubjectId)
					.toArray(Long[]::new);
			List<Subjects> subjects = subjectsService.findBySubjectIds(subjectIds);
			subjectDtoList = subjectsService.findSubjectDtoList(subjects);
		}
		// 按序号排序
		if (CollectionUtils.isNotEmpty(subjectDtoList)) {
			subjectDtoList = subjectDtoList.stream().sorted(Comparator.comparing(SubjectDto::getSort))
					.collect(Collectors.toList());
		}
		subjectDtoPageInfo.setList(subjectDtoList);
		PageUtil.copyProperties(examinationSubjects, subjectDtoPageInfo);
		return subjectDtoPageInfo;
	}

	/**
	 * 获取全部题目
	 */
	public List<SubjectDto> allSubjectList(SubjectDto subjectDto) {
		// 查询考试题目关联表
		List<ExaminationSubject> esList = examinationSubjectService.findListByExaminationId(
				subjectDto.getExaminationId());
		if (CollectionUtils.isNotEmpty(esList)) {
			Long[] subjectIds = esList.stream().map(ExaminationSubject::getSubjectId).toArray(Long[]::new);
			List<Subjects> subjects = subjectsService.findBySubjectIds(subjectIds);
			return subjectsService.findSubjectDtoList(subjects, true, false);
		}
		return Collections.emptyList();
	}

	/**
	 * 根据考试ID查询题目id列表
	 */
	public List<ExaminationSubject> findListByExaminationId(Long examinationId) {
		return examinationSubjectService.findListByExaminationId(examinationId);
	}

	/**
	 * 查询参与考试人数
	 */
	public int findExamUserCount(Examination examination) {
		return this.dao.findExamUserCount(examination);
	}

	/**
	 * 根据考试ID生成二维码
	 */
	public byte[] produceCode(Long examinationId) {
		Examination examination = this.get(examinationId);
		// 调查问卷
		if (examination == null/* || !ExaminationTypeEnum.QUESTIONNAIRE.getValue().equals(examination.getType())*/) {
			return new byte[0];
		}
		String url = sysProperties.getQrCodeUrl() + "?id=" + examination.getId();
		byte[] bytes = null;
		try {
			bytes = QRCodeUtils.getQRCodeImage(url, 180, 180);
		} catch (Exception e) {
			log.error("produceCode failed", e);
		}
		log.info("share examinationId: {}, url: {}", examinationId, url);
		return bytes;
	}

	/**
	 * 根据考试ID生成二维码
	 */
	public byte[] produceCodeV2(Long examinationId) {
		Examination examination = this.get(examinationId);
		// 调查问卷
		if (examination == null/* || !ExaminationTypeEnum.QUESTIONNAIRE.getValue().equals(examination.getType())*/) {
			return new byte[0];
		}
		String url = sysProperties.getQrCodeUrl() + "-v2?id=" + examination.getId();
		byte[] bytes = null;
		try {
			bytes = QRCodeUtils.getQRCodeImage(url, 180, 180);
		} catch (Exception e) {
			log.error("produceCode failed", e);
		}
		log.info("Share v2 examinationId: {}, url: {}", examinationId, url);
		return bytes;
	}

	/**
	 * 查询下一题的序号
	 */
	public Integer nextSubjectNo(Long examinationId) {
		return examinationSubjectService.nextSubjectNo(examinationId);
	}

	/**
	 * 根据考试ID查询全部题目，不返回题目的答案
	 */
	@Cacheable(value = ExamCacheName.EXAM_ALL_SUBJECT, key = "#examinationId")
	public List<SimpleSubjectDto> allSubjects(Long examinationId) {
		List<ExaminationSubject> ess = examinationSubjectService.findListByExaminationId(examinationId);
		if (CollectionUtils.isNotEmpty(ess)) {
			Long[] subjectIds = ess.stream().map(ExaminationSubject::getSubjectId).toArray(Long[]::new);
			List<SubjectDto> dtoList = subjectsService.findSubjectDtoList(subjectsService.findBySubjectIds(subjectIds),
					true, false);
			List<SubjectDto> sorted = Lists.newArrayListWithExpectedSize(dtoList.size());
			for (ExaminationSubject es : ess) {
				for (SubjectDto dto : dtoList) {
					if (dto.getId().equals(es.getSubjectId())) {
						sorted.add(dto);
						break;
					}
				}
			}
			return ExamUtil.simpleSubject(sorted);
		}
		return Lists.newArrayList();
	}

	/**
	 * 根据考试ID批量添加题目
	 */
	@Transactional
	public Boolean batchAddSubjects(Long id, List<SubjectDto> subjects) {
		Integer nextNo = nextSubjectNo(id);
		for (SubjectDto subject : subjects) {
			subject.setId(null);
			subject.setCategoryId(null);
			subject.setCategoryName(null);
			subject.setNewRecord(true);
			subject.setCommonValue();
			// 自定义ID
			subject.setId(SnowFlakeId.newId());
			subject.setSort(nextNo++);
			if (CollectionUtils.isNotEmpty(subject.getOptions())) {
				for (SubjectOption option : subject.getOptions()) {
					option.setId(null);
					option.setSubjectChoicesId(null);
				}
			}
			// 关联考试ID
			subject.setExaminationId(id);
			subjectsService.insert(subject);
		}
		return Boolean.TRUE;
	}

	@Transactional
	public Boolean randomAddSubjects(Long id, RandomSubjectDto params) {
		// 校验分类是否存在
		SubjectCategory category = subjectCategoryService.get(params.getCategoryId());
		SgPreconditions.checkNull(category, "题目分类不存在");
		// 根据分类查询题目
		List<Subjects> subjects = subjectsService.findIdAndTypeByCategoryId(category.getId());
		SgPreconditions.checkCollectionEmpty(subjects, "该分类下的题目数量为空");
		// 数量校验
		Integer cnt = params.getSubjectCount();
		SgPreconditions.checkBoolean(cnt > subjects.size(), "分类下的题目数量不足" + cnt);
		List<SubjectDto> result = randomAddSubject(subjects, cnt);
		if (CollectionUtils.isNotEmpty(result)) {
			clearCurrentSubjects(id);
			batchAddSubjects(id, result);
		}
		return Boolean.TRUE;
	}

	/**
	 * 随机选取题目
	 */
	public List<SubjectDto> randomAddSubject(List<Subjects> subjects, int cnt) {
		StopWatch start = StopWatchUtil.start();
		// 已经选取的题目ID，用于去重
		Set<Long> idSet = Sets.newHashSetWithExpectedSize(cnt);
		// 已经选取的题目
		List<Subjects> tmpSubjects = Lists.newArrayListWithExpectedSize(cnt);
		int itCnt = 0;
		while (tmpSubjects.size() < cnt) {
			itCnt++;
			if (itCnt > 500) {
				throw new CommonException("随机组卷失败，itCnt：" + itCnt);
			}
			int index = ThreadLocalRandom.current().nextInt(0, subjects.size());
			Subjects tmp = subjects.get(index);
			if (!idSet.contains(tmp.getId())) {
				idSet.add(tmp.getId());
				tmpSubjects.add(tmp);
				log.info("select subject: {}，size: {}, target size: {}", tmp.getId(), tmpSubjects.size(), cnt);
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
	public void clearCurrentSubjects(Long id) {
		List<SimpleSubjectDto> subjects = allSubjects(id);
		if (CollectionUtils.isNotEmpty(subjects)) {
			for (SimpleSubjectDto subject : subjects) {
				subjectsService.physicalDelete(subject.getId());
			}
		}
	}
}
