package com.github.tangyi.user.controller;

import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.user.api.dto.DashboardDto;
import com.github.tangyi.exam.api.feign.ExaminationServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台首页数据展示
 *
 * @author tangyi
 * @date 2019-03-01 13:54
 */
@Api("后台首页数据展示")
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private ExaminationServiceClient examinationService;

    /**
     * 获取管控台首页数据
     *
     * @param
     * @return ResponseBean
     * @author tangyi
     * @date 2019/3/1 13:55
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @ApiOperation(value = "后台首页数据展示", notes = "后台首页数据展示")
    @SuppressWarnings("unchecked")
    public ResponseBean<DashboardDto> dashboard() {
        DashboardDto dashboardDto = new DashboardDto();
        // TODO 从redis里获取在线用户数
        dashboardDto.setOnlineUserNumber("790");
        // 查询考试数量
        dashboardDto.setExaminationNumber(examinationService.findExaminationCount().getData() + "");
        return new ResponseBean<>(dashboardDto);
    }
}
