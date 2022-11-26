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

/**
 *
 * @author tangyi
 * @date 2022/4/14 1:38 下午
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "匿名答题信息管理")
@RestController
@RequestMapping("/v1/answer")
public class AnonymousUserAnswerController extends BaseController {

	private final AnswerService answerService;

	private final ExaminationActionService actionService;

	/**
	 * 移动端提交答题
	 * @param examinationId examinationId
	 * @return R
	 * @author tangyi
	 * @date 2020/03/15 16:08
	 */
	@PostMapping("anonymousUser/submitAll/{examinationId}")
	@Operation(summary = "提交答题")
	public R<Boolean> anonymousUserSubmitAll(@PathVariable Long examinationId, @RequestParam String identifier,
			@RequestBody List<SubjectDto> subjectDtos) {
		return R.success(actionService.anonymousUserSubmit(examinationId, identifier, subjectDtos));
	}

	/**
	 * 保存答题，返回下一题信息
	 *
	 * @param answer          answer
	 * @param nextType        0：下一题，1：上一题，2：提交
	 * @return R
	 * @author tangyi
	 * @date 2019/04/30 18:06
	 */
	@PostMapping("anonymousUser/saveAndNext")
	@Operation(summary = "保存答题")
	public R<SubjectDto> anonymousUserSaveAndNext(@RequestBody AnswerDto answer, @RequestParam Integer nextType,
			@RequestParam(required = false) Integer nextSubjectSortNo) {
		return R.success(answerService.saveAndNext(answer, nextType, nextSubjectSortNo));
	}

	/**
	 * 提交答卷
	 *
	 * @param answer answer
	 * @return R
	 * @author tangyi
	 * @date 2018/12/24 20:44
	 */
	@PostMapping("anonymousUser/submit")
	@Operation(summary = "提交答卷")
	public R<Boolean> anonymousUserSubmit(@RequestBody Answer answer) {
		return R.success(actionService.submitAsync(answer));
	}
}
