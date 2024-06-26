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

package com.github.tangyi.operation.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.other.model.OperationBanner;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.operation.service.OperationBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 首页运营位Controller
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "首页运营位管理")
@RequestMapping("/v1/operation/banner")
public class OperationBannerController extends BaseController {

	private final OperationBannerService operationBannerService;

	/**
	 * 查询首页运营位列表
	 */
	@GetMapping("/list")
	@Operation(summary = "查询首页运营位列表")
	public R<PageInfo<OperationBanner>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(operationBannerService.findPage(condition, pageNum, pageSize));
	}

	/**
	 * 获取首页运营位详细信息
	 */
	@GetMapping(value = "/{id}")
	@Operation(summary = "获取首页运营位详细信息")
	public R<OperationBanner> get(@PathVariable("id") Long id) {
		return R.success(operationBannerService.get(id));
	}

	/**
	 * 新增首页运营位
	 */
	@PostMapping
	@Operation(summary = "新增首页运营位")
	@SgLog(value = "首页运营位", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid OperationBanner operationBanner) {
		operationBanner.setCommonValue();
		return R.success(operationBannerService.insert(operationBanner) > 0);
	}

	/**
	 * 修改首页运营位
	 */
	@PutMapping("{id}")
	@Operation(summary = "修改首页运营位")
	@SgLog(value = "首页运营位", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid OperationBanner operationBanner) {
		operationBanner.setId(id);
		return R.success(operationBannerService.update(operationBanner) > 0);
	}

	/**
	 * 删除首页运营位
	 */
	@DeleteMapping("{id}")
	@Operation(summary = "删除首页运营位")
	@SgLog(value = "首页运营位", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		OperationBanner operationBanner = operationBannerService.get(id);
		operationBanner.setCommonValue();
		return R.success(operationBannerService.delete(operationBanner) > 0);
	}
}
