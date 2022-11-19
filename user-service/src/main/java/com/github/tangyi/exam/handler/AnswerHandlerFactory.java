package com.github.tangyi.exam.handler;

import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * @author tangyi
 * @date 2022/4/11 8:52 下午
 */
@Component
@AllArgsConstructor
public class AnswerHandlerFactory {

	public IAnswerHandler getHandler(int type) {
		SubjectTypeEnum subjectTypeEnum = SubjectTypeEnum.matchByValue(type);
		if (subjectTypeEnum == null) {
			throw new IllegalArgumentException("handler type not found: " + type);
		}
		return SpringContextHolder.getApplicationContext().getBean(subjectTypeEnum.getHandler());
	}
}
