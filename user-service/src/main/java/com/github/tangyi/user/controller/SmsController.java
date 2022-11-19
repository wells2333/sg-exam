package com.github.tangyi.user.controller;

import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.github.tangyi.api.user.dto.SmsDto;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.user.service.SmsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送短信接口
 *
 * @author tangyi
 * @date 2019/6/22 12:59
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "发送短信")
@RestController
@RequestMapping(value = "/v1/sms")
public class SmsController extends BaseController {

	private final SmsService smsService;

	/**
	 * 发送短信
	 *
	 * @param smsDto smsDto
	 * @return R
	 * @author tangyi
	 * @date 2019/06/22 13:12
	 */
	@PostMapping("sendSms")
	public R<SendSmsResponseBody> sendSms(@RequestBody SmsDto smsDto) {
		log.info("send message to {}, content: {}", smsDto.getReceiver(), smsDto.getContent());
		SendSmsResponseBody body = smsService.sendSms(smsDto);
		log.info("send message success, response: {}", body);
		return R.success(body);
	}
}
