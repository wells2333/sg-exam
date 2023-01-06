package com.github.tangyi.exam.controller.course;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamCourseChapter;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.course.ExamCourseChapterService;
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
 * 课程章Controller
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "课程章管理")
@RequestMapping("/v1/chapter")
public class ExamCourseChapterController extends BaseController {

	private final ExamCourseChapterService examCourseChapterService;

	@GetMapping("/list")
	@Operation(summary = "查询课程章列表")
	public R<PageInfo<ExamCourseChapter>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examCourseChapterService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取课程章详细信息")
	public R<ExamCourseChapter> get(@PathVariable("id") Long id) {
		return R.success(examCourseChapterService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增课程章")
	@SgLog(value = "新增课程章", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamCourseChapter examCourseChapter) {
		examCourseChapter.setCommonValue();
		return R.success(examCourseChapterService.insert(examCourseChapter) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改课程章")
	@SgLog(value = "修改课程章", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid ExamCourseChapter examCourseChapter) {
		examCourseChapter.setId(id);
		return R.success(examCourseChapterService.update(examCourseChapter) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除课程章")
	@SgLog(value = "删除课程章", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamCourseChapter examCourseChapter = examCourseChapterService.get(id);
		examCourseChapter.setCommonValue();
		return R.success(examCourseChapterService.delete(examCourseChapter) > 0);
	}
}
