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

package com.github.tangyi.exam.controller.course;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.model.ExamCourseEvaluate;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.service.course.CourseService;
import com.github.tangyi.exam.service.course.ExamCourseEvaluateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "课程评价管理")
@RequestMapping("/v1/evaluate")
public class ExamCourseEvaluateController extends BaseController {

	private final ExamCourseEvaluateService examCourseEvaluateService;
	private final CourseService courseService;
	private final IUserService userService;
	private final AttachmentManager attachmentManager;

	@GetMapping("/list")
	@Operation(summary = "查询课程评价列表")
	public R<PageInfo<ExamCourseEvaluate>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<ExamCourseEvaluate> page = examCourseEvaluateService.findPage(condition, pageNum, pageSize);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 查询课程名称
		if (CollectionUtils.isNotEmpty(page.getList())) {
			for (ExamCourseEvaluate evaluate : page.getList()) {
				Course course = courseService.get(evaluate.getCourseId());
				if (course != null) {
					evaluate.setCourseName(course.getCourseName());
				}
				User user = userService.get(evaluate.getUserId());
				if (user != null && user.getAvatarId() != null) {
					evaluate.setAvatarUrl(attachmentManager.getPreviewUrlIgnoreException(user.getAvatarId()));
				}
				evaluate.setEvaluateTime(format.format(evaluate.getUpdateTime()));
			}
		}
		return R.success(page);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取课程评价详细信息")
	public R<ExamCourseEvaluate> get(@PathVariable("id") Long id) {
		return R.success(examCourseEvaluateService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增课程评价")
	@SgLog(value = "新增课程评价", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamCourseEvaluate examCourseEvaluate) {
		examCourseEvaluate.setCommonValue();
		Long userId = SysUtil.getUserId();
		examCourseEvaluate.setUserId(userId);
		User user = userService.get(userId);
		if (user != null) {
			examCourseEvaluate.setOperatorName(user.getName());
		}
		return R.success(examCourseEvaluateService.insert(examCourseEvaluate) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改课程评价")
	@SgLog(value = "修改课程评价", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid ExamCourseEvaluate examCourseEvaluate) {
		examCourseEvaluate.setId(id);
		return R.success(examCourseEvaluateService.update(examCourseEvaluate) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除课程评价")
	@SgLog(value = "删除课程评价", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamCourseEvaluate examCourseEvaluate = examCourseEvaluateService.get(id);
		examCourseEvaluate.setCommonValue();
		return R.success(examCourseEvaluateService.delete(examCourseEvaluate) > 0);
	}
}
