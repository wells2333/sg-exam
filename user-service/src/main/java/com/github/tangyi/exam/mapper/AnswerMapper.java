package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerMapper extends CrudMapper<Answer> {

	int batchInsert(@Param("answers") List<Answer> answers);

	int batchUpdate(@Param("answers") List<Answer> answers);

	Answer findByRecordIdAndSubjectId(Answer answer);

	List<Answer> batchFindByRecordIdAndSubjectId(@Param("recordId") Long recordId,
			@Param("subjectIds") Long[] subjectIds);

	List<Answer> findListByExamRecordId(Long examRecordId);

	/**
	 * 统计正确、错误答题数量
	 */
	Integer countByAnswerType(@Param("examRecordId") Long examRecordId, @Param("answerType") Integer answerType);

	/**
	 * 计算总分
	 */
	Integer sumScoreByAnswerType(@Param("examRecordId") Long examRecordId, @Param("answerType") Integer answerType);
}
