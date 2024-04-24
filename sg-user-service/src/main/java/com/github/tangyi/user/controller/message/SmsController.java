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

import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.github.tangyi.api.user.dto.SmsDto;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TenantHolder;
import com.github.tangyi.user.service.message.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Tag(name = "发送短信")
@RestController
@RequestMapping(value = "/v1/sms")
public class SmsController extends BaseController {

	private final SmsService smsService;

	@PostMapping("sendSms")
	@Operation(summary = "发送短信")
	@SgLog(value = "发送短信", operationType = OperationType.INSERT)
	public R<SendSmsResponseBody> sendSms(@RequestBody SmsDto smsDto) {
		log.info("Start to send sms message to {}, content: {}", smsDto.getReceiver(), smsDto.getContent());
		smsDto.setOperator(SysUtil.getUser());
		smsDto.setTenantCode(TenantHolder.getTenantCode());
		SendSmsResponseBody body = smsService.sendSms(smsDto);
		log.info("Send sms message finished, response: {}", body);
		return R.success(body);
	}
}
