package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.module.Course;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.exam.mapper.CourseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    private final SysProperties sysProperties;

    /**
     * 根据id获取课程信息
     *
     * @param id id
     * @return Course
     * @author tangyi
     * @date 2018/12/03 21:30
     */
    @Cacheable(value = "course#" + CommonConstant.CACHE_EXPIRE, key = "#id")
    @Override
    public Course get(Long id) {
        Course course = super.get(id);
        if (course != null) {
            this.initLogoUrl(Collections.singletonList(course));
        }
        return course;
    }

    /**
     * 获取课程信息
     *
     * @param course course
     * @return Course
     * @author tangyi
     * @date 2018/12/03 21:30
     */
    @Override
    @Cacheable(value = "course#" + CommonConstant.CACHE_EXPIRE, key = "#course.id")
    public Course get(Course course) {
        return super.get(course);
    }

    /**
     * 初始化logo
     *
     * @param page page
     * @param course course
     * @author tangyi
     * @date 2020/03/18 20:38
     */
    @Override
    public PageInfo<Course> findPage(PageInfo<Course> page, Course course) {
        PageInfo<Course> pageInfo = super.findPage(page, course);
        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            this.initLogoUrl(pageInfo.getList());
        }
        return pageInfo;
    }

    /**
     * 更新课程信息
     *
     * @param course course
     * @return int
     * @author tangyi
     * @date 2018/12/03 21:32
     */
    @Override
    @Transactional
    @CacheEvict(value = "course", key = "#course.id")
    public int update(Course course) {
        return super.update(course);
    }

    /**
     * 删除课程信息
     *
     * @param course course
     * @return int
     * @author tangyi
     * @date 2018/12/03 21:32
     */
    @Override
    @Transactional
    @CacheEvict(value = "course", key = "#course.id")
    public int delete(Course course) {
        return super.delete(course);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:05
     */
    @Override
    @Transactional
    @CacheEvict(value = "course", allEntries = true)
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }

    /**
     * 初始化logo
     *
     * @param courseList courseList
     * @author tangyi
     * @date 2020/03/18 20:38
     */
    public void initLogoUrl(List<Course> courseList) {
        try {
            if (sysProperties.getLogoUrl() != null && !sysProperties.getLogoUrl().endsWith("/")) {
                sysProperties.setLogoUrl(sysProperties.getLogoUrl() + "/");
            }
            courseList.forEach(course -> {
                // 获取配置默认头像地址
                if (course.getLogoId() != null && course.getLogoId() != 0L) {
                    //course.setLogoUrl(AttachmentConstant.ATTACHMENT_PREVIEW_URL + course.getLogoId());
                } else {
                    Long index = new Random().nextInt(sysProperties.getLogoCount()) + 1L;
                    course.setLogoUrl(sysProperties.getLogoUrl() + index + sysProperties.getLogoSuffix());
                    course.setLogoId(index);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
