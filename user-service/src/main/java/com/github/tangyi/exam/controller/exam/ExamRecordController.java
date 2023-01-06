package com.github.tangyi.exam.controller.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.*;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.exam.service.answer.MarkAnswerService;
import com.github.tangyi.exam.service.exam.ExaminationActionService;
import com.github.tangyi.exam.service.exam.ExaminationService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
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
	@Operation(summary = "获取考试记录信息", description = "根据考试记录id获取考试记录详细信息")
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
	@Operation(summary = "更新考试记录信息", description = "根据考试记录id更新考试记录的基本信息")
	@SgLog(value = "新考试记录", operationType = OperationType.UPDATE)
	public R<Boolean> updateExamRecord(@PathVariable Long id, @RequestBody @Valid ExaminationRecord examRecord) {
		examRecord.setId(id);
		examRecord.setCommonValue();
		return R.success(examRecordService.update(examRecord) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除考试记录", description = "根据ID删除考试记录")
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
	@Operation(summary = "导出考试成绩", description = "根据成绩id导出成绩")
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
	@Operation(summary = " 查询过去n天的考试记录数据")
	public R<ExaminationDashboardDto> findExamRecordTendency(@RequestParam @NotBlank String tenantCode,
			@RequestParam @NotBlank Integer pastDays) {
		return R.success(examRecordService.findExamRecordTendency(tenantCode, pastDays));
	}

	@PostMapping("start")
	@Operation(summary = "开始考试")
	@SgLog(value = "开始考试", operationType = OperationType.INSERT)
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
	public R<StartExamDto> anonymousUserStart(Long examinationId, String identifier) {
		return R.success(actionService.anonymousUserStart(examinationId, identifier));
	}

	@GetMapping("allSubjects/{id}")
	@Operation(summary = "获取全部题目")
	public R<List<SimpleSubjectDto>> allSubjects(@PathVariable Long id) {
		return R.success(examinationService.allSubjects(id));
	}

	@RequestMapping("anonymousUser/allSubjectList")
	@Operation(summary = "获取全部题目列表")
	public R<List<SubjectDto>> allSubjectList(SubjectDto subjectDto) {
		return R.success(examinationService.allSubjectList(subjectDto));
	}

	@GetMapping("/{examinationId}/subjectIds")
	@Operation(summary = "根据考试ID查询题目id列表")
	public R<List<ExaminationSubject>> findExaminationSubjectIds(@PathVariable Long examinationId) {
		List<ExaminationSubject> subjects = examinationService.findListByExaminationId(examinationId);
		subjects.forEach(BaseEntity::clearCommonValue);
		return R.success(subjects);
	}

	@GetMapping("/anonymousUser/{examinationId}/subjectIds")
	@Operation(summary = "根据考试ID查询题目id列表")
	public R<List<ExaminationSubject>> anonymousUserFindExaminationSubjectIds(@PathVariable Long examinationId) {
		List<ExaminationSubject> subjects = examinationService.findListByExaminationId(examinationId);
		subjects.forEach(BaseEntity::clearCommonValue);
		return R.success(subjects);
	}

	@Operation(summary = "生成二维码", description = "生成二维码")
	@GetMapping("anonymousUser/generateQrCode/{examinationId}")
	public void produceCode(@PathVariable Long examinationId, HttpServletResponse response) throws Exception {
		response.setHeader("Cache-Control", "no-store, no-com.github.tangyi.common.basic.cache");
		response.setContentType("image/jpeg");
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(examinationService.produceCode(examinationId));
				ServletOutputStream out = response.getOutputStream()) {
			BufferedImage image = ImageIO.read(inputStream);
			ImageIO.write(image, "PNG", out);
		}
	}

	@Operation(summary = "生成二维码(v2)", description = "生成二维码(v2)")
	@GetMapping("anonymousUser/generateQrCode/v2/{examinationId}")
	public void produceCodeV2(@PathVariable Long examinationId, HttpServletResponse response) throws Exception {
		response.setHeader("Cache-Control", "no-store, no-com.github.tangyi.common.basic.cache");
		response.setContentType("image/jpeg");
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(
				examinationService.produceCodeV2(examinationId));
				ServletOutputStream out = response.getOutputStream()) {
			BufferedImage image = ImageIO.read(inputStream);
			ImageIO.write(image, "PNG", out);
		}
	}

	@GetMapping("/{id}/details")
	@Operation(summary = "成绩详情", description = "根据考试记录id获取成绩详情")
	public R<ExamRecordDetailsDto> details(@PathVariable Long id) {
		return R.success(actionService.details(id));
	}
}
