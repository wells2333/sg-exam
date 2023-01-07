package com.github.tangyi.user.service;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.github.tangyi.api.user.dto.SmsDto;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.EnvUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Service
public class MobileService {

	private static final int VERIFICATION_CODE_SIZE = Integer.parseInt(EnvUtils.getValue("VERIFICATION_CODE_SIZE", "4"));

	private static final String SMS_TEMPLATE = "{\"code\":\"%s\"}";

	private final RedisTemplate redisTemplate;

	private SmsService smsService;

	@SuppressWarnings("unchecked")
	public R<Boolean> sendVerificationCode(String mobile) {
		String key = CommonConstant.VERIFICATION_CODE_KEY + mobile;
		String code = RandomUtil.randomNumbers(VERIFICATION_CODE_SIZE);
		log.info("generate verification code success: {}, {}", mobile, code);
		redisTemplate.opsForValue().set(key, code, SecurityConstant.DEFAULT_SMS_EXPIRE, TimeUnit.SECONDS);
		SmsDto dto = new SmsDto();
		dto.setReceiver(mobile);
		dto.setContent(String.format(SMS_TEMPLATE, code));
		SendSmsResponseBody body = smsService.sendSms(dto);
		SgPreconditions.checkNull(body, "send verification code response is null");
		boolean isOk = "OK".equals(body.getCode());
		if (isOk) {
			log.info("send verification code success: {}", body.getMessage());
		} else {
			log.error("failed to send verification code, mobile: {}, code: {}, message: {}", mobile, body.getCode(),
					body.getMessage());
		}
		return R.success(isOk);
	}
}
