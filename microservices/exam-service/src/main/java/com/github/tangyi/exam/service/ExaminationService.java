package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.module.Course;
import com.github.tangyi.api.exam.module.Examination;
import com.github.tangyi.api.exam.module.ExaminationSubject;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.module.Attachment;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.zxing.QRCodeUtils;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
public class ExaminationService extends CrudService<ExaminationMapper, Examination> {

    private final SubjectService subjectService;

    private final ExaminationSubjectService examinationSubjectService;

	private final CourseService courseService;

	private final SysProperties sysProperties;

    /**
     * 查询考试
     *
     * @param examination examination
     * @return Examination
     * @author tangyi
     * @date 2019/1/3 14:06
     */
    @Override
    @Cacheable(value = "examination#" + CommonConstant.CACHE_EXPIRE, key = "#examination.id")
    public Examination get(Examination examination) {
        return super.get(examination);
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
		this.initExaminationLogo(examinationDto);
		Examination examination = new Examination();
		BeanUtils.copyProperties(examinationDto, examination);
		examination.setCourseId(examinationDto.getCourse().getId());
		examination.setCommonValue();
		return super.insert(examination);
	}

	/**
	 * 获取分页数据
	 *
	 * @param pageNum     pageNum
	 * @param pageSize    pageSize
	 * @param sort        sort
	 * @param order       order
	 * @param examination examination
	 * @return PageInfo
	 * @author tangyi
	 * @date 2018/11/10 21:10
	 */
	public PageInfo<ExaminationDto> examinationList(String pageNum, String pageSize, String sort, String order, Examination examination) {
		examination.setTenantCode(SysUtil.getTenantCode());
		PageInfo<Examination> page = findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examination);
		PageInfo<ExaminationDto> examinationDtoPageInfo = new PageInfo<>();
		BeanUtils.copyProperties(page, examinationDtoPageInfo);
		if (CollectionUtils.isNotEmpty(page.getList())) {
			List<Course> courses = courseService.findListById(page.getList().stream().map(Examination::getCourseId).distinct().toArray(Long[]::new));
			List<ExaminationDto> examinationDtos = page.getList().stream().map(exam -> {
				ExaminationDto examinationDto = new ExaminationDto();
				BeanUtils.copyProperties(exam, examinationDto);
				// 设置考试所属课程
				courses.stream().filter(tempCourse -> tempCourse.getId().equals(exam.getCourseId())).findFirst().ifPresent(examinationDto::setCourse);
				// 初始化封面图片
				this.initExaminationLogo(examinationDto);
				return examinationDto;
			}).collect(Collectors.toList());
			examinationDtoPageInfo.setList(examinationDtos);
		}
		return examinationDtoPageInfo;
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
    @CacheEvict(value = "examinationDto", key = "#examinationDto.id")
    public int update(ExaminationDto examinationDto) {
    	if (examinationDto.getAvatarId() == null || examinationDto.getAvatarId() == 0L) {
    		this.initExaminationLogo(examinationDto);
		}
		Examination examination = new Examination();
		BeanUtils.copyProperties(examinationDto, examination);
		if (examinationDto.getCourse() != null)
			examination.setCourseId(examinationDto.getCourse().getId());
		examination.setCommonValue();
        return super.update(examination);
    }

    /**
     * 删除考试
     *
     * @param examination examination
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:07
     */
    @Override
    @Transactional
    @CacheEvict(value = "examination", key = "#examination.id")
    public int delete(Examination examination) {
		this.deleteExaminationSubject(new Long[]{examination.getId()});
        return super.delete(examination);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2018/12/4 9:51
     */
    @Override
    @Transactional
    @CacheEvict(value = "examination", allEntries = true)
    public int deleteAll(Long[] ids) {
		this.deleteExaminationSubject(ids);
        return super.deleteAll(ids);
    }

	/**
	 * 删除题目、考试题目关联信息
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2018/12/4 9:51
	 */
	@Transactional
    public void deleteExaminationSubject(Long[] ids) {
		for (Long id : ids) {
			List<ExaminationSubject> examinationSubjects = examinationSubjectService
					.findListByExaminationId(id);
			if (CollectionUtils.isNotEmpty(examinationSubjects)) {
				// 删除考试题目
				SubjectDto subjectDto = new SubjectDto();
				ExaminationSubject es = new ExaminationSubject();
				examinationSubjects.forEach(examinationSubject -> {
					subjectDto.setId(examinationSubject.getSubjectId());
					subjectDto.setType(examinationSubject.getType());
					subjectService.physicalDelete(subjectDto);
					// 删除关联表
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
     * @param sort       sort
     * @param order      order
     * @return PageInfo
     * @author tangyi
     * @date 2019/06/16 16:00
     */
    public PageInfo<SubjectDto> findSubjectPageById(SubjectDto subjectDto, String pageNum, String pageSize, String sort, String order) {
        // 返回结果
        PageInfo<SubjectDto> subjectDtoPageInfo = new PageInfo<>();
        // 查询考试题目关联表
        ExaminationSubject examinationSubject = new ExaminationSubject();
        examinationSubject.setTenantCode(SysUtil.getTenantCode());
        examinationSubject.setExaminationId(subjectDto.getExaminationId());
		examinationSubject.setCategoryId(subjectDto.getCategoryId());
        PageInfo<ExaminationSubject> examinationSubjects = examinationSubjectService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examinationSubject);
        // 根据题目ID查询题目信息
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examinationSubjects.getList())) {
            // 按题目类型分组，获取对应的ID集合
            subjectDtoList = subjectService.findSubjectDtoList(examinationSubjects.getList());
        }
		subjectDtoPageInfo.setList(subjectDtoList);
		PageUtil.copyProperties(examinationSubjects, subjectDtoPageInfo);
        return subjectDtoPageInfo;
    }

	/**
	 * 获取全部题目
	 * @param subjectDto subjectDto
	 * @return List
	 * @author tangyi
	 * @date 2020/3/12 1:00 下午
	 */
    public List<SubjectDto> allSubjectList(SubjectDto subjectDto) {
		// 查询考试题目关联表
		List<ExaminationSubject> examinationSubjects = examinationSubjectService.findListByExaminationId(subjectDto.getExaminationId());
		if (CollectionUtils.isNotEmpty(examinationSubjects)) {
			return subjectService.findSubjectDtoList(examinationSubjects, true, false);
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
	public void initExaminationLogo(ExaminationDto examinationDto) {
		try {
			if (sysProperties.getLogoUrl() != null && !sysProperties.getLogoUrl().endsWith("/")) {
				sysProperties.setLogoUrl(sysProperties.getLogoUrl() + "/");
			}
			// 获取配置默认头像地址
			if (examinationDto.getAvatarId() != null && examinationDto.getAvatarId() != 0L) {
				Attachment attachment = new Attachment();
				attachment.setId(examinationDto.getAvatarId());
				examinationDto.setLogoUrl(AttachmentConstant.ATTACHMENT_PREVIEW_URL + examinationDto.getAvatarId());
			} else {
				Long index = new Random().nextInt(sysProperties.getLogoCount()) + 1L;
				examinationDto.setLogoUrl(sysProperties.getLogoUrl() + index + sysProperties.getLogoSuffix());
				examinationDto.setAvatarId(index);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据考试ID生成二维码
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
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String url = sysProperties.getQrCodeUrl() + "?id=" + examination.getId();
		// TODO
		//QRCodeUtils.encoderQRCode(url, outputStream, "png");
		log.info("Share examinationId: {}, url: {}", examinationId, url);
		return outputStream.toByteArray();
	}

	/**
	 * 根据考试ID生成二维码
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
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String url = sysProperties.getQrCodeUrl() + "-v2?id=" + examination.getId();
		// TODO
		//QRCodeUtils.encoderQRCode(url, outputStream, "png");
		log.info("Share v2 examinationId: {}, url: {}", examinationId, url);
		return outputStream.toByteArray();
	}
}
