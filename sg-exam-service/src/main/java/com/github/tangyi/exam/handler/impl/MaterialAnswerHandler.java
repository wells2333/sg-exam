/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MaterialAnswerHandler extends AbstractAnswerHandler {

	public MaterialAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	@Override
	public boolean judgeRight(JudgeContext judgeContext) {
		return eq(judgeContext);
	}

	@Override
	public void judge(HandleContext handleContext, JudgeContext judgeContext) {
		judgeContext.done();
	}
}
