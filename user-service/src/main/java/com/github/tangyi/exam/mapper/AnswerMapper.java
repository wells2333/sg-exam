package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 答题Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:09
 */
@Repository
public interface AnswerMapper extends CrudMapper<Answer> {

	/**
	 * 批量插入
	 *
	 * @param answers answers
	 * @return Answer
	 * @author tangyi
	 * @date 2019/01/21 19:38
	 */
	int batchInsert(@Param("answers") List<Answer> answers);

	/**
	 * 批量更新
	 *
	 * @param answers answers
	 * @return Answer
	 * @author tangyi
	 * @date 2019/01/21 19:38
	 */
	int batchUpdate(@Param("answers") List<Answer> answers);

	/**
	 * 根据考试记录ID、题目ID查询
	 *
	 * @param answer answer
	 * @return Answer
	 * @author tangyi
	 * @date 2019/01/21 19:38
	 */
	Answer findByRecordIdAndSubjectId(Answer answer);

	/**
	 * 根据考试记录ID、题目ID批量查询
	 *
	 * @return Answer
	 * @author tangyi
	 * @date 2019/01/21 19:38
	 */
	List<Answer> batchFindByRecordIdAndSubjectId(@Param("recordId") Long recordId,
			@Param("subjectIds") Long[] subjectIds);

	/**
	 * 根据examRecordId查询
	 * @param examRecordId examRecordId
	 * @return List
	 * @author tangyi
	 * @date 2020/2/21 1:08 下午
	 */
	List<Answer> findListByExamRecordId(Long examRecordId);

	/**
	 * 统计正确、错误答题数量
	 * @param examRecordId examRecordId
	 * @param answerType answerType
	 * @return Integer
	 */
	Integer countByAnswerType(@Param("examRecordId") Long examRecordId, @Param("answerType") Integer answerType);

	/**
	 * 计算总分
	 * @param examRecordId examRecordId
	 * @param answerType answerType
	 * @return Integer
	 */
	Integer sumScoreByAnswerType(@Param("examRecordId") Long examRecordId, @Param("answerType") Integer answerType);
}
