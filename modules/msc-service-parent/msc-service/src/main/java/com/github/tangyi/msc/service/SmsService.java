package com.github.tangyi.msc.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.github.tangyi.msc.api.dto.SmsDto;
import com.github.tangyi.msc.properties.SmsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tangyi
 * @date 2019/6/22 13:23
 */
@Slf4j
@AllArgsConstructor
@Service
public class SmsService {

    private final SmsProperties smsProperties;

    /**
     * 发送短信
     *
     * @param smsDto smsDto
     * @return String
     * @author tangyi
     * @date 2019/06/22 13:28
     */
    public String sendSms(SmsDto smsDto) {
        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAppKey(), smsProperties.getAppSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(smsProperties.getDomain());
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", smsDto.getReceiver());
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("发送结果：{}", response.getData());
            return response.getData();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
