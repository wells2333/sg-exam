package com.github.tangyi.user.service;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.github.tangyi.api.user.constant.SmsConstant;
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

/**
 * 手机管理Service
 *
 * @author tangyi
 * @date 2019/07/02 09:35
 */
@Slf4j
@AllArgsConstructor
@Service
public class MobileService {

	public static final int VALIDATE_CODE_SIZE = Integer.parseInt(EnvUtils.getValue("VALIDATE_CODE_SIZE", "4"));

	private final RedisTemplate redisTemplate;

	private SmsService smsService;

	/**
	 * 发送短信
	 *
	 * @param mobile     mobile
	 * @return R
	 * @author tangyi
	 * @date 2019/07/02 09:36:52
	 */
	@SuppressWarnings("unchecked")
	public R<Boolean> sendSms(String mobile) {
		String key = CommonConstant.DEFAULT_CODE_KEY + mobile;
		String code = RandomUtil.randomNumbers(VALIDATE_CODE_SIZE);
		log.debug("generate validate code success: {}, {}", mobile, code);
		redisTemplate.opsForValue().set(key, code, SecurityConstant.DEFAULT_SMS_EXPIRE, TimeUnit.SECONDS);
		// 发送短信验证码
		SmsDto smsDto = new SmsDto();
		smsDto.setReceiver(mobile);
		smsDto.setContent(String.format(SmsConstant.SMS_TEMPLATE, code));
		SendSmsResponseBody body = smsService.sendSms(smsDto);
		SgPreconditions.checkNull(body, "send sms response body is null");
		if (!"OK".equals(body.getCode())) {
			log.error("send sms failed, mobile: {}, code: {}, message: {}", mobile, body.getCode(), body.getMessage());
		} else {
			log.info("send validate success: {}", body.getMessage());
		}
		return R.success(Boolean.TRUE);
	}
}
