package com.github.tangyi.exam.service.subject;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.common.utils.ExecutorUtils;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.user.service.CommonExecutorService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
@AllArgsConstructor
public class SubjectServiceFactory {

	private final SubjectChoicesService subjectChoicesService;

	private final SubjectShortAnswerService subjectShortAnswerService;

	private final SubjectJudgementService subjectJudgementService;

	private final SubjectSpeechService subjectSpeechService;

	private final SubjectVideoService subjectVideoService;

	private final CommonExecutorService commonExecutorService;

	/**
	 * 根据题目类型返回对应的BaseSubjectService
	 */
	public ISubjectService service(Integer type) {
		if (SubjectTypeEnum.CHOICES.getValue().equals(type) || SubjectTypeEnum.MULTIPLE_CHOICES.getValue()
				.equals(type)) {
			return subjectChoicesService;
		} else if (SubjectTypeEnum.SHORT_ANSWER.getValue().equals(type)) {
			return subjectShortAnswerService;
		} else if (SubjectTypeEnum.JUDGEMENT.getValue().equals(type)) {
			return subjectJudgementService;
		} else if (SubjectTypeEnum.SPEECH.getValue().equals(type)) {
			return subjectSpeechService;
		} else if (SubjectTypeEnum.VIDEO.getValue().equals(type)) {
			return subjectVideoService;
		}
		throw new IllegalArgumentException("subject service not found: " + type);
	}

	/**
	 * 批量查询题目
	 * @param subjects subjects
	 * @return Collection
	 */
	public Collection<SubjectDto> batchGetSubjects(List<Subjects> subjects) {
		if (CollectionUtils.isEmpty(subjects)) {
			return Collections.emptyList();
		}
		Map<Integer, List<Subjects>> map = subjects.stream()
				.collect(Collectors.groupingBy(Subjects::getType, Collectors.toList()));
		Map<Long, SubjectDto> result = Maps.newHashMapWithExpectedSize(subjects.size());
		ListeningExecutorService executor = commonExecutorService.getSubjectExecutor();
		List<ListenableFuture<?>> futures = Lists.newArrayListWithExpectedSize(map.size());
		for (Map.Entry<Integer, List<Subjects>> entry : map.entrySet()) {
			ListenableFuture<?> future = executor.submit(() -> {
				ISubjectService service = service(entry.getKey());
				List<Long> ids = entry.getValue().stream().map(Subjects::getSubjectId).collect(Collectors.toList());
				List<SubjectDto> dtoList = service.getSubjects(ids);
				if (CollectionUtils.isNotEmpty(dtoList)) {
					for (SubjectDto dto : dtoList) {
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
