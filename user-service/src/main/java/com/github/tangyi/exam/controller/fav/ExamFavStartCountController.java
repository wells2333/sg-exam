package com.github.tangyi.exam.controller.fav;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamFavStartCount;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.fav.FavStartCountService;
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
 * 收藏、开始数量Controller
 *
 * @author tangyi
 * @date 2022-12-17
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "收藏、开始数量管理")
@RequestMapping("/v1/count")
public class ExamFavStartCountController extends BaseController {

	private final FavStartCountService favStartCountService;

	@GetMapping("/list")
	@Operation(summary = "查询收藏、开始数量列表")
	public R<PageInfo<ExamFavStartCount>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(favStartCountService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取收藏、开始数量详细信息")
	public R<ExamFavStartCount> get(@PathVariable("id") Long id) {
		return R.success(favStartCountService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增收藏、开始数量")
	@SgLog(value = "新增收藏、开始数量", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamFavStartCount examFavStartCount) {
		examFavStartCount.setCommonValue();
		return R.success(favStartCountService.insert(examFavStartCount) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改收藏、开始数量")
	@SgLog(value = "修改收藏、开始数量", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid ExamFavStartCount examFavStartCount) {
		examFavStartCount.setId(id);
		return R.success(favStartCountService.update(examFavStartCount) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除收藏、开始数量")
	@SgLog(value = "删除收藏、开始数量", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamFavStartCount examFavStartCount = favStartCountService.get(id);
		examFavStartCount.setCommonValue();
		return R.success(favStartCountService.delete(examFavStartCount) > 0);
	}
}
