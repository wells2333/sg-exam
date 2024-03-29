package com.github.tangyi.common.utils;

import com.github.tangyi.common.base.SgPreconditions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	public static ApplicationContext getApplicationContext() {
		SgPreconditions.checkNull(applicationContext, "The applicationContext is null.");
		return applicationContext;
	}

	@Override
	public void destroy() {
		applicationContext = null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}

	public static void publishEvent(ApplicationEvent event) {
		if (applicationContext == null) {
			return;
		}

		applicationContext.publishEvent(event);
	}
}
