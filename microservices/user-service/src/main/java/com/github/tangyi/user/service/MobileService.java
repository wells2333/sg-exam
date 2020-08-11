package com.github.tangyi.user.service;

import cn.hutool.core.util.RandomUtil;
import com.github.tangyi.api.msc.constant.SmsConstant;
import com.github.tangyi.api.msc.dto.SmsDto;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.exceptions.ServiceException;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.ResponseUtil;
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

    private final RedisTemplate redisTemplate;

    /**
     * 发送短信
     *
     * @param mobile     mobile
     * @return ResponseBean
     * @author tangyi
     * @date 2019/07/02 09:36:52
     */
    public ResponseBean<Boolean> sendSms(String mobile) {
        String key = CommonConstant.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + "@" + mobile;
        // TODO 校验时间
        String code = RandomUtil.randomNumbers(Integer.parseInt(CommonConstant.CODE_SIZE));
        log.debug("Generate validate code success: {}, {}", mobile, code);
        redisTemplate.opsForValue().set(key, code, SecurityConstant.DEFAULT_SMS_EXPIRE, TimeUnit.SECONDS);
        // 调用消息中心服务，发送短信验证码
        SmsDto smsDto = new SmsDto();
        smsDto.setReceiver(mobile);
        smsDto.setContent(String.format(SmsConstant.SMS_TEMPLATE, code));
        ResponseBean<?> result = null;
        if (!ResponseUtil.isSuccess(result))
            throw new ServiceException("Send validate code error: " + result.getMsg());
        log.info("Send validate result: {}", result.getData());
        return new ResponseBean<>(Boolean.TRUE, code);
    }
}
