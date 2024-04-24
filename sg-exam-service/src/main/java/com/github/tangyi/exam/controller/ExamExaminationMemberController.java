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

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamPermission;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.service.ExamPermissionService;
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
@Tag(name = "考试成员管理")
@RequestMapping("/v1/examinationMember")
public class ExamExaminationMemberController extends BaseController {

	private final ExamPermissionService examExaminationMemberService;

	@GetMapping("/list")
	@Operation(summary = "查询考试成员列表")
	public R<PageInfo<ExamPermission>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examExaminationMemberService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取考试成员详细信息")
	public R<ExamPermission> get(@PathVariable("id") Long id) {
		return R.success(examExaminationMemberService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增考试成员")
	@SgLog(value = "新增考试成员", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamPermission examExaminationMember) {
		examExaminationMember.setCommonValue();
		return R.success(examExaminationMemberService.insert(examExaminationMember) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改考试成员")
	@SgLog(value = "修改考试成员", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id,
			@RequestBody @Valid ExamPermission examExaminationMember) {
		examExaminationMember.setId(id);
		examExaminationMember.setCommonValue(SysUtil.getUser(), examExaminationMember.getTenantCode());
		return R.success(examExaminationMemberService.update(examExaminationMember) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除考试成员")
	@SgLog(value = "删除考试成员", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamPermission examExaminationMember = examExaminationMemberService.get(id);
		examExaminationMember.setCommonValue();
		return R.success(examExaminationMemberService.delete(examExaminationMember) > 0);
	}
}
