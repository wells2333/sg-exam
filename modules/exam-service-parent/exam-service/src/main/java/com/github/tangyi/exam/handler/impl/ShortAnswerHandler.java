package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.handler.BaseHandler;
import com.github.tangyi.exam.handler.HandleResult;
import com.github.tangyi.exam.service.SubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 简答题
 * @author tangyi
 * @date 2019/12/8 22:00
 */
@Slf4j
@AllArgsConstructor
@Component
public class ShortAnswerHandler implements BaseHandler {

	private final SubjectService subjectService;

	@Override
	public HandleResult handle(List<Answer> answers) {
		return null;
	}
}
