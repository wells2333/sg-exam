package com.github.tangyi.exam.service.answer;

import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.handler.AnswerHandleResult;
import com.github.tangyi.exam.handler.AnswerHandlerFactory;
import com.github.tangyi.exam.handler.IAnswerHandler;
import com.github.tangyi.exam.utils.ExamUtil;
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

	public AnswerHandleResult handleAll(Map<Integer, List<Answer>> map) {
		List<AnswerHandleResult> results = Lists.newArrayListWithExpectedSize(map.size());
		for (Map.Entry<Integer, List<Answer>> entry : map.entrySet()) {
			IAnswerHandler handler = handlerFactory.getHandler(entry.getKey());
			AnswerHandleResult handleResult = handler.handle(entry.getValue());
			if (handleResult != null) {
				results.add(handleResult);
			}
		}
		return ExamUtil.addAll(results);
	}
}
