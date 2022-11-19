package com.github.tangyi.exam.service.data;

import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author tangyi
 * @date 2022/11/12 10:49 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamStartCountService {

	private final ExaminationMapper examinationMapper;

	private final RedisCounterService redisCounterService;

	/**
	 * 查询考试的考试次数
	 * @param dtoList dtoList
	 */
	public void findExamStartCount(List<ExaminationDto> dtoList) {
		List<Long> ids = dtoList.stream().map(ExaminationDto::getId).collect(Collectors.toList());
		Map<Long, Long> map = redisCounterService.getCounts(ExamCacheName.EXAMINATION_START_COUNT, ids);
		if (MapUtils.isEmpty(map)) {
			return;
		}
		for (ExaminationDto dto : dtoList) {
			Long startCount = map.get(dto.getId());
			if (startCount != null) {
				dto.setStartCount(startCount);
			}
		}
	}

	@Transactional
	public void updateStartCount(List<Long> ids) {
		Map<Long, Long> countMap = redisCounterService.getCounts(ExamCacheName.EXAMINATION_START_COUNT, ids);
		if (MapUtils.isEmpty(countMap)) {
			return;
		}
		List<Examination> updates = Lists.newArrayList();
		for (Map.Entry<Long, Long> entry : countMap.entrySet()) {
			Examination examination = examinationMapper.selectByPrimaryKey(entry.getKey());
			if (examination != null && !entry.getValue().equals(examination.getStartCount())) {
				examination.setStartCount(entry.getValue());
				updates.add(examination);
			}
		}
		if (CollectionUtils.isNotEmpty(updates)) {
			examinationMapper.bathUpdateStartCountAndFavorite(updates);
		}
		log.info("update start count success, size: {}", updates.size());
	}
}
