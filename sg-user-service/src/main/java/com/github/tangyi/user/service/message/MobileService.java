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

package com.github.tangyi.user.service.message;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.github.tangyi.api.user.dto.SmsDto;
import com.github.tangyi.api.user.service.IMobileService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TenantHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Service
public class MobileService implements IMobileService {

	private static final int VERIFICATION_CODE_SIZE = Integer.parseInt(
			EnvUtils.getValue("VERIFICATION_CODE_SIZE", "4"));
	private static final String SMS_TEMPLATE = "{\"code\":\"%s\"}";

	private final RedisTemplate redisTemplate;
	private SmsService smsService;

	@SuppressWarnings("unchecked")
	public R<Boolean> sendVerificationCode(String mobile) {
		String key = CommonConstant.VERIFICATION_CODE_KEY + mobile;
		String code = RandomUtil.randomNumbers(VERIFICATION_CODE_SIZE);
		SmsDto dto = new SmsDto();
		dto.setReceiver(mobile);
		dto.setContent(String.format(SMS_TEMPLATE, code));
		dto.setOperator(SysUtil.getUser());
		dto.setTenantCode(TenantHolder.getTenantCode());
		SendSmsResponseBody body = smsService.sendSms(dto);
		SgPreconditions.checkNull(body, "Send verification code response is null");
		boolean isOk = "OK".equals(body.getCode());
		if (isOk) {
			redisTemplate.opsForValue().set(key, code, SecurityConstant.DEFAULT_SMS_EXPIRE, TimeUnit.SECONDS);
			log.info("Verification code has been sent successfully, mobile: {}, code: {}", mobile, code);
		} else {
			log.error("Failed to send verification code, mobile: {}, code: {}, message: {}", mobile, body.getCode(),
					body.getMessage());
		}
		return R.success(isOk);
	}
}
