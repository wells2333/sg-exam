package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysConfig;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "系统配置管理")
@RequestMapping("/v1/config")
public class SysConfigController extends BaseController {

	private final SysConfigService sysConfigService;

	@GetMapping("/list")
	@Operation(summary = "查询系统配置列表")
	public R<PageInfo<SysConfig>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysConfigService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取系统配置详细信息")
	public R<SysConfig> get(@PathVariable("id") Long id) {
		return R.success(sysConfigService.get(id));
	}

	@GetMapping(value = "/getByKey")
	@Operation(summary = "根据key获取系统配置")
	public R<SysConfig> getByKey(@RequestParam String key) {
		return R.success(sysConfigService.getByKey(key, SysUtil.getTenantCode()));
	}

	@GetMapping(value = "/getDefaultSysConfig")
	@Operation(summary = "获取系统配置")
	public R<Map<String, Object>> getDefaultSysConfig() {
		return R.success(sysConfigService.getDefaultSysConfig());
	}

	@PostMapping
	@Operation(summary = "新增系统配置")
	@SgLog(value = "新增系统配置", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SysConfig sysConfig) {
		sysConfig.setCommonValue();
		return R.success(sysConfigService.insert(sysConfig) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改系统配置")
	@SgLog(value = "修改系统配置", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysConfig sysConfig) {
		sysConfig.setId(id);
		sysConfig.setCommonValue(SysUtil.getUser(), sysConfig.getTenantCode());
		return R.success(sysConfigService.update(sysConfig) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除系统配置")
	@SgLog(value = "删除系统配置", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		SysConfig sysConfig = sysConfigService.get(id);
		sysConfig.setCommonValue();
		return R.success(sysConfigService.delete(sysConfig) > 0);
	}
}
