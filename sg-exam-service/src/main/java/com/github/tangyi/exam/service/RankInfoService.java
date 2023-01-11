package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.RankInfoDto;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.JsonMapper;
import com.github.tangyi.common.utils.RUtil;
import com.github.tangyi.common.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class RankInfoService {

	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 获取排名数据
	 */
	public List<RankInfoDto> getRankInfo(Long recordId) {
		List<RankInfoDto> rankInfos = new ArrayList<>();
		// 查询缓存
		Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet()
				.reverseRangeByScoreWithScores(AnswerConstant.CACHE_PREFIX_RANK + recordId, 0, Integer.MAX_VALUE);
		if (typedTuples != null) {
			// 用户ID列表
			Set<Long> userIds = new HashSet<>();
			typedTuples.forEach(typedTuple -> {
				ExaminationRecord record = JsonMapper.getInstance()
						.fromJson(typedTuple.getValue(), ExaminationRecord.class);
				if (record != null) {
					RankInfoDto rankInfo = new RankInfoDto();
					rankInfo.setUserId(record.getUserId());
					userIds.add(record.getUserId());
					rankInfo.setScore(typedTuple.getScore());
					rankInfos.add(rankInfo);
				}
			});
			if (!userIds.isEmpty()) {
				R<List<UserVo>> userResponse = null;
				if (RUtil.isSuccess(userResponse)) {
					rankInfos.forEach(rankInfo -> {
						userResponse.getResult().stream().filter(user -> user.getId().equals(rankInfo.getUserId()))
								.findFirst().ifPresent(user -> {
									// 设置考生信息
									rankInfo.setName(user.getName());
									rankInfo.setAvatarUrl(user.getAvatarUrl());
								});
					});
				}
			}
		}
		return rankInfos;
	}

	/**
	 * 更新排名信息
	 * 基于Redis的sort set数据结构
	 */
	public void updateRank(ExaminationRecord record) {
		redisTemplate.opsForZSet().add(AnswerConstant.CACHE_PREFIX_RANK + record.getExaminationId(),
				JsonMapper.getInstance().toJson(record), record.getScore());
	}
}
