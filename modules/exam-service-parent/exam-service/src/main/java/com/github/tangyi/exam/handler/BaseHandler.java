package com.github.tangyi.exam.handler;

import com.github.tangyi.exam.api.module.Answer;

import java.util.List;

/**
 * 统计成绩
 * @author tangyi
 * @date 2019/12/8 9:56 下午
 */
@FunctionalInterface
public interface BaseHandler {

	HandleResult handle(List<Answer> answers);
}
