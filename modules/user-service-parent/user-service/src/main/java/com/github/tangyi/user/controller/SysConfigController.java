package com.github.tangyi.user.controller;

import com.github.tangyi.common.basic.dto.SysConfigDto;
import com.github.tangyi.common.basic.properties.SysProperties;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统配置controller
 *
 * @author tangyi
 * @date 2019-02-28 17:29
 */
@AllArgsConstructor
@Api("系统配置信息管理")
@RestController
@RequestMapping("/v1/sysConfig")
public class SysConfigController extends BaseController {

    private final SysProperties sysProperties;

    /**
     * 获取系统配置
     *
     * @return ResponseBean
     * @author tangyi
     * @date 2019/2/28 17:31
     */
    @GetMapping
    @ApiOperation(value = "获取系统配置", notes = "获取系统配置")
    public ResponseBean<SysConfigDto> getSysConfig() {
        SysConfigDto sysConfigDto = new SysConfigDto();
        BeanUtils.copyProperties(sysProperties, sysConfigDto);
        return new ResponseBean<>(sysConfigDto);
    }
}
