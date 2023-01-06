package com.github.tangyi.exam.service.answer;

import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.handler.AnswerHandleResult;
import com.github.tangyi.exam.handler.AnswerHandlerFactory;
import com.github.tangyi.exam.handler.IAnswerHandler;
import com.github.tangyi.exam.utils.AnswerHandlerUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class AnswerHandleService {

	private final AnswerHandlerFactory handlerFactory;

	/**
	 * 自动判分
	 */
	public AnswerHandleResult handleAll(Map<Integer, List<Answer>> distinctAnswer) {
		List<AnswerHandleResult> results = Lists.newArrayListWithExpectedSize(distinctAnswer.size());
		for (Map.Entry<Integer, List<Answer>> entry : distinctAnswer.entrySet()) {
			IAnswerHandler handler = handlerFactory.getHandler(entry.getKey());
			AnswerHandleResult handleResult = handler.handle(entry.getValue());
			if (handleResult != null) {
				results.add(handleResult);
			}
		}
		return AnswerHandlerUtil.addAll(results);
	}
}
