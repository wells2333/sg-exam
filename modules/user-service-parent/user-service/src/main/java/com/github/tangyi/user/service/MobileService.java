package com.github.tangyi.user.service;

import cn.hutool.core.util.RandomUtil;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.enums.LoginType;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.msc.api.constant.SmsConstant;
import com.github.tangyi.msc.api.dto.SmsDto;
import com.github.tangyi.msc.api.feign.MscServiceClient;
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

    private final UserService userService;

    private final RedisTemplate redisTemplate;

    private final MscServiceClient mscServiceClient;

    /**
     * 发送短信
     *
     * @param mobile     mobile
     * @param tenantCode tenantCode
     * @return ResponseBean
     * @author tangyi
     * @date 2019/07/02 09:36:52
     */
    public ResponseBean<Boolean> sendSms(String mobile, String tenantCode) {
        String key = tenantCode + ":" + CommonConstant.DEFAULT_CODE_KEY + LoginType.SMS.getType() + "@" + mobile;
        // TODO 校验时间
        String code = RandomUtil.randomNumbers(Integer.parseInt(CommonConstant.CODE_SIZE));
        log.debug("手机号生成验证码成功:{},{}", mobile, code);
        redisTemplate.opsForValue().set(key, code, SecurityConstant.DEFAULT_SMS_EXPIRE, TimeUnit.SECONDS);
        // 调用消息中心服务，发送短信验证码
        SmsDto smsDto = new SmsDto();
        smsDto.setReceiver(mobile);
        smsDto.setContent(String.format(SmsConstant.SMS_TEMPLATE, code));
        ResponseBean<?> result = mscServiceClient.sendSms(smsDto);
        if (result == null)
            throw new CommonException("发送短信失败.");
        if (result.getCode() == ResponseBean.FAIL)
            throw new CommonException(result.getMsg());
        log.info("发送验证码结果：{}", result.getData());
        return new ResponseBean<>(Boolean.TRUE, code);
    }
}
