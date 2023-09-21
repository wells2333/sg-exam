package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.RankInfoDto;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.vo.UserVo;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class RankInfoService {

	private final RedisTemplate<String, String> redisTemplate;

	private final IUserService userService;

	private String getRankKey(Long examinationId) {
		return AnswerConstant.CACHE_PREFIX_RANK + examinationId;
	}

	/**
	 * 获取排名数据
	 */
	public List<RankInfoDto> getRankInfo(Long examinationId) {
		String key = this.getRankKey(examinationId);
		Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
				.reverseRangeByScoreWithScores(key, 0, Integer.MAX_VALUE);
		if (tuples == null) {
			return Collections.emptyList();
		}

		int rankNum = 1;
		List<RankInfoDto> rankInfos = Lists.newArrayListWithExpectedSize(tuples.size());
		for (ZSetOperations.TypedTuple<String> tuple : tuples) {
			if (tuple != null && tuple.getValue() != null) {
				Long userId = Long.valueOf(tuple.getValue());
				RankInfoDto rankInfo = new RankInfoDto();
				rankInfo.setUserId(userId);
				rankInfo.setScore(tuple.getScore());
				rankInfo.setRankNum(rankNum++);
				rankInfos.add(rankInfo);
				UserVo userVo = userService.getUserInfo(userId);
				if (userVo != null) {
					rankInfo.setName(userVo.getName());
					rankInfo.setAvatarUrl(userVo.getAvatarUrl());
				}
			}
		}
		return rankInfos;
	}

	/**
	 * 更新排名信息
	 */
	public void updateRank(ExaminationRecord record) {
		try {
			String key = this.getRankKey(record.getExaminationId());
			String value = record.getUserId() + "";
			redisTemplate.opsForZSet().add(key, value, record.getScore());
			log.info("Update rank finished, examinationId: {}, userId: {}, score: {}", record.getExaminationId(), value,
					record.getScore());
		} catch (Exception e) {
			log.error("Failed to update rank, examinationId: {}", record.getExaminationId(), e);
		}
	}
}
