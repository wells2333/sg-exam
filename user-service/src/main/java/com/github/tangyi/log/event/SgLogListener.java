package com.github.tangyi.log.event;

import com.github.tangyi.common.model.Log;
import com.github.tangyi.user.service.LogService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步监听日志事件
 *
 * @author tangyi
 * @date 2019/3/12 23:59
 */
@Configuration
public class SgLogListener {

	private final LogService logService;

	public SgLogListener(LogService logService) {
		this.logService = logService;
	}

	@Async
	@Order
	@EventListener(SgLogEvent.class)
	public void saveSysLog(SgLogEvent event) {
		Log log = (Log) event.getSource();
		logService.save(log);
	}
}
