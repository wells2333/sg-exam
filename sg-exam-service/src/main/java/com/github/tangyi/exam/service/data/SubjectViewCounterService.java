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

package com.github.tangyi.exam.service.data;

import com.github.tangyi.api.exam.dto.SubjectDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SubjectViewCounterService {

	private static final String SUBJECT_VIEW_KEY = "subject:view:";

	private final RedisCounterService counterService;

	public Long viewSubject(Long subjectId) {
		return counterService.incrCount(SUBJECT_VIEW_KEY, subjectId);
	}

	public void getSubjectsView(List<SubjectDto> list) {
		List<Long> ids = list.stream().map(SubjectDto::getId).collect(Collectors.toList());
		Map<Long, Long> map = counterService.getCounts(SUBJECT_VIEW_KEY, ids);
		for (SubjectDto dto : list) {
			Long cnt = map.get(dto.getId());
			if (cnt == null) {
				cnt = 0L;
			}
			dto.setViews(cnt > 10000 ? "10000+" : String.valueOf(cnt));
		}
	}
}
