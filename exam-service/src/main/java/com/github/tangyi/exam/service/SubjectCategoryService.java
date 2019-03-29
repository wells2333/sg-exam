package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.exam.mapper.SubjectCategoryMapper;
import com.github.tangyi.exam.api.module.SubjectCategory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 题目分类service
 *
 * @author tangyi
 * @date 2018/12/4 21:56
 */
@Service
public class SubjectCategoryService extends CrudService<SubjectCategoryMapper, SubjectCategory> {

    /**
     * 查找题目分类
     *
     * @param subjectCategory subjectCategory
     * @return SubjectCategory
     * @author tangyi
     * @date 2019/1/3 14:21
     */
    @Override
    @Cacheable(value = "category", key = "#subjectCategory.id")
    public SubjectCategory get(SubjectCategory subjectCategory) {
        return super.get(subjectCategory);
    }

    /**
     * 更新题目分类
     *
     * @param subjectCategory subjectCategory
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:21
     */
    @Override
    @Transactional
    @CacheEvict(value = "category", key = "#subjectCategory.id")
    public int update(SubjectCategory subjectCategory) {
        return super.update(subjectCategory);
    }

    /**
     * 删除题目分类
     *
     * @param subjectCategory subjectCategory
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:21
     */
    @Override
    @Transactional
    @CacheEvict(value = "category", key = "#subjectCategory.id")
    public int delete(SubjectCategory subjectCategory) {
        return super.delete(subjectCategory);
    }

    /**
     * 批量删除题目分类
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:23
     */
    @Override
    @Transactional
    @CacheEvict(value = "category", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }
}
