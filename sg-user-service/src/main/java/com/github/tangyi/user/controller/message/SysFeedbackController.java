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

package com.github.tangyi.user.controller.message;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysFeedback;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.message.SysFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "反馈信息管理")
@RequestMapping("/v1/feedback")
public class SysFeedbackController extends BaseController {

	private final SysFeedbackService sysFeedbackService;

	@GetMapping("/list")
	@Operation(summary = "查询反馈信息列表")
	public R<PageInfo<SysFeedback>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysFeedbackService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取反馈信息详细信息")
	public R<SysFeedback> get(@PathVariable("id") Long id) {
		return R.success(sysFeedbackService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增反馈信息")
	@SgLog(value = "新增反馈信息", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SysFeedback sysFeedback) {
		sysFeedback.setCommonValue();
		return R.success(sysFeedbackService.insert(sysFeedback) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改反馈信息")
	@SgLog(value = "修改反馈信息", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysFeedback sysFeedback) {
		return R.success(sysFeedbackService.update(sysFeedback) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除反馈信息")
	@SgLog(value = "删除反馈信息", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		SysFeedback sysFeedback = sysFeedbackService.get(id);
		sysFeedback.setCommonValue();
		return R.success(sysFeedbackService.delete(sysFeedback) > 0);
	}
}
