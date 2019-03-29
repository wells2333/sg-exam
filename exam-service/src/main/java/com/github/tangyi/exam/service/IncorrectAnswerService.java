package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.exam.mapper.IncorrectAnswerMapper;
import com.github.tangyi.exam.api.module.ExamRecord;
import com.github.tangyi.exam.api.module.IncorrectAnswer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 错题service
 *
 * @author tangyi
 * @date 2018/11/8 21:21
 */
@Service
public class IncorrectAnswerService extends CrudService<IncorrectAnswerMapper, IncorrectAnswer> {

    /**
     * 查找错题
     *
     * @param incorrectAnswer incorrectAnswer
     * @return IncorrectAnswer
     * @author tangyi
     * @date 2019/1/3 14:12
     */
    @Override
    @Cacheable(value = "incorrect", key = "#incorrectAnswer.id")
    public IncorrectAnswer get(IncorrectAnswer incorrectAnswer) {
        return super.get(incorrectAnswer);
    }

    /**
     * 更新错题
     *
     * @param incorrectAnswer incorrectAnswer
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:13
     */
    @Override
    @Transactional
    @CacheEvict(value = "incorrect", key = "#incorrectAnswer.id")
    public int update(IncorrectAnswer incorrectAnswer) {
        return super.update(incorrectAnswer);
    }

    /**
     * 批量保存
     *
     * @param incorrectAnswerList incorrectAnswerList
     * @return int
     * @author tangyi
     * @date 2018/12/26 14:37
     */
    @Transactional
    public int insertBatch(List<IncorrectAnswer> incorrectAnswerList) {
        return this.dao.insertBatch(incorrectAnswerList);
    }

    /**
     * 删除错题
     *
     * @param incorrectAnswer incorrectAnswer
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:13
     */
    @Override
    @Transactional
    @CacheEvict(value = "incorrect", key = "#incorrectAnswer.id")
    public int delete(IncorrectAnswer incorrectAnswer) {
        return super.delete(incorrectAnswer);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:13
     */
    @Override
    @Transactional
    @CacheEvict(value = "incorrect", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }

    /**
     * 根据考试记录删除
     *
     * @param examRecord examRecord
     * @return int
     * @author tangyi
     * @date 2019/1/22 14:06
     */
    @Transactional
    @CacheEvict(value = "incorrect", allEntries = true)
    public int deleteByExaminationRecord(ExamRecord examRecord) {
        return this.dao.deleteByExaminationRecord(examRecord);
    }
}
