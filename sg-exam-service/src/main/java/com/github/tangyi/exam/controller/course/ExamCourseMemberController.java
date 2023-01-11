package com.github.tangyi.exam.controller.course;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExamCourseMemberDto;
import com.github.tangyi.api.exam.model.ExamCourseMember;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.course.ExamCourseMemberService;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.log.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "课程学员管理")
@RequestMapping("/v1/member")
public class ExamCourseMemberController extends BaseController {

	private final ExamCourseMemberService examCourseMemberService;

	@GetMapping("/list")
	@Operation(summary = "查询课程学员列表")
	public R<PageInfo<ExamCourseMemberDto>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<ExamCourseMemberDto> dtoPage = new PageInfo<>();
		PageInfo<ExamCourseMember> page = examCourseMemberService.findPage(condition, pageNum, pageSize);
		BeanUtils.copyProperties(page, dtoPage);
		if (CollectionUtils.isNotEmpty(page.getList())) {
			dtoPage.setList(examCourseMemberService.toExamCourseMemberDto(page.getList()));
		}
		return R.success(dtoPage);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取课程学员详细信息")
	public R<ExamCourseMember> get(@PathVariable("id") Long id) {
		return R.success(examCourseMemberService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增课程学员")
	@SgLog(value = "新增课程学员", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamCourseMember examCourseMember) {
		examCourseMember.setCommonValue();
		return R.success(examCourseMemberService.insert(examCourseMember) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改课程学员")
	@SgLog(value = "修改课程学员", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid ExamCourseMember examCourseMember) {
		examCourseMember.setId(id);
		return R.success(examCourseMemberService.update(examCourseMember) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除课程学员")
	@SgLog(value = "删除课程学员", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamCourseMember examCourseMember = examCourseMemberService.get(id);
		examCourseMember.setCommonValue();
		return R.success(examCourseMemberService.delete(examCourseMember) > 0);
	}
}
