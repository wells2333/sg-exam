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

import com.github.tangyi.api.user.enums.MessageType;
import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.api.user.service.ISysMessageService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "公告管理")
@RequestMapping("/v1/notice")
public class SysNoticeController extends BaseController {

	private final ISysMessageService messageService;

	@GetMapping("/getNotice")
	@Operation(summary = "查询公告")
	public R<String> getNotice() {
		String value = null;
		SysMessage message = messageService.getByMessageType(MessageType.NOTICE.getType());
		if (message != null) {
			value = message.getContent();
		}
		return R.success(value);
	}
}
