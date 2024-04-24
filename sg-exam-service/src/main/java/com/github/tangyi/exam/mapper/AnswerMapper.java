/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
