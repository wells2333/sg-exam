package com.github.tangyi.exam.service.course;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.CourseChapterDto;
import com.github.tangyi.api.exam.dto.CourseDetailDto;
import com.github.tangyi.api.exam.dto.CourseSectionDto;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.model.ExamCourseChapter;
import com.github.tangyi.api.exam.model.ExamCourseMember;
import com.github.tangyi.api.exam.model.ExamCourseSection;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.CourseMapper;
import com.github.tangyi.exam.service.image.RandomImageService;
import com.github.tangyi.user.service.AttachmentService;
import com.github.tangyi.user.service.QiNiuService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 课程service
 *
 * @author tangyi
 * @date 2018/11/8 21:18
 */
@Slf4j
@Service
@AllArgsConstructor
public class CourseService extends CrudService<CourseMapper, Course> {

	private final AttachmentService attachmentService;

	private final QiNiuService qiNiuService;

	private final RandomImageService randomImageService;

	private final ExamCourseChapterService chapterService;

	private final ExamCourseSectionService sectionService;

	private final ExamCourseKnowledgePointService knowledgePointService;

	private final ExamCourseMemberService memberService;

	@Override
	@Cacheable(value = ExamCacheName.COURSE, key = "#id")
	public Course get(Long id) {
		Course course = super.get(id);
		if (course != null) {
			this.initCourseInfo(Collections.singletonList(course));
		}
		return course;
	}

	public List<Course> popularCourses() {
		PageInfo<Course> page = this.findPage(Maps.newHashMap(), 1, 10);
		if (CollectionUtils.isNotEmpty(page.getList())) {
			return page.getList();
		}
		return Lists.newArrayList();
	}

	@Override
	public PageInfo<Course> findPage(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<Course> pageInfo = super.findPage(params, pageNum, pageSize);
		if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
			this.initCourseInfo(pageInfo.getList());
		}
		return pageInfo;
	}

	@Override
	@Transactional
	public int insert(Course course) {
		// 没有上传图片，使用默认图片
		if (course.getImageId() == null) {
			course.setImageId(qiNiuService.createRandomImage(Group.DEFAULT));
		}
		return super.insert(course);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.COURSE, key = "#course.id")
	public int update(Course course) {
		return super.update(course);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.COURSE, key = "#course.id")
	public int delete(Course course) {
		Attachment attachment = new Attachment();
		attachment.setId(course.getImageId());
		if (attachmentService.delete(attachment) > 0) {
			return super.delete(course);
		}
		return 0;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.COURSE, allEntries = true)
	public int deleteAll(Long[] ids) {
		int update = 0;
		for (Long id : ids) {
			Course course = this.get(id);
			update += this.delete(course);
		}
		return update;
	}

	public void initCourseInfo(List<Course> courses) {
		try {
			ExamCourseMember member = new ExamCourseMember();
			for (Course course : courses) {
				// 图片
				String imageUrl = null;
				if (course.getImageId() != null && course.getImageId() != 0L) {
					imageUrl = qiNiuService.getPreviewUrl(course.getImageId());
				}
				course.setImageUrl(StringUtils.getIfEmpty(imageUrl, randomImageService::randomImage));
				// 报名人数
				member.setCourseId(course.getId());
				course.setMemberCount(memberService.findMemberCountByCourseId(member));
			}
		} catch (Exception e) {
			log.error("initCourseInfo failed", e);
		}
	}

	/**
	 * 查询课程的详细信息
	 * @param id id
	 * @return CourseDetailDto
	 */
	public CourseDetailDto getDetail(Long id) {
		CourseDetailDto dto = new CourseDetailDto();
		// 课时
		AtomicLong learnHour = new AtomicLong(0);
		// 课程信息
		Course course = this.get(id);
		// 章节
		List<CourseChapterDto> chapters = findChapters(id, learnHour);
		dto.setCourse(course);
		dto.setChapters(chapters);
		dto.setChapterSize(CollectionUtils.size(chapters) + "");
		dto.setLearnHour(learnHour.get() + " 小时");
		// 学员数量
		ExamCourseMember member = new ExamCourseMember();
		member.setCourseId(id);
		member.setUserId(SysUtil.getUserId());
		dto.setMemberCount(memberService.findMemberCountByCourseId(member) + "");
		// 是否已报名
		dto.setIsUserJoin(memberService.findByCourseIdAndUserId(id, SysUtil.getUserId()) != null);
		return dto;
	}

	public List<CourseChapterDto> findChapters(Long id, AtomicLong learnHour) {
		List<CourseChapterDto> chapterDtos = Lists.newArrayList();
		List<ExamCourseChapter> chapters = chapterService.findChaptersByCourseId(id);
		if (CollectionUtils.isNotEmpty(chapters)) {
			for (ExamCourseChapter chapter : chapters) {
				CourseChapterDto chapterDto = new CourseChapterDto();
				List<CourseSectionDto> sections = findSections(chapter, learnHour);
				chapterDto.setChapter(chapter);
				chapterDto.setSections(sections);
				chapterDtos.add(chapterDto);
			}
		}
		return chapterDtos;
	}

	private List<CourseSectionDto> findSections(ExamCourseChapter chapter, AtomicLong learnHour) {
		List<CourseSectionDto> dtoList = Lists.newArrayList();
		List<ExamCourseSection> sections = sectionService.findSectionsByChapterId(chapter.getId());
		if (CollectionUtils.isNotEmpty(sections)) {
			for (ExamCourseSection section : sections) {
				if (section.getLearnHour() != null) {
					learnHour.addAndGet(section.getLearnHour());
				}
				CourseSectionDto dto = new CourseSectionDto();
				dto.setSection(section);
				dto.setPoints(knowledgePointService.getPoints(section.getId()));
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	@Transactional
	public Boolean joinCourse(Long courseId, Long userId, String type) {
		ExamCourseMember member = new ExamCourseMember();
		member.setCourseId(courseId);
		member.setUserId(userId);
		// 报名
		if (Status.OPEN.equals(type)) {
			ExamCourseMember exist = memberService.findByCourseIdAndUserId(courseId, userId);
			if (exist != null) {
				throw new CommonException("不能重复加入");
			}
			return memberService.insert(member) > 0;
		} else {
			// 取消报名
			return memberService.deleteByCourseIdAndUserId(member) > 0;
		}
	}
}
