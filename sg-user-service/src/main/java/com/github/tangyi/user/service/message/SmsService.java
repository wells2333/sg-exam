package com.github.tangyi.user.service.message;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.github.tangyi.api.user.dto.SmsDto;
import com.github.tangyi.api.user.model.SysSms;
import com.github.tangyi.api.user.service.ISysSmsService;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.user.mapper.massage.SysSmsMapper;
import com.github.tangyi.user.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService extends CrudService<SysSmsMapper, SysSms> implements ISysSmsService {

	private static final Boolean SEND_SMS = Boolean.parseBoolean(EnvUtils.getValue("SEND_SMS", "true"));

	private final SmsProperties smsProperties;

	private final com.aliyun.dysmsapi20170525.Client client;

	public SmsService(SmsProperties smsProperties) throws Exception {
		this.smsProperties = smsProperties;
		Config config = new Config().setAccessKeyId(smsProperties.getAppKey())
				.setAccessKeySecret(smsProperties.getAppSecret());
		config.endpoint = "dysmsapi.aliyuncs.com";
		this.client = new com.aliyun.dysmsapi20170525.Client(config);
	}

	public SendSmsResponseBody sendSms(SmsDto smsDto) {
		SendSmsRequest sendSmsRequest = new SendSmsRequest().setSignName(smsProperties.getSignName())
				.setTemplateCode(smsProperties.getTemplateCode()).setPhoneNumbers(smsDto.getReceiver())
				.setTemplateParam(smsDto.getContent());
		try {
			SendSmsResponse response;
			if (SEND_SMS) {
				response = client.sendSms(sendSmsRequest);
			} else {
				response = new SendSmsResponse();
				SendSmsResponseBody body = new SendSmsResponseBody();
				body.setCode("OK");
				response.setBody(body);
			}
			SendSmsResponseBody body = response.getBody();
			String responseStr = JSON.toJSONString(body);
			SysSms sms = new SysSms();
			sms.setReceiver(smsDto.getReceiver());
			sms.setContent(smsDto.getContent());
			sms.setResponse(responseStr);
			log.info("Sms has been sent successfully, mobile: {}, response: {}", smsDto.getReceiver(), responseStr);
			return body;
		} catch (Exception e) {
			throw new CommonException(e, "Failed to send sms");
		}
	}

	public int insertSms(SmsDto smsDto, String responseStr) {
		SysSms sms = new SysSms();
		sms.setCommonValue(smsDto.getOperator(), smsDto.getTenantCode());
		sms.setReceiver(smsDto.getReceiver());
		sms.setContent(smsDto.getContent());
		sms.setResponse(responseStr);
		return this.insert(sms);
	}
}


