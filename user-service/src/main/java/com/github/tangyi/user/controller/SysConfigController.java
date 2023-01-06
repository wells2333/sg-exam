package com.github.tangyi.user.controller;

import com.github.tangyi.common.dto.SysConfigDto;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Tag(name = "系统配置信息管理")
@RestController
@RequestMapping("/v1/sysConfig")
public class SysConfigController extends BaseController {

	private final SysProperties sysProperties;

	@GetMapping
	@Operation(summary = "获取系统配置", description = "获取系统配置")
	public R<SysConfigDto> getSysConfig() {
		SysConfigDto sysConfigDto = new SysConfigDto();
		BeanUtils.copyProperties(sysProperties, sysConfigDto);
		return R.success(sysConfigDto);
	}
}
