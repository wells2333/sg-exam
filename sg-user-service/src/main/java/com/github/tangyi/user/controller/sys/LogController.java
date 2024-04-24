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

package com.github.tangyi.user.controller.sys;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.sys.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "日志信息管理")
@RestController
@RequestMapping("/v1/log")
public class LogController extends BaseController {

	private final LogService logService;

	@GetMapping("/{id}")
	@Operation(summary = "获取日志信息", description = "根据日志 ID 获取日志详细信息")
	public Log log(@PathVariable Long id) {
		return logService.get(id);
	}

	@GetMapping("logList")
	@Operation(summary = "获取日志列表")
	public R<PageInfo<Log>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(logService.findPage(condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "新增日志", description = "新增日志")
	public R<Boolean> addLog(@RequestBody @Valid Log log) {
		return R.success(logService.insert(log) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除日志", description = "根据 ID 删除日志")
	public R<Boolean> delete(@PathVariable Long id) {
		Log log = new Log();
		log.setId(id);
		return R.success(logService.delete(log) > 0);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除日志", description = "根据日志 ids 批量删除日志")
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(ArrayUtils.isNotEmpty(ids) ? logService.deleteAll(ids) > 0 : Boolean.FALSE);
	}
}
