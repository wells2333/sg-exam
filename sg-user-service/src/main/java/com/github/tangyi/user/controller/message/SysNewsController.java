package com.github.tangyi.user.controller.message;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysNews;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.message.SysNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "新闻管理")
@RequestMapping("/v1/news")
public class SysNewsController extends BaseController {

	private final SysNewsService sysNewsService;

	@GetMapping("/list")
	@Operation(summary = "查询新闻列表")
	public R<PageInfo<SysNews>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysNewsService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取新闻详细信息")
	public R<SysNews> get(@PathVariable("id") Long id) {
		return R.success(sysNewsService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增新闻")
	@SgLog(value = "新闻", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SysNews sysNews) {
		sysNews.setCommonValue();
		return R.success(sysNewsService.insert(sysNews) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改新闻")
	@SgLog(value = "新闻", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysNews sysNews) {
		return R.success(sysNewsService.update(sysNews) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除新闻")
	@SgLog(value = "新闻", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		SysNews sysNews = sysNewsService.get(id);
		sysNews.setCommonValue();
		return R.success(sysNewsService.delete(sysNews) > 0);
	}
}
