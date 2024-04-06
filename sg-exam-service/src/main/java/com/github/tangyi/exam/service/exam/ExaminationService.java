package com.github.tangyi.exam.service.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.RandomSubjectDto;
import com.github.tangyi.api.exam.dto.SimpleSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.*;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.lucene.DocType;
import com.github.tangyi.common.lucene.IndexCrudParam;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.*;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.constants.ExamConstant;
import com.github.tangyi.exam.constants.ExamConstantProperty;
import com.github.tangyi.exam.enums.ExaminationType;
import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import com.github.tangyi.exam.service.ExamPermissionService;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.exam.service.MaterialSubjectService;
import com.github.tangyi.exam.service.course.CourseService;
import com.github.tangyi.exam.service.fav.ExamFavoritesService;
import com.github.tangyi.exam.service.subject.SubjectCategoryService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.ExamUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
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
		implements IExaminationService, ExamCacheName, ExamConstant {

	private final SubjectsService subjectsService;
	private final ExaminationSubjectService examinationSubjectService;
	private final SysProperties sysProperties;
	private final AttachmentManager attachmentManager;
	private final SubjectCategoryService subjectCategoryService;
	private final CourseService courseService;
	private final ExamFavoritesService examFavoritesService;
	private final ExamPermissionService examPermissionService;
	private final IUserService userService;
	private final ExamIdFetcher examIdFetcher;
	private final MaterialSubjectService msService;

	private ExamConstantProperty examConstantProperty;
	@Override
	public Long findAllExaminationCount() {
		return this.dao.findAllExaminationCount();
	}

	@Override
	@Cacheable(value = ExamCacheName.EXAMINATION, key = "#id")
	public Examination get(Long id) {
		return super.get(id);
	}

	public List<Long> findAllIds() {
		return this.examIdFetcher.fetchAll(0, Collections.emptyMap());
	}

	@Override
	public PageInfo<Examination> findUserExaminations(Map<String, Object> params, int pageNum, int pageSize) {
		this.commonPageParam(params, pageNum, pageSize);
		return new PageInfo<>(this.dao.findUserExaminations(params));
	}

	@Override
	public ExaminationDto getDetail(Long id) {
		Examination e = super.get(id);
		Long[] ids = Collections.singletonList(e.getId()).toArray(Long[]::new);
		List<Course> courses = courseService.findListById(ids);
		Map<Long, Course> courseMap = toCourseMap(courses);
		return toDto(e, courseMap);
	}

	@Override
	@Transactional
	public int insertExamination(ExaminationDto examinationDto) {
		Examination e = Examination.of(examinationDto);
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		e.setCommonValue(user, tenantCode);
		Course course = examinationDto.getCourse();
		if (course != null) {
			e.setCourseId(course.getId());
		}
		if (e.getImageId() == null && e.getImageUrl() == null) {
//			e.setImageId(attachmentManager.defaultImage(Group.DEFAULT));
			e.setImageUrl(examConstantProperty.getExaminationImageUrl());
		}
		this.addExaminationMembers(examinationDto, user, tenantCode);
		int update = super.insert(e);
		if (update > 0) {
			IndexCrudParam param = IndexCrudParam.builder() //
					.id(e.getId()) //
					.type(DocType.EXAM) //
					.contents(Lists.newArrayList(e.getExaminationName(), e.getRemark())) //
					.updateTime(e.getUpdateTime().getTime()) //
					.clickCnt(0) //
					.joinCnt(0) //
					.build(); //
			super.addIndex(param);
		}
		return update;
	}

	@Override
	public PageInfo<ExaminationDto> examinationList(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<Examination> page = findPage(params, pageNum, pageSize);
		List<Examination> list = page.getList();
		if (CollectionUtils.isNotEmpty(list)) {
			return toDtoPage(page, list, params);
		}
		return new PageInfo<>();
	}

	@Override
	public PageInfo<ExaminationDto> userExaminationList(Map<String, Object> params, int pageNum, int pageSize) {
		Long userId = SysUtil.getUserId();
		params.put("userId", userId);
		User user = userService.get(userId);
		Preconditions.checkNotNull(user, "user not found");
		Long deptId = user.getDeptId();
		if (deptId != null) {
			params.put("deptId", deptId);
		}
		PageInfo<Examination> page = this.findUserExaminations(params, pageNum, pageSize);
		List<Examination> list = page.getList();
		if (CollectionUtils.isNotEmpty(list)) {
			return toDtoPage(page, list, params);
		}

		return new PageInfo<>();
	}

	@Override
	public int findExaminationRecordCountByExaminationId(String examinationId) {
		return this.dao.findExaminationRecordCountByExaminationId(examinationId);
	}

	public PageInfo<?> findUserFavoritesPage(PageInfo<ExamUserFav> page) {
		List<Long> examIds = page.getList().stream().map(ExamUserFav::getTargetId).toList();
		List<Examination> examinations = findListById(examIds.toArray(Long[]::new));
		List<ExaminationDto> dtoList = toDtoList(examinations);
		for (ExaminationDto dto : dtoList) {
			dto.setFavorite(true);
		}
		examFavoritesService.findExamStartCounts(dtoList);
		examFavoritesService.findFavCount(dtoList);
		return PageUtil.newPageInfo(page, dtoList);
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, key = "#examinationDto.id")
	public int updateExamination(ExaminationDto examinationDto) {
		Examination e = Examination.of(examinationDto);
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		e.setCommonValue(user, tenantCode);
		Course course = examinationDto.getCourse();
		if (course != null) {
			e.setCourseId(course.getId());
		}
		this.examPermissionService.deletePermission(PERMISSION_TYPE_EXAM, e.getId());
		this.addExaminationMembers(examinationDto, user, tenantCode);
		int update = super.update(e);
		if (update > 0) {
			Integer memberCnt = this.examPermissionService.findCount(PERMISSION_TYPE_EXAM, e.getId());
			long joinCnt = memberCnt == null ? 0 : memberCnt;
			IndexCrudParam param = IndexCrudParam.builder() //
					.id(e.getId()) //
					.type(DocType.EXAM) //
					.contents(Lists.newArrayList(e.getExaminationName(), e.getRemark())) //
					.updateTime(e.getUpdateTime().getTime()) //
					.clickCnt(joinCnt) //
					.joinCnt(joinCnt) //
					.build(); //
			super.updateIndex(param);
		}
		return update;
	}

	@Transactional
	public void addExaminationMembers(ExaminationDto dto, String user, String tenantCode) {
		// 未发布
		if (!EXAM_STATUS_PUBLISHED.equals(dto.getStatus())) {
			return;
		}

		Long id = dto.getId();
		// 用户 ID
		examPermissionService.addPermissions(id, dto.getMembers(), PERMISSION_TYPE_EXAM, PERMISSION_ID_TYPE_USER, user,
				tenantCode);
		// 部门 ID
		String deptMember = dto.getDeptMember();
		if (StringUtils.isNotEmpty(deptMember)) {
			examPermissionService.addPermissions(id, Collections.singletonList(Long.valueOf(deptMember)),
					PERMISSION_TYPE_EXAM, PERMISSION_ID_TYPE_DEPT, user, tenantCode);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, key = "#examination.id")
	public int delete(Examination examination) {
		this.deleteExaminationSubject(new Long[]{examination.getId()});
		int update = super.delete(examination);
		if (update > 0) {
			super.deleteIndex(examination.getId(), DocType.EXAM);
		}
		return update;
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, allEntries = true)
	public int deleteAll(Long[] ids) {
		this.deleteExaminationSubject(ids);
		int update = super.deleteAll(ids);
		if (update > 0) {
			for (Long id : ids) {
				super.deleteIndex(id, DocType.EXAM);
			}
		}
		return update;
	}

	@Transactional
	@CacheEvict(value = {ExamCacheName.EXAMINATION, ExamCacheName.EXAM_ALL_SUBJECT}, allEntries = true)
	public void deleteExaminationSubject(Long[] ids) {
		for (Long id : ids) {
			List<ExaminationSubject> subjects = examinationSubjectService.findListByExaminationId(id);
			if (CollectionUtils.isNotEmpty(subjects)) {
				ExaminationSubject es = new ExaminationSubject();
				subjects.forEach(s -> {
					subjectsService.physicalDelete(s.getSubjectId());
					es.setSubjectId(s.getSubjectId());
					examinationSubjectService.deleteBySubjectId(es);
				});
			}
		}
	}

	public int findExaminationCount(Examination examination) {
		return this.dao.findExaminationCount(examination);
	}

    public PageInfo<SubjectDto> findSubjectPageById(SubjectDto subjectDto, Map<String, Object> params, int pageNum,
                                                    int pageSize) {
        // 查询考试题目关联表
        ExaminationSubject es = new ExaminationSubject();
        es.setTenantCode(SysUtil.getTenantCode());
        es.setExaminationId(subjectDto.getExaminationId());
        PageInfo<ExaminationSubject> examinationSubjects = examinationSubjectService.findPage(params, pageNum,
                pageSize);
        List<SubjectDto> subjectDtoList = Lists.newArrayList();
        // 根据题目 ID 查询题目信息
        if (CollectionUtils.isNotEmpty(examinationSubjects.getList())) {
            Long[] subjectIds = examinationSubjects.getList().stream().map(ExaminationSubject::getSubjectId)
                    .toArray(Long[]::new);
            List<Subjects> subjects = subjectsService.findBySubjectIds(subjectIds);
            subjectDtoList = subjectsService.findSubjectDtoList(subjects);
            //是否需要对题目信息进行查询
            if (params.get("subjectName") != null) {
                String subjectName = params.get("subjectName").toString();
                subjectDtoList = subjectDtoList.stream()
                        .filter(subject -> subject.getSubjectName().contains(subjectName))
                        .collect(Collectors.toList());
            }
        }
        // 按序号排序
        if (CollectionUtils.isNotEmpty(subjectDtoList)) {
            subjectDtoList = subjectDtoList.stream().sorted(Comparator.comparing(SubjectDto::getSort))
                    .collect(Collectors.toList());
        }
        return PageUtil.newPageInfo(examinationSubjects, subjectDtoList);
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
	 * 根据考试 ID 查询题目 id 列表
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

	@Override
	public String generateQrCodeMessage(Long examinationId) {
		String baseUrl = sysProperties.getQrCodeUrl();
		if (StringUtils.isEmpty(baseUrl)) {
			throw new IllegalStateException("The qrCode url is not config.");
		}

		return baseUrl + "?id=" + examinationId;
	}

	/**
	 * 根据考试 ID 生成二维码
	 */
	@Override
	public byte[] generateQrCode(Long examinationId) {
		String url = this.generateQrCodeMessage(examinationId);
		byte[] bytes = null;
		try {
			bytes = QRCodeUtils.getQRCodeImage(url, 180, 180);
		} catch (Exception e) {
			log.error("Failed to generate qrCode, examinationId: {}", examinationId, e);
		}
		log.info("Generate qrCode finished, examinationId: {}, url: {}", examinationId, url);
		return bytes;
	}

	/**
	 * 查询下一题的序号
	 */
	@Override
	public Integer nextSubjectNo(Long examinationId) {
		return examinationSubjectService.nextSubjectNo(examinationId);
	}

	/**
	 * 根据考试 ID 查询全部题目，不返回题目的答案
	 */
//	@Cacheable(value = ExamCacheName.EXAM_ALL_SUBJECT, key = "#examinationId")
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
						if (dto.getType().equals(SubjectType.MATERIAL.getValue())){
							List<SubjectDto> childSubjects = dto.getChildSubjects();
							sorted.addAll(childSubjects);
						}
						break;
					}
				}
			}
			return ExamUtil.simpleSubject(sorted);
		}
		return Lists.newArrayList();
	}

	/**
	 * 根据考试 ID 批量添加题目
	 */
	@Transactional
	public Boolean batchAddSubjects(Long id, List<SubjectDto> subjects) {
		Integer nextNo = nextSubjectNo(id);
		for (SubjectDto subject : subjects) {
			Long oldId = subject.getId();
			subject.setParentId(oldId);
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
			subject.setExaminationId(id);
			subjectsService.insert(subject);
			// 关联材料题下面的题目
			if (id != null){
				if (subject.getType().equals(SubjectType.MATERIAL.getValue())){
					// 找到老题目
					List<MaterialSubject> list = msService.findListByMaterialId(oldId);
					for (int i = 0; i < list.size(); i++) {
						MaterialSubject materialSubject = list.get(i);
						materialSubject.setExaminationId(subject.getExaminationId());
						// 和新添加的题目进行绑定
						materialSubject.setMaterialId(subject.getId());
						materialSubject.setId(null);
						msService.insert(materialSubject);
					}
				}
			}
		}
		return Boolean.TRUE;
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
		Integer cnt = params.getSubjectCount();
		SgPreconditions.checkBoolean(cnt > subjects.size(), "The category's subject is not enough.");
		List<SubjectDto> result = this.randomAddSubject(subjects, cnt);
		if (CollectionUtils.isNotEmpty(result)) {
			this.batchAddSubjects(id, result);
		}
		return Boolean.TRUE;
	}

	@Override
	public void addIndex(Examination examination, long clickCnt, long joinCnt) {
		super.addIndex(this.buildIndexCrudParam(examination, joinCnt, joinCnt));
	}

	@Override
	public void updateIndex(Examination examination) {
		Integer memberCnt = this.examPermissionService.findCount(PERMISSION_TYPE_EXAM, examination.getId());
		long joinCnt = memberCnt == null ? 0 : memberCnt;
		super.updateIndex(this.buildIndexCrudParam(examination, joinCnt, joinCnt));
	}

	@Transactional
	public void clearSubjects(Long id) {
		List<SimpleSubjectDto> subjects = allSubjects(id);
		if (CollectionUtils.isNotEmpty(subjects)) {
			for (SimpleSubjectDto subject : subjects) {
				subjectsService.physicalDelete(subject.getId());
			}
		}
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

	private PageInfo<ExaminationDto> toDtoPage(PageInfo<Examination> page, List<Examination> list,
			Map<String, Object> params) {
		List<ExaminationDto> dtoList = toDtoList(list);
		for (ExaminationDto examinationDto : dtoList) {
			int count = findExaminationRecordCountByExaminationId(String.valueOf(examinationDto.getId()));
			examinationDto.setJoinNum(count);
		}
		if (params.get("sort") != null && "join".equals(params.get("sort"))) {
			this.dao.findAllExaminationCount();
			// 使用自定义的比较器按照考试数量对列表进行排序
			Collections.sort(dtoList, new Comparator<ExaminationDto>() {
				@Override
				public int compare(ExaminationDto exam1, ExaminationDto exam2) {
					int count1 = exam1.getJoinNum();
					int count2 = exam2.getJoinNum();
					// 按照考试数量进行降序排序
					return Integer.compare(count2, count1);
				}
			});
		}
		if (Status.OPEN.equals(params.get("favorite"))) {
			examFavoritesService.findExamStartCounts(dtoList);
			examFavoritesService.findUserFavorites(dtoList);
			examFavoritesService.findFavCount(dtoList);
		}
		return PageUtil.newPageInfo(page, dtoList);
	}

	private List<ExaminationDto> toDtoList(List<Examination> examinations) {
		Long[] ids = examinations.stream().map(Examination::getCourseId).distinct().toArray(Long[]::new);
		List<Course> courses = courseService.findListById(ids);
		Map<Long, Course> courseMap = toCourseMap(courses);
		return examinations.stream().map(exam -> toDto(exam, courseMap)).collect(Collectors.toList());
	}

	private Map<Long, Course> toCourseMap(List<Course> courses) {
		Map<Long, Course> courseMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(courses)) {
			for (Course course : courses) {
				courseMap.put(course.getId(), course);
			}
		}
		return courseMap;
	}

	private ExaminationDto toDto(Examination exam, Map<Long, Course> courseMap) {
		ExaminationDto dto = ExaminationDto.of(exam);
		Optional.ofNullable(courseMap.get(exam.getCourseId())).ifPresent(dto::setCourse);
		initExaminationImage(dto);
		dto.setTypeLabel(ExaminationType.matchByValue(dto.getType()).getName());
		return dto;
	}

	private void initExaminationImage(ExaminationDto examinationDto) {
		if (examinationDto.getImageId() != null && examinationDto.getImageId() != 0L) {
			examinationDto.setImageUrl(attachmentManager.getPreviewUrlIgnoreException(examinationDto.getImageId()));
		}
	}

	private IndexCrudParam buildIndexCrudParam(Examination examination, long clickCnt, long joinCnt) {
		return IndexCrudParam.builder() //
				.id(examination.getId()) //
				.type(DocType.EXAM) //
				.contents(Lists.newArrayList(examination.getExaminationName(), examination.getRemark())) //
				.updateTime(examination.getUpdateTime().getTime()) //
				.clickCnt(clickCnt) //
				.joinCnt(joinCnt) //
				.build(); //
	}
}
