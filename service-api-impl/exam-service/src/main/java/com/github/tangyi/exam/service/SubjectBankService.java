package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.exam.mapper.SubjectBankMapper;
import com.github.tangyi.exam.api.module.SubjectBank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 题库service
 *
 * @author tangyi
 * @date 2018/12/9 14:11
 */
@Service
public class SubjectBankService extends CrudService<SubjectBankMapper, SubjectBank> {

    /**
     * 查找题库
     *
     * @param subjectBank subjectBank
     * @return SubjectBank
     * @author tangyi
     * @date 2019/1/3 14:19
     */
    @Override
    @Cacheable(value = "bank", key = "#subjectBank.id")
    public SubjectBank get(SubjectBank subjectBank) {
        return super.get(subjectBank);
    }

    /**
     * 更新题库
     *
     * @param subjectBank subjectBank
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:19
     */
    @Override
    @Transactional
    @CacheEvict(value = "bank", key = "#subjectBank.id")
    public int update(SubjectBank subjectBank) {
        return super.update(subjectBank);
    }

    /**
     * 删除题库
     *
     * @param subjectBank subjectBank
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:19
     */
    @Override
    @Transactional
    @CacheEvict(value = "bank", key = "#subjectBank.id")
    public int delete(SubjectBank subjectBank) {
        return super.delete(subjectBank);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:19
     */
    @Override
    @Transactional
    @CacheEvict(value = "bank", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }
}
