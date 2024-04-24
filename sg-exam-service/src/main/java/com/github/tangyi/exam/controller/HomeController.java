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

package com.github.tangyi.exam.controller;

import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "首页信息")
@RestController
@RequestMapping("/v1/home")
public class HomeController extends BaseController {

	private final IUserService userService;
	private final ICourseService courseService;
	private final IExaminationService examinationService;
	private final SubjectsService subjectsService;

	@GetMapping("summary")
	public R<Object> summary() {
		Map<String, Object> res = Maps.newHashMapWithExpectedSize(4);
		res.put("userCount", userService.findAllUserCount());
		res.put("courseCount", courseService.findAllCourseCount());
		res.put("examinationCount", examinationService.findAllExaminationCount());
		res.put("subjectCount", subjectsService.findAllSubjectCount());
		return R.success(res);
	}
}
