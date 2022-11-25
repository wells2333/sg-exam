package com.github.tangyi.exam.controller.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamSubjectFavorites;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.ExamSubjectFavoritesService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 收藏题目Controller
 *
 * @author tangyi
 * @date 2022-08-18
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "收藏题目管理")
@RequestMapping("/v1/favorites")
public class ExamSubjectFavoritesController extends BaseController {

	private final ExamSubjectFavoritesService examSubjectFavoritesService;

	/**
	 * 查询收藏题目列表
	 */
	@GetMapping("/list")
	@Operation(summary = "查询收藏题目列表")
	public R<PageInfo<ExamSubjectFavorites>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examSubjectFavoritesService.findPage(condition, pageNum, pageSize));
	}

	/**
	 * 获取收藏题目详细信息
	 */
	@GetMapping(value = "/{id}")
	@Operation(summary = "获取收藏题目详细信息")
	public R<ExamSubjectFavorites> get(@PathVariable("id") Long id) {
		return R.success(examSubjectFavoritesService.get(id));
	}

	/**
	 * 删除收藏题目
	 */
	@DeleteMapping("{id}")
	@Operation(summary = "删除收藏题目")
	@SgLog(value = "删除收藏题目", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamSubjectFavorites examSubjectFavorites = examSubjectFavoritesService.get(id);
		examSubjectFavorites.setCommonValue();
		return R.success(examSubjectFavoritesService.delete(examSubjectFavorites) > 0);
	}

	/**
	 * 收藏题目
	 */
	@PostMapping("favoriteSubject")
	@Operation(summary = "收藏题目")
	@SgLog(value = "收藏题目", operationType = OperationType.INSERT)
	public R<Boolean> favoriteSubject(@RequestParam Long userId, @RequestParam Long subjectId) {
		return R.success(examSubjectFavoritesService.favoriteSubject(userId, subjectId));
	}
}
