package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.exam.mapper.CourseMapper;
import com.github.tangyi.exam.api.module.Course;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程service
 *
 * @author tangyi
 * @date 2018/11/8 21:18
 */
@Service
public class CourseService extends CrudService<CourseMapper, Course> {

    /**
     * 获取课程信息
     *
     * @param course course
     * @return Course
     * @author tangyi
     * @date 2018/12/03 21:30
     */
    @Override
    @Cacheable(value = "course", key = "#course.id")
    public Course get(Course course) {
        return super.get(course);
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
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }
}
