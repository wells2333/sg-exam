package com.github.tangyi.exam.controller.course;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.CourseSectionDto;
import com.github.tangyi.api.exam.model.ExamCourseSection;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.course.ExamCourseSectionService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 课程节Controller
 *
 * @author tangyi
 * @date 2022-11-21
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "课程节管理")
@RequestMapping("/v1/section")
public class ExamCourseSectionController extends BaseController {

	private final ExamCourseSectionService examCourseSectionService;

	@GetMapping("/list")
	@Operation(summary = "查询课程节列表")
	public R<PageInfo<ExamCourseSection>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examCourseSectionService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取课程节详细信息")
	public R<ExamCourseSection> get(@PathVariable("id") Long id) {
		return R.success(examCourseSectionService.get(id));
	}

	@GetMapping(value = "/watchSection/{id}")
	@Operation(summary = "查看课程节视频")
	@SgLog(value = "查看课程节视频", operationType = OperationType.INSERT)
	public R<CourseSectionDto> watchSection(@PathVariable("id") Long id) {
		return R.success(examCourseSectionService.watchSection(id));
	}

	@PostMapping
	@Operation(summary = "新增课程节")
	@SgLog(value = "新增课程节", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamCourseSection examCourseSection) {
		examCourseSection.setCommonValue();
		return R.success(examCourseSectionService.insert(examCourseSection) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改课程节")
	@SgLog(value = "修改课程节", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid ExamCourseSection examCourseSection) {
		examCourseSection.setId(id);
		return R.success(examCourseSectionService.update(examCourseSection) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除课程节")
	@SgLog(value = "课程节", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamCourseSection examCourseSection = examCourseSectionService.get(id);
		examCourseSection.setCommonValue();
		return R.success(examCourseSectionService.delete(examCourseSection) > 0);
	}
}
