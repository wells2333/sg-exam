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

import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectsMapper extends CrudMapper<Subjects> {

	Long findAllSubjectCount();

	Subjects findBySubjectId(Long subjectId);

	Subjects findByCategoryIdAndSort(@Param("categoryId") Long categoryId, @Param("sort") Integer sort);

	List<Subjects> findBySubjectIds(@Param("subjectIds") Long[] subjectIds);

	List<Subjects> findByCategoryId(Long categoryId);

	List<Subjects> findIdAndTypeByCategoryId(@Param("categoryId") Long categoryId);

	List<Subjects> findByCategoryIds(@Param("categoryIds") Long[] categoryIds);

	List<Subjects> findByCategoryIdAndMaxSort(@Param("categoryId") Long categoryId, @Param("sort") Integer sort);

	List<Subjects> findByType(Integer type);

	Integer findMaxSortByCategoryId(Long categoryId);

	Integer findSubjectCountByCategoryId(Long categoryId);
}
