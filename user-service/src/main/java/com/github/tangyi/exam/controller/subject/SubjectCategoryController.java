package com.github.tangyi.exam.controller.subject;

import com.github.tangyi.api.exam.constants.ExamSubjectConstant;
import com.github.tangyi.api.exam.dto.SubjectCategoryDto;
import com.github.tangyi.api.exam.model.SubjectCategory;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.subject.SubjectCategoryService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Tag(name = "题库分类信息管理")
@RestController
@RequestMapping("/v1/subjectCategory")
public class SubjectCategoryController extends BaseController {

	private final SubjectCategoryService categoryService;

	private final SubjectsService subjectsService;

	@GetMapping(value = "categoryTree")
	@Operation(summary = "获取分类列表")
	public R<List<SubjectCategoryDto>> categoryTree(@RequestParam(required = false) Map<String, Object> condition) {
		return R.success(categoryService.categoryTree(condition));
	}

	@GetMapping(value = "categoryTreeWithSubjectCnt")
	@Operation(summary = "获取分类列表和对应的题目数量")
	public R<List<SubjectCategoryDto>> categoryTreeWithSubjectCnt(
			@RequestParam(required = false) Map<String, Object> condition) {
		List<SubjectCategoryDto> tree = categoryService.categoryTree(condition);
		List<Long> ids = tree.stream().map(SubjectCategoryDto::getId).collect(Collectors.toList());
		Map<Long, Integer> map = subjectsService.findSubjectCountByCategoryIds(ids);
		for (SubjectCategoryDto dto : tree) {
			dto.setSubjectCnt(map.get(dto.getId()));
		}
		return R.success(tree);
	}

	@GetMapping("/{id}")
	@Operation(summary = "获取分类信息", description = "根据分类id获取分类详细信息")
	public R<SubjectCategory> subjectCategory(@PathVariable Long id) {
		return R.success(categoryService.get(id));
	}

	@PostMapping
	@Operation(summary = "创建分类", description = "创建分类")
	@SgLog(value = "创建分类", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SubjectCategory subjectCategory) {
		subjectCategory.setCommonValue();
		subjectCategory.setType(ExamSubjectConstant.PUBLIC_CATEGORY);
		if (subjectCategory.getParentId() == null) {
			subjectCategory.setParentId(-1L);
		}
		return R.success(categoryService.insert(subjectCategory) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新分类信息", description = "根据分类id更新分类的基本信息")
	@SgLog(value = "更新分类信息", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid SubjectCategory subjectCategory) {
		subjectCategory.setId(id);
		subjectCategory.setCommonValue();
		return R.success(categoryService.update(subjectCategory) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除分类", description = "根据ID删除分类")
	@SgLog(value = "删除分类", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		SubjectCategory subjectCategory = new SubjectCategory();
		subjectCategory.setId(id);
		return R.success(categoryService.delete(subjectCategory) > 0);
	}
}
