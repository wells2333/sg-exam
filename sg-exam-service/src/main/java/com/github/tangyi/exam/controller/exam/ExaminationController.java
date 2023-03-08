package com.github.tangyi.exam.controller.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.MemberDto;
import com.github.tangyi.api.exam.dto.RandomSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "考试信息管理")
@RestController
@RequestMapping("/v1/examination")
public class ExaminationController extends BaseController {

	private final IExaminationService examinationService;

	@GetMapping("/{id}")
	@Operation(summary = "获取考试信息", description = "根据考试 ID 获取考试信息")
	public R<Examination> examination(@PathVariable Long id) {
		return R.success(examinationService.get(id));
	}

	@GetMapping("/{id}/detail")
	@Operation(summary = "获取考试详细信息", description = "根据考试id获取考试详细信息")
	public R<ExaminationDto> detail(@PathVariable Long id) {
		return R.success(examinationService.getDetail(id));
	}

	@GetMapping("/{id}/getMembers")
	@Operation(summary = "获取考试成员 ID", description = "根据考试 ID 获取考试成员 ID")
	public R<MemberDto> getMembers(@PathVariable Long id) {
		return R.success(examinationService.getMembers(id));
	}

	@GetMapping("/anonymousUser/{id}")
	@Operation(summary = "获取考试信息", description = "根据考试id获取考试详细信息")
	public R<Examination> anonymousUserGet(@PathVariable Long id) {
		return R.success(examinationService.get(id));
	}

	@GetMapping("examinationList")
	@Operation(summary = "获取考试列表")
	public R<PageInfo<ExaminationDto>> examinationList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examinationService.examinationList(condition, pageNum, pageSize));
	}

	@GetMapping("userExaminationList")
	@Operation(summary = "获取用户有权限的考试列表")
	public R<PageInfo<ExaminationDto>> userExaminationList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examinationService.userExaminationList(condition, pageNum, pageSize));
	}

	@RequestMapping("subjectList")
	@Operation(summary = "获取题目列表")
	public R<PageInfo<SubjectDto>> subjectList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize,
			SubjectDto subjectDto) {
		return R.success(examinationService.findSubjectPageById(subjectDto, condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "创建考试", description = "创建考试")
	@SgLog(value = "创建考试", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExaminationDto examinationDto) {
		examinationDto.setCommonValue();
		return R.success(examinationService.insertExamination(examinationDto) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新考试信息", description = "根据考试id更新考试的基本信息")
	@SgLog(value = "更新考试", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid ExaminationDto examinationDto) {
		examinationDto.setId(id);
		examinationDto.setCommonValue();
		return R.success(examinationService.updateExamination(examinationDto) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除考试", description = "根据ID删除考试")
	@SgLog(value = "删除考试", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		Examination examination = examinationService.get(id);
		if (examination != null) {
			examination.setCommonValue();
			return R.success(examinationService.delete(examination) > 0);
		}
		return R.success(Boolean.FALSE);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除考试", description = "根据考试id批量删除考试")
	@SgLog(value = "删除考试", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(examinationService.deleteAll(ids) > 0);
	}

	@GetMapping("nexSubjectNo/{id}")
	@Operation(summary = "获取下一题的序号")
	public R<Integer> nexSubjectNo(@PathVariable Long id) {
		return R.success(examinationService.nextSubjectNo(id));
	}

	@PostMapping("batchAddSubjects/{id}")
	@Operation(summary = "批量添加题目")
	@SgLog(value = "批量添加题目", operationType = OperationType.INSERT)
	public R<Boolean> batchAddSubjects(@PathVariable Long id, @RequestBody List<SubjectDto> subjects) {
		return R.success(examinationService.batchAddSubjects(id, subjects));
	}

	@PostMapping("randomAddSubjects/{id}")
	@Operation(summary = "随机添加题目")
	@SgLog(value = "随机添加题目", operationType = OperationType.INSERT)
	public R<Boolean> randomAddSubjects(@PathVariable Long id, @RequestBody RandomSubjectDto params) {
		return R.success(examinationService.randomAddSubjects(id, params));
	}
}
