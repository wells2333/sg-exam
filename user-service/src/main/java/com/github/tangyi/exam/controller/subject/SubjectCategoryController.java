package com.github.tangyi.exam.controller.subject;

import com.github.tangyi.api.exam.constants.ExamSubjectConstant;
import com.github.tangyi.api.exam.dto.SubjectCategoryDto;
import com.github.tangyi.api.exam.model.SubjectCategory;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.subject.SubjectCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 题目分类controller
 *
 * @author tangyi
 * @date 2018/12/4 21:57
 */
@AllArgsConstructor
@Tag(name = "题库分类信息管理")
@RestController
@RequestMapping("/v1/subjectCategory")
public class SubjectCategoryController extends BaseController {

	private final SubjectCategoryService categoryService;

	/**
	 * 返回树形分类集合
	 */
	@GetMapping(value = "categoryTree")
	@Operation(summary = "获取分类列表")
	public R<List<SubjectCategoryDto>> categoryTree() {
		return R.success(categoryService.categoryTree());
	}

	@GetMapping("/{id}")
	@Operation(summary = "获取分类信息", description = "根据分类id获取分类详细信息")
	public R<SubjectCategory> subjectCategory(@PathVariable Long id) {
		return R.success(categoryService.get(id));
	}

	@PostMapping
	@Operation(summary = "创建分类", description = "创建分类")
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
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid SubjectCategory subjectCategory) {
		subjectCategory.setId(id);
		subjectCategory.setCommonValue();
		return R.success(categoryService.update(subjectCategory) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除分类", description = "根据ID删除分类")
	public R<Boolean> delete(@PathVariable Long id) {
		SubjectCategory subjectCategory = new SubjectCategory();
		subjectCategory.setId(id);
		return R.success(categoryService.delete(subjectCategory) > 0);
	}
}
