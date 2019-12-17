package com.github.tangyi.user.controller;

import com.github.tangyi.common.core.exceptions.ServiceException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.ResponseUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.exam.api.feign.ExaminationServiceClient;
import com.github.tangyi.user.api.dto.DashboardDto;
import com.github.tangyi.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台首页数据展示
 *
 * @author tangyi
 * @date 2019-03-01 13:54
 */
@AllArgsConstructor
@Api("后台首页数据展示")
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController extends BaseController {

    private final ExaminationServiceClient examinationService;

    private final UserService userService;

    /**
     * 获取管控台首页数据
     *
     * @return ResponseBean
     * @author tangyi
     * @date 2019/3/1 13:55
     */
    @GetMapping
    @ApiOperation(value = "后台首页数据展示", notes = "后台首页数据展示")
    public ResponseBean<DashboardDto> dashboard() {
        String tenantCode = SysUtil.getTenantCode();
        DashboardDto dashboardDto = new DashboardDto();
        // 查询用户数量
        UserVo userVo = new UserVo();
        userVo.setTenantCode(tenantCode);
        dashboardDto.setOnlineUserNumber(userService.userCount(userVo).toString());
        // 查询考试数量
        ResponseBean<Integer> examinationCountResponseBean = examinationService.findExaminationCount(tenantCode);
        if (!ResponseUtil.isSuccess(examinationCountResponseBean))
            throw new ServiceException("Get examination count failed: " + examinationCountResponseBean.getMsg());
        dashboardDto.setExaminationNumber(examinationCountResponseBean.getData().toString());
        // 查询参与人数
        ResponseBean<Integer> examUserCountResponseBean = examinationService.findExamUserCount(tenantCode);
        if (!ResponseUtil.isSuccess(examUserCountResponseBean))
            throw new ServiceException("Get user count failed");
        dashboardDto.setExamUserNumber(examUserCountResponseBean.getData().toString());
        return new ResponseBean<>(dashboardDto);
    }
}
