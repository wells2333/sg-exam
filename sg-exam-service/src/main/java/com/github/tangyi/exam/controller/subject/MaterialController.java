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

package com.github.tangyi.exam.controller.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.RandomSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.subject.SubjectMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "材料题目管理")
@RestController
@RequestMapping("/v1/material")
public class MaterialController extends BaseController {

	private final SubjectMaterialService subjectMaterialService;

	@RequestMapping("subjectList")
	@Operation(summary = "获取题目列表")
	public R<PageInfo<SubjectDto>> subjectList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize,
			SubjectDto subjectDto) {
		return R.success(subjectMaterialService.findSubjectPageById(subjectDto, condition, pageNum, pageSize));
	}

	@PostMapping("batchAddSubjects/{id}")
	@Operation(summary = "批量添加题目")
	@SgLog(value = "批量添加题目", operationType = OperationType.INSERT)
	public R<Boolean> batchAddSubjects(@PathVariable Long id, @RequestBody HashMap<String, Object> map) {
		return R.success(subjectMaterialService.batchAddSubjects(id, map));
	}

	@PostMapping("randomAddSubjects/{id}")
	@Operation(summary = "随机添加题目")
	@SgLog(value = "随机添加题目", operationType = OperationType.INSERT)
	public R<Boolean> randomAddSubjects(@PathVariable Long id, @RequestBody RandomSubjectDto params) {
		return R.success(subjectMaterialService.randomAddSubjects(id, params));
	}
}
