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

package com.github.tangyi.user.controller.message;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysMessageRead;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.message.SysMessageReadService;
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
@Tag(name = "已读消息管理")
@RequestMapping("/v1/read")
public class SysMessageReadController extends BaseController {

	private final SysMessageReadService sysMessageReadService;

	@GetMapping("/list")
	@Operation(summary = "查询已读消息列表")
	public R<PageInfo<SysMessageRead>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysMessageReadService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取已读消息详细信息")
	public R<SysMessageRead> get(@PathVariable("id") Long id) {
		return R.success(sysMessageReadService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增已读消息")
	public R<Boolean> add(@RequestBody @Valid SysMessageRead sysMessageRead) {
		sysMessageRead.setCommonValue();
		return R.success(sysMessageReadService.insert(sysMessageRead) > 0);
	}

	@PostMapping("readMessage")
	@Operation(summary = "读消息")
	public R<Boolean> readMessage(@RequestBody @Valid SysMessageRead sysMessageRead) {
		SysMessageRead read = sysMessageReadService.findByMessageIdAndReceiverId(sysMessageRead.getMessageId(),
				sysMessageRead.getReceiverId());
		if (read == null) {
			sysMessageRead.setCommonValue();
			return R.success(sysMessageReadService.insert(sysMessageRead) > 0);
		}
		return R.success(Boolean.FALSE);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改已读消息")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysMessageRead sysMessageRead) {
		sysMessageRead.setId(id);
		return R.success(sysMessageReadService.update(sysMessageRead) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除已读消息")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		SysMessageRead sysMessageRead = sysMessageReadService.get(id);
		sysMessageRead.setCommonValue();
		return R.success(sysMessageReadService.delete(sysMessageRead) > 0);
	}
}
