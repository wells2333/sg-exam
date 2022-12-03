package com.github.tangyi.exam.controller.course;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamCourseKnowledgePoint;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.course.ExamCourseKnowledgePointService;
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
 * 章节知识点Controller
 *
 * @author tangyi
 * @date 2022-12-02
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "章节知识点管理")
@RequestMapping("/v1/knowledgePoint")
public class ExamCourseKnowledgePointController extends BaseController {

	private final ExamCourseKnowledgePointService examCourseKnowledgePointService;

	@GetMapping("/list")
	@Operation(summary = "查询章节知识点列表")
	public R<PageInfo<ExamCourseKnowledgePoint>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examCourseKnowledgePointService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取章节知识点详细信息")
	public R<ExamCourseKnowledgePoint> get(@PathVariable("id") Long id) {
		return R.success(examCourseKnowledgePointService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增章节知识点")
	@SgLog(value = "新增章节知识点", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		examCourseKnowledgePoint.setCommonValue();
		return R.success(examCourseKnowledgePointService.insert(examCourseKnowledgePoint) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改章节知识点")
	@SgLog(value = "修改章节知识点", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id,
			@RequestBody @Valid ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		examCourseKnowledgePoint.setId(id);
		return R.success(examCourseKnowledgePointService.update(examCourseKnowledgePoint) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除章节知识点")
	@SgLog(value = "删除章节知识点", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamCourseKnowledgePoint examCourseKnowledgePoint = examCourseKnowledgePointService.get(id);
		examCourseKnowledgePoint.setCommonValue();
		return R.success(examCourseKnowledgePointService.delete(examCourseKnowledgePoint) > 0);
	}
}
