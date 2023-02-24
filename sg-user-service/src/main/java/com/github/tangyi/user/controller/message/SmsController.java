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
		log.info("send message to {}, content: {}", smsDto.getReceiver(), smsDto.getContent());
		smsDto.setOperator(SysUtil.getUser());
		smsDto.setTenantCode(TenantHolder.getTenantCode());
		SendSmsResponseBody body = smsService.sendSms(smsDto);
		log.info("send message success, response: {}", body);
		return R.success(body);
	}
}
