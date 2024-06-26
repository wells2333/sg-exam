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

import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.exam.service.exam.ExaminationActionService;
import com.github.tangyi.exam.service.answer.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Tag(name = "匿名答题信息管理")
@RestController
@RequestMapping("/v1/answer")
public class AnonymousUserAnswerController extends BaseController {

	private final AnswerService answerService;
	private final ExaminationActionService actionService;

	@PostMapping("anonymousUser/submitAll/{examinationId}")
	@Operation(summary = "提交答题")
	public R<Boolean> anonymousUserSubmitAll(@PathVariable Long examinationId, @RequestParam String identifier,
			@RequestBody List<SubjectDto> subjectDtos) {
		return R.success(actionService.anonymousUserSubmit(examinationId, identifier, subjectDtos));
	}

	/**
	 * 保存答题，返回下一题信息
	 * @param nextType        0：下一题，1：上一题，2：提交
	 */
	@PostMapping("anonymousUser/saveAndNext")
	@Operation(summary = "保存答题")
	public R<SubjectDto> anonymousUserSaveAndNext(@RequestBody AnswerDto answer, @RequestParam Integer nextType,
			@RequestParam(required = false) Integer nextSubjectSortNo) {
		return R.success(answerService.saveAndNext(answer, nextType, nextSubjectSortNo));
	}

	@PostMapping("anonymousUser/submit")
	@Operation(summary = "提交答卷")
	public R<Boolean> anonymousUserSubmit(@RequestBody Answer answer) {
		return R.success(actionService.submitAsync(answer));
	}
}
