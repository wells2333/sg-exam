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
public class ValidateCodeService {

	private final RedisTemplate redisTemplate;

	@SuppressWarnings({"unchecked"})
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
			throw new InvalidValidateCodeException("验证码错误");
		}
		redisTemplate.delete(key);
	}
}
