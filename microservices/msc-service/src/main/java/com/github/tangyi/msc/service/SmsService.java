package com.github.tangyi.msc.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.github.tangyi.api.msc.dto.SmsDto;
import com.github.tangyi.api.msc.model.SmsResponse;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.utils.JsonMapper;
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
     * @return SmsResponse
     * @author tangyi
     * @date 2019/06/22 13:28
     */
    public SmsResponse sendSms(SmsDto smsDto) {
        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAppKey(), smsProperties.getAppSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(smsProperties.getDomain());
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", smsDto.getReceiver());
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());
        request.putQueryParameter("TemplateParam", smsDto.getContent());
        request.setVersion(smsProperties.getVersion());
        request.setAction(smsProperties.getAction());
        try {
        	// TODO 测试
            if (true) {
                SmsResponse s = new SmsResponse();
                s.setCode(smsDto.getContent());
                return s;
            }
            CommonResponse response = client.getCommonResponse(request);
            log.info("response: {}", response.getData());
            if (response.getHttpStatus() != 200)
                throw new CommonException(response.getData());
            SmsResponse smsResponse = JsonMapper.getInstance().fromJson(response.getData(), SmsResponse.class);
            if (smsResponse == null)
                throw new CommonException("Parse response error");
            if (!"OK".equals(smsResponse.getCode()))
                throw new CommonException(smsResponse.getMessage());
            return smsResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("Send message failed: " + e.getMessage());
        }
    }
}


