package com.github.tangyi.user.controller;

import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.user.dto.DashboardDto;
import com.github.tangyi.common.exceptions.ServiceException;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.ResponseUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.user.service.TenantService;
import com.github.tangyi.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private final UserService userService;

    private final TenantService tenantService;

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
        // 租户数量
		dashboardDto.setTenantCount(tenantService.tenantCount().toString());
        // 查询考试数量
        ResponseBean<ExaminationDashboardDto> dashboardData = null;
        if (!ResponseUtil.isSuccess(dashboardData))
            throw new ServiceException("Get examination dashboard data failed: " + dashboardData.getMsg());
        ExaminationDashboardDto examinationDashboardDto = dashboardData.getData();
        if (examinationDashboardDto != null) {
            if (examinationDashboardDto.getExaminationCount() != null)
                dashboardDto.setExaminationNumber(examinationDashboardDto.getExaminationCount().toString());
            if (examinationDashboardDto.getExamUserCount() != null)
             dashboardDto.setExamUserNumber(examinationDashboardDto.getExamUserCount().toString());
            if (examinationDashboardDto.getExaminationRecordCount() != null)
                dashboardDto.setExaminationRecordNumber(examinationDashboardDto.getExaminationRecordCount().toString());
        }
        return new ResponseBean<>(dashboardDto);
    }

    /**
     * 过去一周考试记录数
     * @return ResponseBean
     * @author tangyi
     * @date 2020/1/31 6:08 下午
     */
    @GetMapping("examRecordTendency")
	@ApiOperation(value = "过去一周考试记录数", notes = "过去一周考试记录数")
	public ResponseBean<DashboardDto> examRecordTendency(@RequestParam Integer pastDays) {
		DashboardDto dashboardDto = new DashboardDto();
		ResponseBean<ExaminationDashboardDto> examRecordTendencyData = null;
		if (!ResponseUtil.isSuccess(examRecordTendencyData))
			throw new ServiceException("Get examination record tendency data failed: " + examRecordTendencyData.getMsg());
		dashboardDto.setExamRecordDate(examRecordTendencyData.getData().getExamRecordDate());
		dashboardDto.setExamRecordData(examRecordTendencyData.getData().getExamRecordData());
		return new ResponseBean<>(dashboardDto);
	}
}
