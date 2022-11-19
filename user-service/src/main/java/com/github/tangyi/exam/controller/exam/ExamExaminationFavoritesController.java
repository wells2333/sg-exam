package com.github.tangyi.exam.controller.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamExaminationFavorites;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.ExamExaminationFavoritesService;
import com.github.tangyi.exam.service.data.ExamFavoriteService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 收藏Controller
 *
 * @author tangyi
 * @date 2022-08-18
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "收藏管理")
@RequestMapping("/v1/favoriteExam")
public class ExamExaminationFavoritesController extends BaseController {

	private final ExamExaminationFavoritesService examExaminationFavoritesService;

	private final ExamFavoriteService examFavoriteService;

	/**
	 * 查询收藏列表
	 */
	@GetMapping("/list")
	@Operation(summary = "查询收藏列表")
	public R<PageInfo<ExamExaminationFavorites>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examExaminationFavoritesService.findPage(condition, pageNum, pageSize));
	}

	/**
	 * 获取收藏详细信息
	 */
	@GetMapping(value = "/{id}")
	@Operation(summary = "获取收藏详细信息")
	public R<ExamExaminationFavorites> get(@PathVariable("id") Long id) {
		return R.success(examExaminationFavoritesService.get(id));
	}

	/**
	 * 删除收藏
	 */
	@DeleteMapping("{id}")
	@Operation(summary = "删除收藏")
	@SgLog(value = "收藏", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamExaminationFavorites examExaminationFavorites = examExaminationFavoritesService.get(id);
		examExaminationFavorites.setCommonValue();
		return R.success(examExaminationFavoritesService.delete(examExaminationFavorites) > 0);
	}

	/**
	 * 收藏考试
	 */
	@PostMapping("favoriteExam/{examinationId}")
	@Operation(summary = "收藏考试")
	@SgLog(value = "收藏考试", operationType = OperationType.UPDATE)
	public R<Boolean> favoriteExam(@PathVariable Long examinationId, @RequestParam Long userId,
			@RequestParam Integer type) {
		return R.success(examFavoriteService.favoriteExam(userId, examinationId, type));
	}
}
