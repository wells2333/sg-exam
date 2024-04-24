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

package com.github.tangyi.exam.service.subject;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.api.exam.thread.IExecutorHolder;
import com.github.tangyi.common.utils.ExecutorUtils;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.exam.enums.SubjectType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SubjectServiceFactory {

	private static ApplicationContext CONTEXT;

	private static void initApplicationContext() {
		if (CONTEXT == null) {
			CONTEXT = SpringContextHolder.getApplicationContext();
		}
		Preconditions.checkNotNull(CONTEXT);
	}

	public static SubjectChoicesService getChoicesService() {
		initApplicationContext();
		return CONTEXT.getBean(SubjectChoicesService.class);
	}

	public static SubjectShortAnswerService getShortAnswerService() {
		initApplicationContext();
		return CONTEXT.getBean(SubjectShortAnswerService.class);
	}

	public static SubjectJudgementService getJudgementService() {
		initApplicationContext();
		return CONTEXT.getBean(SubjectJudgementService.class);
	}

	public static SubjectFillBlankService getFillBlankService() {
		initApplicationContext();
		return CONTEXT.getBean(SubjectFillBlankService.class);
	}

	public static SubjectMaterialService getSubjectMaterialService() {
		initApplicationContext();
		return CONTEXT.getBean(SubjectMaterialService.class);
	}

	public static ISubjectService getService(int type) {
		initApplicationContext();
		ISubjectService service = null;
		if (SubjectType.CHOICES.getValue() == type || SubjectType.MULTIPLE_CHOICES.getValue() == type) {
			service = CONTEXT.getBean(SubjectChoicesService.class);
		} else if (SubjectType.SHORT_ANSWER.getValue() == type) {
			service = CONTEXT.getBean(SubjectShortAnswerService.class);
		} else if (SubjectType.JUDGEMENT.getValue() == type) {
			service = CONTEXT.getBean(SubjectJudgementService.class);
		} else if (SubjectType.FILL_BLANK.getValue() == type) {
			service = CONTEXT.getBean(SubjectFillBlankService.class);
		}else if (SubjectType.MATERIAL.getValue() == type) {
			service = CONTEXT.getBean(SubjectMaterialService.class);
		}

		if (service == null) {
			throw new IllegalArgumentException("subject service not found: " + type);
		}

		return service;
	}

	public static Collection<SubjectDto> batchGetSubjects(List<Subjects> subjects) {
		if (CollectionUtils.isEmpty(subjects)) {
			return Collections.emptyList();
		}

		initApplicationContext();
		Map<Integer, List<Subjects>> map = subjects.stream()
				.collect(Collectors.groupingBy(Subjects::getType, Collectors.toList()));
		Map<Long, SubjectDto> result = Maps.newHashMapWithExpectedSize(subjects.size());
		ApplicationContext context = SpringContextHolder.getApplicationContext();
		ListeningExecutorService executor = context.getBean(IExecutorHolder.class).getSubjectExecutor();
		List<ListenableFuture<?>> futures = Lists.newArrayListWithExpectedSize(map.size());
		for (Map.Entry<Integer, List<Subjects>> entry : map.entrySet()) {
			ListenableFuture<?> future = executor.submit(() -> {
				ISubjectService service = getService(entry.getKey());
				List<Long> ids = entry.getValue().stream().map(Subjects::getSubjectId).collect(Collectors.toList());
				List<SubjectDto> list = service.getSubjects(ids);
				if (CollectionUtils.isNotEmpty(list)) {
					for (SubjectDto dto : list) {
						result.put(dto.getId(), dto);
					}
				}
			});
			futures.add(future);
		}
		ExecutorUtils.waitFutures(futures);
		return result.values();
	}
}
