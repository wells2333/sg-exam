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
import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.service.message.SysMessageService;
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
@Tag(name = "消息管理")
@RequestMapping("/v1/message")
public class SysMessageController extends BaseController {

	private final SysMessageService sysMessageService;

	@GetMapping("/list")
	@Operation(summary = "查询消息列表")
	public R<PageInfo<SysMessage>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysMessageService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取消息详细信息")
	public R<SysMessage> get(@PathVariable("id") Long id) {
		return R.success(sysMessageService.get(id));
	}

	@GetMapping("userMessageList")
	@Operation(summary = "查询用户的消息接收列表")
	public R<PageInfo<SysMessage>> userMessageList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysMessageService.getPublishedMessage(condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "新增消息")
	@SgLog(value = "消息", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SysMessage sysMessage) {
		return R.success(sysMessageService.addMessage(sysMessage) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改消息")
	@SgLog(value = "消息", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysMessage sysMessage) {
		sysMessage.setId(id);
		sysMessage.setCommonValue(SysUtil.getUser(), SysUtil.getTenantCode());
		return R.success(sysMessageService.update(sysMessage) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除消息")
	@SgLog(value = "消息", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		SysMessage sysMessage = sysMessageService.get(id);
		sysMessage.setCommonValue();
		return R.success(sysMessageService.delete(sysMessage) > 0);
	}
}
