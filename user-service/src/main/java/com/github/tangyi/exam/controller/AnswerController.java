package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.RankInfoDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.service.ExaminationActionService;
import com.github.tangyi.exam.service.RankInfoService;
import com.github.tangyi.exam.service.answer.AnswerService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 答题controller
 *
 * @author tangyi
 * @date 2018/11/8 21:24
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "答题信息管理")
@RestController
@RequestMapping("/v1/answer")
public class AnswerController extends BaseController {

	private final AnswerService answerService;

	private final SubjectsService subjectsService;

	private final ExaminationActionService actionService;

	private final RankInfoService rankInfoService;

	@GetMapping("/{id}")
	@Operation(summary = "获取答题信息")
	public R<Answer> answer(@PathVariable Long id) {
		return R.success(answerService.get(id));
	}

	@GetMapping("answerList")
	@Operation(summary = "获取答题列表")
	public R<PageInfo<Answer>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(answerService.findPage(condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "创建答题")
	@SgLog(value = "新增答题", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid Answer answer) {
		answer.setCommonValue();
		return R.success(answerService.insert(answer) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新答题信息")
	@SgLog(value = "更新答题", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Answer answer) {
		answer.setId(id);
		answer.setCommonValue();
		return R.success(answerService.update(answer) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除答题")
	@SgLog(value = "删除答题", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		boolean success = false;
		try {
			Answer answer = answerService.get(id);
			if (answer != null) {
				answer.setCommonValue();
				success = answerService.delete(answer) > 0;
			}
		} catch (Exception e) {
			log.error("delete answer failed", e);
		}
		return R.success(success);
	}

	@PostMapping("save")
	@Operation(summary = "保存答题，并返回下一题")
	@SgLog(value = "保存答题，并返回下一题", operationType = OperationType.UPDATE)
	public R<Boolean> save(@RequestBody @Valid Answer answer) {
		return R.success(answerService.save(answer) > 0);
	}

	/**
	 * 保存答题，返回下一题信息
	 *
	 * @param answer          answer
	 * @param type        0：下一题，1：上一题，2：提交
	 * @return R
	 * @author tangyi
	 * @date 2019/04/30 18:06
	 */
	@PostMapping("saveAndNext")
	@Operation(summary = "保存答题")
	@SgLog(value = "保存答题", operationType = OperationType.UPDATE)
	public R<SubjectDto> saveAndNext(@RequestBody AnswerDto answer, @RequestParam Integer type,
			@RequestParam(required = false) Integer nextSubjectSortNo) {
		return R.success(answerService.saveAndNext(answer, type, nextSubjectSortNo));
	}

	/**
	 * 保存答题
	 *
	 * @param answer          answer
	 * @return R
	 * @author tangyi
	 * @date 2019/04/30 18:06
	 */
	@PostMapping("saveAnswer")
	@Operation(summary = "保存答题")
	@SgLog(value = "保存答题", operationType = OperationType.UPDATE)
	public R<Boolean> saveAnswer(@RequestBody AnswerDto answer) {
		return R.success(answerService.save(answer, SysUtil.getUser(), SysUtil.getTenantCode()) > 0);
	}

	/**
	 * 下一题
	 *
	 * @param examinationId       examinationId
	 * @param subjectId          subjectId
	 * @param nextType          0：下一题，1：上一题
	 * @return R
	 * @author tangyi
	 * @date 2019/04/30 18:06
	 */
	@GetMapping("nextSubject")
	@Operation(summary = "获取下一题")
	public R<SubjectDto> nextSubject(@RequestParam Long examinationId, @RequestParam Long subjectId,
			@RequestParam Integer nextType) {
		return R.success(subjectsService.getNextByCurrentIdAndType(examinationId, subjectId, nextType));
	}

	/**
	 * 提交答卷
	 *
	 * @param answer answer
	 * @return R
	 * @author tangyi
	 * @date 2018/12/24 20:44
	 */
	@PostMapping("submit")
	@Operation(summary = "提交答卷")
	@SgLog(value = "提交答卷", operationType = OperationType.UPDATE)
	public R<Boolean> submit(@RequestBody Answer answer) {
		return R.success(actionService.submitAsync(answer));
	}

	@PostMapping("submitAll")
	@Operation(summary = "批量提交答题")
	@SgLog(value = "批量提交答题", operationType = OperationType.UPDATE)
	public R<Boolean> submitAll(@RequestBody List<AnswerDto> answers) {
		return R.success(actionService.submitAll(answers));
	}

	@PutMapping("mark")
	@Operation(summary = "批改答题")
	@SgLog(value = "批改答题", operationType = OperationType.UPDATE)
	public R<Boolean> markAnswer(@RequestBody @Valid Answer answer) {
		answer.setCommonValue();
		return R.success(answerService.updateScore(answer) > 0);
	}

	/**
	 * 答题列表，包括题目的详情
	 * 支持查询正确、错误类型的题目
	 *
	 * @param recordId recordId
	 * @param answer   answer
	 * @return PageInfo
	 * @author tangyi
	 * @date 2019/06/18 19:16
	 */
	@GetMapping("record/{recordId}/answerListInfo")
	@Operation(summary = "获取答题信息列表")
	public PageInfo<AnswerDto> answerListInfo(
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) String pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) String pageSize,
			@PathVariable Long recordId, Answer answer) {
		return answerService.answerListInfo(pageNum, pageSize, recordId, answer);
	}

	/**
	 * 答题详情
	 *
	 * @param recordId        recordId
	 * @param currentSubjectId   currentSubjectId
	 * @param nextType        0：下一题，1：上一题
	 * @return R
	 * @author tangyi
	 * @date 2019/06/18 22:50
	 */
	@GetMapping("record/{recordId}/answerInfo")
	@Operation(summary = "答题详情")
	public R<AnswerDto> answerInfo(@PathVariable Long recordId, @RequestParam(required = false) Long currentSubjectId,
			@RequestParam(required = false) Integer nextType) {
		return R.success(answerService.answerInfo(recordId, currentSubjectId, nextType));
	}

	/**
	 * 获取排名数据，成绩由高到底排序，返回姓名、头像、分数信息
	 * @param recordId recordId
	 * @return R
	 * @author tangyi
	 * @date 2019/12/8 23:32
	 */
	@GetMapping("record/{recordId}/rankInfo")
	@Operation(summary = "排名列表")
	public R<List<RankInfoDto>> rankInfo(@PathVariable Long recordId) {
		return R.success(rankInfoService.getRankInfo(recordId));
	}
}
