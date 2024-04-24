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

package com.github.tangyi.api.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.CourseDetailDto;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;
import java.util.Map;

public interface ICourseService extends ICrudService<Course> {

	Long findAllCourseCount();

	List<Long> findAllIds();

	PageInfo<Course> userCourseList(Map<String, Object> params, int pageNum, int pageSize);

	PageInfo<Course> findUserCourses(Map<String, Object> params, int pageNum, int pageSize);

	List<Course> popularCourses(String findFav);

	CourseDetailDto getDetail(Long id);

	Boolean joinCourse(Long courseId, Long userId);

	CourseDetailDto getCourseAttach(Long courseId);

	void addIndex(Course course, long clickCnt, long joinCnt);

	void updateIndex(Course course);
}
