package com.github.tangyi.exam.api.feign;

import com.github.tangyi.common.core.constant.ServiceConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.feign.config.CustomFeignConfig;
import com.github.tangyi.exam.api.dto.ExaminationDashboardDto;
import com.github.tangyi.exam.api.feign.fallback.ExaminationServiceClientFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 考试接口
 * <p>
 * FeignClient需要指定configuration为CustomFeignConfig，用于服务间调用携带token
 *
 * @author tangyi
 * @date 2019-03-01 15:21
 */
@FeignClient(name = ServiceConstant.EXAM_SERVICE, configuration = CustomFeignConfig.class, fallback = ExaminationServiceClientFallbackImpl.class)
public interface ExaminationServiceClient {

	/**
	 * 查询考试监控数据
	 *
	 * @param tenantCode 租户标识
	 * @return ResponseBean
	 */
	@GetMapping("/v1/examRecord/dashboard")
	ResponseBean<ExaminationDashboardDto> findExaminationDashboardData(@RequestParam("tenantCode") String tenantCode);

	/**
	 * 查询考试记录监控数据
	 *
	 * @param tenantCode 租户标识
	 * @param pastDays pastDays
	 * @return ResponseBean
	 */
	@GetMapping("/v1/examRecord/dashboard/examRecordTendency")
	ResponseBean<ExaminationDashboardDto> findExamRecordTendencyData(@RequestParam("tenantCode") String tenantCode, @RequestParam("pastDays") Integer pastDays);

}
