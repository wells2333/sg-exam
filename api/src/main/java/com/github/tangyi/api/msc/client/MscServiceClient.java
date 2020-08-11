package com.github.tangyi.api.msc.client;

import com.github.tangyi.api.msc.dto.SmsDto;
import com.github.tangyi.common.model.ResponseBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 消息中心服务
 *
 * @author tangyi
 * @date 2019/07/02 16:04
 */
public interface MscServiceClient {

    /**
     * 发送短信
     *
     * @param smsDto smsDto
     * @return ResponseBean
     * @author tangyi
     * @date 2019/07/02 16:07:27
     */
    @PostMapping("/v1/sms/sendSms")
    ResponseBean<?> sendSms(@RequestBody SmsDto smsDto);
}
