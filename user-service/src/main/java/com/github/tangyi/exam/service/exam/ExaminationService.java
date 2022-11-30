package com.github.tangyi.exam.service.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.RandomSubjectDto;
import com.github.tangyi.api.exam.dto.SimpleSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.Id;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.zxing.QRCodeUtils;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.enums.ExaminationTypeEnum;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.course.CourseService;
import com.github.tangyi.exam.service.data.ExamFavoriteService;
import com.github.tangyi.exam.service.data.ExamStartCountService;
import com.github.tangyi.exam.service.image.RandomImageService;
import com.github.tangyi.exam.service.subject.SubjectCategoryService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.ExaminationUtil;
import com.github.tangyi.user.service.QiNiuService;
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

/**
 * 考试service
 *
 * @author tangyi
 * @date 2018/11/8 21:19
 */
@Slf4j
@AllArgsConstructor
@Service
public class ExaminationService extends CrudService<ExaminationMapper, Examination> implements ExamCacheName {

	private final SubjectsService subjectsService;

	private final ExaminationSubjectService examinationSubjectService;

	private final CourseService courseService;

	private final SysProperties sysProperties;

	private final QiNiuService qiNiuService;

	private final RandomImageService randomImageService;

	private final ExamStartCountService examStartCountService;

	private final ExamFavoriteService examFavoriteService;

	private final SubjectCategoryService subjectCategoryService;

	@Override
	@Cacheable(value = ExamCacheName.EXAMINATION, key = "#id")
	public Examination get(Long id) {
		return super.get(id);
	}

	/**
	 * 新增考试
	 *
	 * @param examinationDto examinationDto
	 * @return int
	 * @author tangyi
	 * @date 2019/1/3 14:06
	 */
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

	/**
	 * 获取分页数据
	 *
	 * @param pageNum     pageNum
	 * @param pageSize    pageSize
	 * @return PageInfo
	 * @author tangyi
	 * @date 2018/11/10 21:10
	 */
	public PageInfo<ExaminationDto> examinationList(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<Examination> page = findPage(params, pageNum, pageSize);
		PageInfo<ExaminationDto> pageInfo = new PageInfo<>();
		BeanUtils.copyProperties(page, pageInfo);
		if (CollectionUtils.isNotEmpty(page.getList())) {
			List<ExaminationDto> dtoList = toDtoList(page.getList());
			if (Status.OPEN.equals(params.get("favorite"))) {
				examStartCountService.findExamStartCount(dtoList);
				examFavoriteService.findUserFavorites(dtoList);
				examFavoriteService.findExamFavCount(dtoList);
			}
			pageInfo.setList(dtoList);
		}
		return pageInfo;
	}

	public PageInfo<ExaminationDto> findUserFavoritesByPage(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<ExaminationDto> pageInfo = new PageInfo<>();
		List<Long> examIds = examFavoriteService.findUserFavoritesExamIds();
		if (CollectionUtils.isNotEmpty(examIds)) {
			List<Examination> examinations = this.dao.findListById(examIds.toArray(Long[]::new));
			List<ExaminationDto> dtoList = toDtoList(examinations);
			for (ExaminationDto dto : dtoList) {
				dto.setFavorite(true);
			}
			pageInfo.setList(dtoList);
		}
		return pageInfo;
	}

	public List<ExaminationDto> toDtoList(List<Examination> examinations) {
		List<Course> courses = courseService.findListById(
				examinations.stream().map(Examination::getCourseId).distinct().toArray(Long[]::new));
		return examinations.stream().map(exam -> {
			ExaminationDto dto = new ExaminationDto();
			BeanUtils.copyProperties(exam, dto);
			// 设置考试所属课程
			courses.stream().filter(tempCourse -> tempCourse.getId().equals(exam.getCourseId())).findFirst()
					.ifPresent(dto::setCourse);
			initExaminationImage(dto);
			dto.setTypeLabel(ExaminationTypeEnum.matchByValue(dto.getType()).getName());
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * 更新考试
	 *
	 * @param examinationDto examinationDto
	 * @return int
	 * @author tangyi
	 * @date 2019/1/3 14:07
	 */
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

	/**
	 * 删除题目、考试题目关联信息
	 *
	 * @param ids ids
	 * @author tangyi
	 * @date 2018/12/4 9:51
	 */
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
	 *
	 * @param examination examination
	 * @return int
	 * @author tangyi
	 * @date 2019/3/1 15:32
	 */
	public int findExaminationCount(Examination examination) {
		return this.dao.findExaminationCount(examination);
	}

	/**
	 * 根据考试ID获取题目分页数据
	 *
	 * @param subjectDto subjectDto
	 * @param pageNum    pageNum
	 * @param pageSize   pageSize
	 * @return PageInfo
	 */
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
	 *
	 * @param subjectDto subjectDto
	 * @return List
	 * @author tangyi
	 * @date 2020/3/12 1:00 下午
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
	 *
	 * @param examinationId examinationId
	 * @return ExaminationSubject
	 * @author tangyi
	 * @date 2019/06/18 14:34
	 */
	public List<ExaminationSubject> findListByExaminationId(Long examinationId) {
		return examinationSubjectService.findListByExaminationId(examinationId);
	}

	/**
	 * 查询参与考试人数
	 *
	 * @param examination examination
	 * @return int
	 * @author tangyi
	 * @date 2019/10/27 20:08:58
	 */
	public int findExamUserCount(Examination examination) {
		return this.dao.findExamUserCount(examination);
	}

	/**
	 * 获取考试封面
	 *
	 * @param examinationDto examinationDto
	 * @author tangyi
	 * @date 2020/03/12 22:32:30
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

	/**
	 * 根据考试ID生成二维码
	 *
	 * @param examinationId examinationId
	 * @author tangyi
	 * @date 2020/3/15 1:16 下午
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
	 *
	 * @param examinationId examinationId
	 * @author tangyi
	 * @date 2020/3/21 5:38 下午
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
	 * @param examinationId examinationId
	 * @return Integer
	 */
	public Integer nextSubjectNo(Long examinationId) {
		return examinationSubjectService.nextSubjectNo(examinationId);
	}

	/**
	 * 根据考试ID查询全部题目，不返回题目的答案
	 * @param examinationId examinationId
	 * @return List
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
			return ExaminationUtil.simpleSubject(sorted);
		}
		return Lists.newArrayList();
	}

	/**
	 * 根据考试ID批量添加题目
	 * @param id id
	 * @param subjects subjects
	 * @return Boolean
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
			subject.setId(Id.nextId());
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
		List<SubjectDto> dtoList = subjectsService.findListByCategoryId(category.getId());
		SgPreconditions.checkCollectionEmpty(dtoList, "该分类下的题目数量为空");
		// 数量校验
		Integer cnt = params.getSubjectCount();
		SgPreconditions.checkBoolean(cnt > dtoList.size(), "分类下的题目数量不足" + cnt);
		List<SubjectDto> result = randomAddSubject(dtoList, cnt);
		if (CollectionUtils.isNotEmpty(result)) {
			clearCurrentSubjects(id);
			batchAddSubjects(id, result);
		}
		return Boolean.TRUE;
	}

	/**
	 * 随机选取题目
	 * @param dtoList dtoList
	 * @param cnt cnt
	 * @return List
	 */
	public List<SubjectDto> randomAddSubject(List<SubjectDto> dtoList, int cnt) {
		StopWatch start = StopWatchUtil.start();
		// 已经选取的题目ID，用于去重
		Set<Long> idSet = Sets.newHashSetWithExpectedSize(cnt);
		// 已经选取的题目
		List<SubjectDto> result = Lists.newArrayListWithExpectedSize(cnt);
		int itCnt = 0;
		while (result.size() < cnt) {
			itCnt++;
			if (itCnt > 500) {
				throw new CommonException("随机组卷失败，itCnt：" + itCnt);
			}
			int index = ThreadLocalRandom.current().nextInt(0, dtoList.size());
			SubjectDto dto = dtoList.get(index);
			if (!idSet.contains(dto.getId())) {
				idSet.add(dto.getId());
				result.add(dto);
				log.info("select subject: {}，size: {}, target size: {}", dto.getId(), result.size(), cnt);
			}
		}
		log.info("randomAddSubject success, itCnt: {}, took: {}", itCnt, StopWatchUtil.stop(start));
		return result;
	}

	/**
	 * 清空现有的题目
	 */
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
