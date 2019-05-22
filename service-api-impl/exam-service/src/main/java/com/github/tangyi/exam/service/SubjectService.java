package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.exam.api.module.Subject;
import com.github.tangyi.exam.mapper.SubjectMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 题目service
 *
 * @author tangyi
 * @date 2018/11/8 21:23
 */
@Service
public class SubjectService extends CrudService<SubjectMapper, Subject> {

    /**
     * 查找题目
     *
     * @param subject subject
     * @return Subject
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Cacheable(value = "subject", key = "#subject.id")
    public Subject get(Subject subject) {
        return super.get(subject);
    }

    /**
     * 根据考试ID和序号查找
     *
     * @param subject subject
     * @return Subject
     * @author tangyi
     * @date 2019/01/20 12:22
     */
    public Subject getByExaminationIdAndSerialNumber(Subject subject) {
        return this.dao.getByExaminationIdAndSerialNumber(subject);
    }

    /**
     * 根据考试ID查询题目数
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/01/23 20:19
     */
    int getExaminationTotalSubject(Subject subject) {
        return this.dao.getExaminationTotalSubject(subject);
    }

    /**
     * 新增
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/01/23 20:03
     */
    @Override
    @Transactional
    public int insert(Subject subject) {
        return super.insert(subject);
    }

    /**
     * 更新题目
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Transactional
    @CacheEvict(value = "subject", key = "#subject.id")
    public int update(Subject subject) {
        return super.update(subject);
    }

    /**
     * 删除题目
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Transactional
    @CacheEvict(value = "subject", key = "#subject.id")
    public int delete(Subject subject) {
        return super.delete(subject);
    }

    /**
     * 批量删除题目
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Transactional
    @CacheEvict(value = "subject", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }
}
