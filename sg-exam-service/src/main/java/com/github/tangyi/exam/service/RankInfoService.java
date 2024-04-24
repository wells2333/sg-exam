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
		List<RankInfoDto> result = Lists.newArrayListWithExpectedSize(tuples.size());
		for (ZSetOperations.TypedTuple<String> tuple : tuples) {
			if (tuple != null && tuple.getValue() != null) {
				Long uid = Long.valueOf(tuple.getValue());
				RankInfoDto info = new RankInfoDto();
				info.setUserId(uid);
				info.setScore(tuple.getScore());
				info.setRankNum(rankNum++);
				result.add(info);
				UserVo vo = userService.getUserInfo(uid);
				if (vo != null) {
					info.setName(vo.getName());
					info.setAvatarUrl(vo.getAvatarUrl());
				} else {
					info.setName(String.valueOf(uid));
				}
			}
		}
		return result;
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
