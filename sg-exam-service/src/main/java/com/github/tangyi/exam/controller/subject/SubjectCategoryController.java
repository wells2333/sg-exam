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

package com.github.tangyi.exam.controller.subject;

import com.github.tangyi.api.exam.constants.ExamSubjectConstant;
import com.github.tangyi.api.exam.dto.SubjectCategoryDto;
import com.github.tangyi.api.exam.model.SubjectCategory;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.subject.SubjectCategoryService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	@Operation(summary = "获取分类树和对应的题目数量")
	public R<List<SubjectCategoryDto>> categoryTreeWithSubjectCnt(
			@RequestParam(required = false) Map<String, Object> condition) {
		List<SubjectCategoryDto> categories = categoryService.categoryTree(condition);
		subjectsService.findAndFillSubjectCnt(categories);
		return R.success(categories);
	}

	@GetMapping(value = "getSubjectCntByParentId")
	@Operation(summary = "根据父类目 ID 获取子类目信息和对应题目数量")
	public R<List<SubjectCategoryDto>> getSubjectCntByParentId(Long parentId) {
		List<SubjectCategoryDto> categories = categoryService.getCategoryByParentId(parentId);
		subjectsService.findAndFillSubjectCnt(categories);
		return R.success(categories);
	}

	@GetMapping(value = "getCategoryInfo")
	@Operation(summary = "根据类目 ID 获取类目信息")
	public R<SubjectCategoryDto> getCategoryInfo(Long id) {
		SubjectCategory category = categoryService.get(id);
		if (category != null) {
			SubjectCategoryDto dto = new SubjectCategoryDto(category);
			subjectsService.findAndFillSubjectCnt(Collections.singletonList(dto));
			return R.success(dto);
		}
		return R.success(null);
	}

	@GetMapping("/{id}")
	@Operation(summary = "获取分类信息", description = "根据分类 id 获取分类详细信息")
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
	@Operation(summary = "更新分类信息", description = "根据分类 id 更新分类的基本信息")
	@SgLog(value = "更新分类信息", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid SubjectCategory subjectCategory) {
		subjectCategory.setId(id);
		subjectCategory.setCommonValue();
		return R.success(categoryService.update(subjectCategory) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除分类", description = "根据 ID 删除分类")
	@SgLog(value = "删除分类", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		SubjectCategory subjectCategory = new SubjectCategory();
		subjectCategory.setId(id);
		return R.success(categoryService.delete(subjectCategory) > 0);
	}
}
