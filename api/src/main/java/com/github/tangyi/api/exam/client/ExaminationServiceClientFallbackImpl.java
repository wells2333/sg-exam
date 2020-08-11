package com.github.tangyi.api.exam.client;

import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.common.model.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考试服务熔断
 *
 * @author tangyi
 * @date 2019-03-01 15:22
 */
@Slf4j
@Service
public class ExaminationServiceClientFallbackImpl implements ExaminationServiceClient {

	private Throwable throwable;

	@Override
	public ResponseBean<ExaminationDashboardDto> findExaminationDashboardData(String tenantCode) {
		log.error("Call findExaminationDashboardData circuit breaker, {}", tenantCode, throwable);
		return new ResponseBean<>(new ExaminationDashboardDto());
	}

	@Override
	public ResponseBean<ExaminationDashboardDto> findExamRecordTendencyData(String tenantCode, Integer pastDays) {
		log.error("Call findExamRecordTendencyData circuit breaker, {}, {}", tenantCode, pastDays, throwable);
		return new ResponseBean<>(new ExaminationDashboardDto());
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
}
