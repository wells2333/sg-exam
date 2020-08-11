package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.module.Answer;
import com.github.tangyi.common.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 答题Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:09
 */
@Mapper
public interface AnswerMapper extends CrudMapper<Answer> {

    /**
     * 根据用户ID、考试ID、考试记录ID、题目ID查找答题
     *
     * @param answer answer
     * @return Answer
     * @author tangyi
     * @date 2019/01/21 19:38
     */
    Answer getAnswer(Answer answer);

    /**
     * 根据examRecordId查询
     * @param examRecordId examRecordId
     * @return List
     * @author tangyi
     * @date 2020/2/21 1:08 下午
     */
    List<Answer> findListByExamRecordId(Long examRecordId);
}
