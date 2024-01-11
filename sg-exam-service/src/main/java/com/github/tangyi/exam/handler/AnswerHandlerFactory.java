package com.github.tangyi.exam.handler;

import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.exam.enums.SubjectType;
import org.springframework.context.ApplicationContext;

public class AnswerHandlerFactory {

	private static ApplicationContext CONTEXT;

	public static IAnswerHandler getHandler(int type) {
		if (CONTEXT == null) {
			CONTEXT = SpringContextHolder.getApplicationContext();
		}
		return CONTEXT.getBean(SubjectType.matchByValue(type).getHandler());
	}
}
