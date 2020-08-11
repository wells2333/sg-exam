package com.github.tangyi.api.exam.client;

import com.github.tangyi.api.config.CustomFeignConfig;
import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.user.client.UserServiceClientFallbackFactory;
import com.github.tangyi.common.constant.ServiceConstant;
import com.github.tangyi.common.model.ResponseBean;
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
@FeignClient(name = ServiceConstant.EXAM_SERVICE, url = "${sys.examServiceUrl}", configuration = CustomFeignConfig.class, fallbackFactory = ExaminationServiceClientFallbackFactory.class)
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
