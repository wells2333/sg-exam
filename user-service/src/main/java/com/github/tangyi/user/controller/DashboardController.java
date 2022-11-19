package com.github.tangyi.user.controller;

import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.user.dto.DashboardDto;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.common.base.BaseController;
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

/**
 * 后台首页数据展示
 *
 * @author tangyi
 * @date 2019-03-01 13:54
 */
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
	 *
	 * @return R
	 * @author tangyi
	 * @date 2019/3/1 13:55
	 */
	@GetMapping
	@Operation(summary = "后台首页数据展示", description = "后台首页数据展示")
	public R<DashboardDto> dashboard() {
		String tenantCode = SysUtil.getTenantCode();
		DashboardDto dashboardDto = new DashboardDto();
		// 查询用户数量
		UserVo userVo = new UserVo();
		userVo.setTenantCode(tenantCode);
		dashboardDto.setOnlineUserNumber(userService.userCount(userVo).toString());
		// 租户数量
		dashboardDto.setTenantCount(tenantService.tenantCount().toString());
		// 查询考试数量
		ExaminationDashboardDto examinationDashboardDto = examRecordService.findExamDashboardData(tenantCode);
		if (examinationDashboardDto != null) {
			if (examinationDashboardDto.getExaminationCount() != null) {
				dashboardDto.setExaminationNumber(examinationDashboardDto.getExaminationCount().toString());
			}
			if (examinationDashboardDto.getExamUserCount() != null) {
				dashboardDto.setExamUserNumber(examinationDashboardDto.getExamUserCount().toString());
			}
			if (examinationDashboardDto.getExaminationRecordCount() != null) {
				dashboardDto.setExaminationRecordNumber(examinationDashboardDto.getExaminationRecordCount().toString());
			}
		}
		return R.success(dashboardDto);
	}

	/**
	 * 过去一周考试记录数
	 * @return R
	 * @author tangyi
	 * @date 2020/1/31 6:08 下午
	 */
	@GetMapping("examRecordTendency")
	@Operation(summary = "过去一周考试记录数", description = "过去一周考试记录数")
	public R<DashboardDto> examRecordTendency(@RequestParam Integer pastDays) {
		DashboardDto dashboardDto = new DashboardDto();
		ExaminationDashboardDto tendency = examRecordService.findExamRecordTendency(SysUtil.getTenantCode(), pastDays);
		dashboardDto.setExamRecordDate(tendency.getExamRecordDate());
		dashboardDto.setExamRecordData(tendency.getExamRecordData());
		return R.success(dashboardDto);
	}
}
