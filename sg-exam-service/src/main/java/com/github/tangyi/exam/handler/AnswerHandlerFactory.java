package com.github.tangyi.exam.handler;

import com.github.tangyi.exam.enums.SubjectType;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AnswerHandlerFactory {

	private final ApplicationContext applicationContext;

	public IAnswerHandler getHandler(int type) {
		return applicationContext.getBean(SubjectType.matchByValue(type).getHandler());
	}
}
