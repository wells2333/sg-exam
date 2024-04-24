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

package com.github.tangyi.exam.controller.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamCourseEvaluate;
import com.github.tangyi.api.exam.model.ExamEvaluate;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.service.exam.ExamEvaluateService;
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
@Tag(name = "考试评价管理")
@RequestMapping("/v1/exam/evaluate")
public class ExamEvaluateController extends BaseController {

	private final ExamEvaluateService examEvaluateService;
	private final IExaminationService examinationService;
	private final IUserService userService;
	private final AttachmentManager attachmentManager;

	@GetMapping("/list")
	@Operation(summary = "查询考试评价列表")
	public R<PageInfo<ExamEvaluate>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<ExamEvaluate> page = examEvaluateService.findPage(condition, pageNum, pageSize);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (CollectionUtils.isNotEmpty(page.getList())) {
			for (ExamEvaluate evaluate : page.getList()) {
				Examination examination = examinationService.get(evaluate.getExamId());
				if (examination != null) {
					evaluate.setExaminationName(examination.getExaminationName());
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
	@Operation(summary = "获取考试评价详细信息")
	public R<ExamEvaluate> get(@PathVariable("id") Long id) {
		return R.success(examEvaluateService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增考试评价")
	@SgLog(value = "新增考试评价", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamEvaluate evaluate) {
		evaluate.setCommonValue();
		Long userId = SysUtil.getUserId();
		evaluate.setUserId(userId);
		User user = userService.get(userId);
		if (user != null) {
			evaluate.setOperatorName(user.getName());
		}
		return R.success(examEvaluateService.insert(evaluate) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改考试评价")
	@SgLog(value = "修改课程评价", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid ExamEvaluate evaluate) {
		evaluate.setId(id);
		return R.success(examEvaluateService.update(evaluate) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除考试评价")
	@SgLog(value = "删除考试评价", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamEvaluate evaluate = examEvaluateService.get(id);
		evaluate.setCommonValue();
		return R.success(examEvaluateService.delete(evaluate) > 0);
	}
}
