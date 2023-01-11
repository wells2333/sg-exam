package com.github.tangyi.user.controller;

import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.user.dto.DashboardDto;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.user.service.TenantService;
import com.github.tangyi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Tag(name = "后台首页数据展示")
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController extends BaseController {

	private final UserService userService;

	private final TenantService tenantService;

	private final ExamRecordService examRecordService;

	/**
	 * 获取管控台首页数据
	 */
	@GetMapping
	@Operation(summary = "后台首页数据展示", description = "后台首页数据展示")
	public R<DashboardDto> dashboard() {
		String tenantCode = SysUtil.getTenantCode();
		DashboardDto dto = new DashboardDto();
		// 查询用户数量
		UserVo userVo = new UserVo();
		userVo.setTenantCode(tenantCode);
		dto.setOnlineUserNumber(userService.userCount(userVo).toString());
		// 租户数量
		dto.setTenantCount(tenantService.tenantCount().toString());
		// 查询考试数量
		ExaminationDashboardDto dashboardDto = examRecordService.findExamDashboardData(tenantCode);
		if (dashboardDto != null) {
			if (dashboardDto.getExaminationCount() != null) {
				dto.setExaminationNumber(dashboardDto.getExaminationCount().toString());
			}
			if (dashboardDto.getExamUserCount() != null) {
				dto.setExamUserNumber(dashboardDto.getExamUserCount().toString());
			}
			if (dashboardDto.getExaminationRecordCount() != null) {
				dto.setExaminationRecordNumber(dashboardDto.getExaminationRecordCount().toString());
			}
		}
		return R.success(dto);
	}

	/**
	 * 过去一周考试记录数
	 */
	@GetMapping("examRecordTendency")
	@Operation(summary = "过去一周考试记录数", description = "过去一周考试记录数")
	public R<DashboardDto> examRecordTendency(@RequestParam Integer pastDays) {
		DashboardDto dto = new DashboardDto();
		ExaminationDashboardDto tendency = examRecordService.findExamRecordTendency(SysUtil.getTenantCode(), pastDays);
		dto.setExamRecordDate(tendency.getExamRecordDate());
		dto.setExamRecordData(tendency.getExamRecordData());
		return R.success(dto);
	}
}
