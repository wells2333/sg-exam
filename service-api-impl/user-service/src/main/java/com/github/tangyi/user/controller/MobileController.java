package com.github.tangyi.user.controller;

import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.user.service.MobileService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 手机管理Controller
 *
 * @author tangyi
 * @date 2019/07/02 09:34
 */
@Slf4j
@AllArgsConstructor
@Api("手机管理")
@RestController
@RequestMapping("/v1/mobile")
public class MobileController extends BaseController {

    private final MobileService mobileService;

    /**
     * 发送短信
     *
     * @param mobile     mobile
     * @param tenantCode tenantCode
     * @return ResponseBean
     * @author tangyi
     * @date 2019/07/02 09:49:05
     */
    @GetMapping("sendSms/{mobile}")
    public ResponseBean<Boolean> sendSms(@PathVariable String mobile, @RequestHeader(SecurityConstant.TENANT_CODE_HEADER) String tenantCode) {
        return mobileService.sendSms(mobile, tenantCode);
    }
}
