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

import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationSubjectMapper extends CrudMapper<ExaminationSubject> {

	int deleteBySubjectId(ExaminationSubject examinationSubject);

	List<ExaminationSubject> findListBySubjectId(ExaminationSubject examinationSubject);

	List<ExaminationSubject> findListByExaminationId(Long examinationId);

	List<ExaminationSubject> findListByExaminationIdAndMaxSort(@Param("examinationId") Long examinationId,
			@Param("maxSort") Integer maxSort);

	ExaminationSubject findByExaminationIdAndSubjectId(ExaminationSubject examinationSubject);

	/**
	 * 查询序号最小的题目
	 */
	ExaminationSubject findMinSortByExaminationId(Long examinationId);

	/**
	 * 根据考试 ID 和序号查询
	 */
	ExaminationSubject findByExaminationIdAndSort(ExaminationSubject examinationSubject);

	/**
	 * 根据考试 ID 查询最大的序号
	 */
	Integer findMaxSortByExaminationId(ExaminationSubject examinationSubject);

	/**
	 * 查询考试的题目数量
	 */
	Integer findSubjectCount(Long examinationId);

	/**
	 * 根据上一题 ID 查询下一题
	 */
	ExaminationSubject getByPreviousId(ExaminationSubject examinationSubject);

	/**
	 * 根据当前题目 ID 查询上一题
	 */
	ExaminationSubject getPreviousByCurrentId(ExaminationSubject examinationSubject);

	/**
	 * 根据分类 id 查询
	 */
	List<ExaminationSubject> findListByCategoryId(Long categoryId);
}
