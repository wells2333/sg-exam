package com.github.tangyi.user.service;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.github.tangyi.api.user.dto.SmsDto;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.user.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

	public static final String SMS_ENDPOINT = EnvUtils.getValue("SMS_ENDPOINT", "dysmsapi.aliyuncs.com");

	private final SmsProperties smsProperties;

	private final com.aliyun.dysmsapi20170525.Client client;

	public SmsService(SmsProperties smsProperties) throws Exception {
		this.smsProperties = smsProperties;
		try {
			Config config = new Config().setAccessKeyId(smsProperties.getAppKey())
					.setAccessKeySecret(smsProperties.getAppSecret());
			config.endpoint = SMS_ENDPOINT;
			this.client = new com.aliyun.dysmsapi20170525.Client(config);
		} catch (Exception e) {
			log.error("init sms client failed", e);
			throw e;
		}
	}


	/**
	 * 发送短信
	 *
	 * @param smsDto smsDto
	 * @return SendSmsResponseBody
	 * @author tangyi
	 * @date 2019/06/22 13:28
	 */
	public SendSmsResponseBody sendSms(SmsDto smsDto) {
		SendSmsRequest sendSmsRequest = new SendSmsRequest().setSignName(smsProperties.getSignName())
				.setTemplateCode(smsProperties.getTemplateCode()).setPhoneNumbers(smsDto.getReceiver())
				.setTemplateParam(smsDto.getContent());
		try {
			SendSmsResponse response = client.sendSms(sendSmsRequest);
			SendSmsResponseBody body = response.getBody();
			log.info("send sms success, mobile: {}, response: {}", smsDto.getReceiver(), JSON.toJSONString(body));
			return body;
		} catch (Exception e) {
			throw new CommonException(e, "send message failed: " + e.getMessage());
		}
	}
}


