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

	private final RedisCounterService redisCounterService;

	public Long viewSubject(Long subjectId) {
		return redisCounterService.incrCount(SUBJECT_VIEW_KEY, subjectId);
	}

	public void getSubjectsView(List<SubjectDto> list) {
		List<Long> ids = list.stream().map(SubjectDto::getId).collect(Collectors.toList());
		Map<Long, Long> map = redisCounterService.getCounts(SUBJECT_VIEW_KEY, ids);
		for (SubjectDto dto : list) {
			Long cnt = map.get(dto.getId());
			if (cnt == null) {
				cnt = 0L;
			}
			dto.setViews(cnt > 10000 ? "10000+" : String.valueOf(cnt));
		}
	}
}
