package com.github.tangyi.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 自定义埋点上报
 */
@Component
public class PrometheusComponent implements ApplicationContextAware {

	private static PrometheusComponent instance;

	/**
	 * 请求总数
	 */
	private Counter reqCounter;

	/**
	 * 正在请求的http数量
	 */
	private Gauge reqGauge;

	/**
	 * 请请求耗时分布
	 */
	private Histogram reqLatencyHistogram;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		instance = this;
		CollectorRegistry registry = applicationContext.getBean(CollectorRegistry.class);
		reqCounter = Counter.build().name("rest_req_total").labelNames("path", "method", "code").help("总请求数")
				.register(registry);
		reqGauge = Gauge.build().name("rest_in_progress_req").labelNames("path", "method").help("正在处理的请求数")
				.register(registry);
		reqLatencyHistogram = Histogram.build().labelNames("path", "method", "code")
				.name("rest_req_latency_seconds_histogram").help("请求耗时分布").register(registry);
	}

	public static PrometheusComponent getInstance() {
		return instance;
	}

	public Counter counter() {
		return reqCounter;
	}

	public Gauge gauge() {
		return reqGauge;
	}

	public Histogram histogram() {
		return reqLatencyHistogram;
	}
}
