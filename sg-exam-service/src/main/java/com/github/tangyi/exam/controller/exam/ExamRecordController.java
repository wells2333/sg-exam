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
import com.github.tangyi.api.exam.dto.*;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.exam.enums.ExaminationType;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.exam.service.answer.MarkAnswerService;
import com.github.tangyi.exam.service.exam.ExaminationActionService;
import com.github.tangyi.exam.service.exam.ExaminationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "考试记录信息管理")
@RestController
@RequestMapping("/v1/examRecord")
public class ExamRecordController extends BaseController {

	private final ExamRecordService examRecordService;
	private final MarkAnswerService markAnswerService;
	private final ExaminationActionService actionService;
	private final ExaminationService examinationService;

	@GetMapping("/{id}")
	@Operation(summary = "获取考试记录信息", description = "根据考试记录 ID 获取考试记录详细信息")
	public R<ExaminationRecord> examRecord(@PathVariable Long id) {
		return R.success(examRecordService.get(id));
	}

	@GetMapping("examRecordList")
	@Operation(summary = "获取考试记录列表")
	public R<PageInfo<ExaminationRecordDto>> examRecordList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examRecordService.examRecordList(condition, pageNum, pageSize));
	}

	@GetMapping("userRecords/{userId}")
	@Operation(summary = "获取用户的考试记录列表")
	public R<PageInfo<ExaminationRecordDto>> userRecords(@PathVariable Long userId,
			@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examRecordService.getUserExamRecords(userId, condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "创建考试记录", description = "创建考试记录")
	@SgLog(value = "创建考试记录", operationType = OperationType.INSERT)
	public R<ExaminationRecord> addExamRecord(@RequestBody @Valid ExaminationRecord examRecord) {
		examRecord.setCommonValue();
		examRecord.setStartTime(examRecord.getCreateTime());
		examRecordService.insert(examRecord);
		return R.success(examRecord);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新考试记录信息", description = "根据考试记录 id 更新考试记录的基本信息")
	@SgLog(value = "新考试记录", operationType = OperationType.UPDATE)
	public R<Boolean> updateExamRecord(@PathVariable Long id, @RequestBody @Valid ExaminationRecord examRecord) {
		examRecord.setId(id);
		examRecord.setCommonValue();
		return R.success(examRecordService.update(examRecord) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除考试记录", description = "根据 ID 删除考试记录")
	@SgLog(value = "删除考试记录", operationType = OperationType.DELETE)
	public R<Boolean> deleteExamRecord(@PathVariable Long id) {
		ExaminationRecord examRecord = examRecordService.get(id);
		if (examRecord != null) {
			examRecord.setCommonValue();
			return R.success(examRecordService.delete(examRecord) > 0);
		}
		return R.success(Boolean.FALSE);
	}

	@PostMapping("export")
	@Operation(summary = "导出考试成绩", description = "根据成绩 id 导出成绩")
	public void exportExamRecord(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		examRecordService.exportExamRecord(ids, request, response);
	}

	@PutMapping("completeMark/{id}")
	@Operation(summary = "完成批改")
	@SgLog(value = "完成批改", operationType = OperationType.UPDATE)
	public R<Boolean> completeMark(@PathVariable Long id) {
		return R.success(markAnswerService.complete(id));
	}

	@GetMapping("dashboard")
	@Operation(summary = "查询考试监控数据")
	public R<ExaminationDashboardDto> findExamDashboardData(@RequestParam @NotBlank String tenantCode) {
		return R.success(examRecordService.findExamDashboardData(tenantCode));
	}

	@GetMapping("dashboard/examRecordTendency")
	@Operation(summary = " 查询过去 n 天的考试记录数据")
	public R<ExaminationDashboardDto> findExamRecordTendency(@RequestParam @NotBlank String tenantCode,
			@RequestParam @NotBlank Integer pastDays) {
		return R.success(examRecordService.findExamRecordTendency(tenantCode, pastDays));
	}

	@PostMapping("start")
	@Operation(summary = "开始考试")
	public R<StartExamDto> start(@RequestBody ExaminationRecord examRecord) {
		return R.success(actionService.start(examRecord));
	}

	@GetMapping("currentTime")
	@Operation(summary = "获取服务器当前时间", description = "获取服务器当前时间")
	public R<String> currentTime() {
		return R.success(DateUtils.localDateToString(LocalDateTime.now()));
	}

	@GetMapping("duration")
	@Operation(summary = "获取考试已用时间", description = "获取考试已用时间")
	public R<String> duration(@RequestParam String startTime) {
		return R.success(StopWatchUtil.duration(startTime));
	}

	@PostMapping("anonymousUser/start")
	@Operation(summary = "匿名用户开始考试")
	public R<StartExamDto> anonymousUserStart(Long examinationId, String identifier, HttpServletRequest req) {
		// 匿名用户考试，账号默认使用使用 ip 地址
		if (StringUtils.isEmpty(identifier)) {
			identifier = req.getRemoteAddr();
			log.info("Anonymous user start exam, examinationId: {}, ip: {}", examinationId, identifier);
		}
		return R.success(actionService.anonymousUserStart(examinationId, identifier));
	}

	@GetMapping("allSubjects/{id}")
	@Operation(summary = "获取全部题目")
	public R<List<SimpleSubjectDto>> allSubjects(@PathVariable Long id) {
		return R.success(examinationService.allSubjects(id));
	}

	@RequestMapping("anonymousUser/allSubjects/{id}")
	@Operation(summary = "获取全部题目列表")
	public R<List<SimpleSubjectDto>> anonymousUserAllSubjects(@PathVariable Long id) {
		Examination examination = examinationService.get(id);
		// 只返回非调查问卷类型的题目
		if (examination == null || !ExaminationType.QUESTIONNAIRE.getValue().equals(examination.getType())) {
			return R.success(Collections.emptyList());
		}

		return R.success(examinationService.allSubjects(id));
	}

	@GetMapping("/{examinationId}/subjectIds")
	@Operation(summary = "根据考试 ID 查询题目 id 列表")
	public R<List<ExaminationSubject>> findExaminationSubjectIds(@PathVariable Long examinationId) {
		List<ExaminationSubject> subjects = examinationService.findListByExaminationId(examinationId);
		subjects.forEach(BaseEntity::clearCommonValue);
		return R.success(subjects);
	}

	@GetMapping("/anonymousUser/{examinationId}/subjectIds")
	@Operation(summary = "根据考试 ID 查询题目 id 列表")
	public R<List<ExaminationSubject>> anonymousUserFindExaminationSubjectIds(@PathVariable Long examinationId) {
		List<ExaminationSubject> subjects = examinationService.findListByExaminationId(examinationId);
		subjects.forEach(BaseEntity::clearCommonValue);
		return R.success(subjects);
	}

	@GetMapping("/{id}/details")
	@Operation(summary = "成绩详情", description = "根据考试记录 id 获取成绩详情")
	public R<ExamRecordDetailsDto> details(@PathVariable Long id) {
		return R.success(actionService.details(id));
	}

	@GetMapping("calculateDuration")
	@Operation(summary = "计算时间间隔", description = "根据开始时间计算时间间隔")
	public R<String> calculateDuration(@RequestParam String startTime) {
		Date date;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(startTime);
		} catch (ParseException e) {
			log.error("Failed to parse start time {}", startTime);
			return R.success("");
		}

		Duration duration = DateUtils.calculateDuration(date, new Date());
		return R.success(DateUtils.formatDurationV2(duration, false));
	}
}
