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

package com.github.tangyi.user.service;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.exceptions.InvalidValidateCodeException;
import com.github.tangyi.common.exceptions.ValidateCodeExpiredException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@SuppressWarnings({"unchecked"})
public class ValidateCodeService {

	private final RedisTemplate redisTemplate;

	public void checkCode(String code, String randomStr) {
		String key = CommonConstant.VERIFICATION_CODE_KEY + randomStr;
		// 验证码过期
		if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		}

		Object codeObj = redisTemplate.opsForValue().get(key);
		if (codeObj == null) {
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		}

		String saveCode = codeObj.toString();
		if (StrUtil.isBlank(saveCode)) {
			redisTemplate.delete(key);
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		}

		if (!StrUtil.equals(saveCode, code)) {
			redisTemplate.delete(key);
			throw new InvalidValidateCodeException("Invalid code.");
		}

		redisTemplate.delete(key);
	}
}
