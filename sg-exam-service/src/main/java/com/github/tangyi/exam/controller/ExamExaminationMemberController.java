package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamExaminationMember;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.service.ExamExaminationMemberService;
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

	private final ExamExaminationMemberService examExaminationMemberService;

	@GetMapping("/list")
	@Operation(summary = "查询考试成员列表")
	public R<PageInfo<ExamExaminationMember>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examExaminationMemberService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取考试成员详细信息")
	public R<ExamExaminationMember> get(@PathVariable("id") Long id) {
		return R.success(examExaminationMemberService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增考试成员")
	@SgLog(value = "新增考试成员", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamExaminationMember examExaminationMember) {
		examExaminationMember.setCommonValue();
		return R.success(examExaminationMemberService.insert(examExaminationMember) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改考试成员")
	@SgLog(value = "修改考试成员", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id,
			@RequestBody @Valid ExamExaminationMember examExaminationMember) {
		examExaminationMember.setId(id);
		examExaminationMember.setCommonValue(SysUtil.getUser(), examExaminationMember.getTenantCode());
		return R.success(examExaminationMemberService.update(examExaminationMember) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除考试成员")
	@SgLog(value = "删除考试成员", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamExaminationMember examExaminationMember = examExaminationMemberService.get(id);
		examExaminationMember.setCommonValue();
		return R.success(examExaminationMemberService.delete(examExaminationMember) > 0);
	}
}
