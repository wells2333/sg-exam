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

package com.github.tangyi.exam.service.fav;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamUserFav;
import com.github.tangyi.api.exam.service.IUserFavService;
import com.github.tangyi.common.config.CustomGlobalExceptionHandler;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.TxUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.constants.UserFavConstant;
import com.github.tangyi.exam.mapper.UserFavMapper;
import com.github.tangyi.exam.service.data.RedisCounterService;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserFavService extends CrudService<UserFavMapper, ExamUserFav>
		implements IUserFavService, UserFavConstant, ExamCacheName {

	private final RedisTemplate<String, Long> longRedisTemplate;
	private final RedisCounterService redisCounterService;
	private final UserFavMapper userFavoritesMapper;
	private final PlatformTransactionManager txManager;

	@Override
	@Cacheable(value = ExamCacheName.USER_FAVORITES, key = "#id")
	public ExamUserFav get(Long id) {
		return super.get(id);
	}

	public PageInfo<ExamUserFav> findUserFavoritesPage(Map<String, Object> params, int pageNum, int pageSize,
			String targetType) {
		params.put("targetType", targetType);
		return super.findPage(params, pageNum, pageSize);
	}

	@Override
	@Transactional
	public int insert(ExamUserFav examUserFav) {
		return super.insert(examUserFav);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.USER_FAVORITES, key = "#examUserFav.id")
	public int update(ExamUserFav examUserFav) {
		return super.update(examUserFav);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.USER_FAVORITES, key = "#examUserFav.id")
	public int delete(ExamUserFav examUserFav) {
		return super.delete(examUserFav);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.USER_FAVORITES, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	/**
	 * 收藏、取消收藏
	 * @param userId 用户 ID
	 * @param targetId 目标 ID，和业务相关，如考试、课程等
	 * @param targetType 目标类型，和业务相关，如考试、课程等
	 * @param favType 操作类型：1：收藏，其它：取消收藏
	 * @return true: 操作成功，false: 操作失败
	 */
	@Override
	public boolean favorite(Long userId, Long targetId, int targetType, Integer favType) {
		ExamUserFav fav = new ExamUserFav();
		fav.setTargetId(targetId);
		fav.setTargetType(targetType);
		fav.setUserId(userId);
		boolean isFav = Status.ONE.equals(favType);

		// 更新 DB
		boolean isDbDuplicate = false;
		TransactionStatus status = TxUtil.startTransaction(txManager);
		try {
			if (isFav) {
				fav.setCommonValue();
				userFavoritesMapper.insert(fav);
			} else {
				userFavoritesMapper.deleteByUserIdAndTarget(fav);
			}
			txManager.commit(status);
		} catch (DuplicateKeyException e) {
			txManager.rollback(status);
			log.warn("Execute favorite, userId: {}, targetId: {}, targetType: {}, favType: {}, msg: {}", userId,
					targetId, targetType, favType, e.getMessage());
			isDbDuplicate = true;
		} catch (Exception e) {
			txManager.rollback(status);
			log.error("Failed to execute favorite, userId: {}, targetId: {}, targetType: {}, favType: {}", userId,
					targetId, targetType, favType, e);
			CustomGlobalExceptionHandler.throwInternalServerError();
		}

		// 更新 Redis
		this.updateUserFavorites(fav, isFav, !isDbDuplicate);
		log.info("Execute favorite success, userId: {}, targetId: {}, targetType: {}", userId, targetId, targetType);
		return true;
	}

	@Override
	public boolean isUserFavorites(Long userId, Long targetId, int targetType) {
		String favKey = getUserFavKey(userId, targetType);
		Double score = longRedisTemplate.opsForZSet().score(favKey, targetId);
		// Redis 没有数据，查询 DB
		if (score == null) {
			ExamUserFav fav = new ExamUserFav();
			fav.setTargetId(targetId);
			fav.setTargetType(targetType);
			fav.setUserId(userId);
			fav = userFavoritesMapper.getByUserIdAndTarget(fav);
			if (fav == null) {
				return false;
			}

			// DB 数据写入 Redis
			long millis = Objects.requireNonNull(fav.getCreateTime()).getTime();
			return Boolean.TRUE.equals(longRedisTemplate.opsForZSet().add(favKey, targetId, millis));
		}
		return score > 0;
	}

	@Override
	public Set<Long> findUserFavoritesIds(Long userId, int targetType) {
		if (userId == null) {
			return Collections.emptySet();
		}

		return findUserFavorites(userId, targetType).keySet();
	}

	@Override
	public Map<Long, ExamUserFav> findUserFavorites(Long userId, int targetType) {
		if (userId == null) {
			return Collections.emptyMap();
		}

		Map<Long, ExamUserFav> favorites = Maps.newHashMap();
		Set<Long> set = findUserFavoritesFromCache(userId, targetType);
		for (Long targetId : set) {
			ExamUserFav fav = new ExamUserFav();
			fav.setUserId(userId);
			fav.setTargetId(targetId);
			favorites.put(targetId, fav);
		}
		return favorites;
	}

	@Override
	public Set<Long> findUserFavoritesFromCache(Long userId, int targetType) {
		String favKey = getUserFavKey(userId, targetType);
		Set<Long> values = longRedisTemplate.opsForZSet().range(favKey, 0, -1);
		return CollectionUtils.isNotEmpty(values) ? values : Collections.emptySet();
	}

	@Override
	public Map<Long, Long> findFavCount(List<Long> ids, int targetType) {
		String facCountKey = getFavCountKey(targetType);
		return redisCounterService.getCounts(facCountKey, ids);
	}

	public void incrStartCount(Long id, int targetType) {
		String key = getStartCountKey(targetType);
		redisCounterService.incrCount(key, id);
		log.info("incr start count success, id: {}, targetType: {}", id, targetType);
	}

	public void setStartCount(Long id, int targetType, Long value) {
		String key = getStartCountKey(targetType);
		redisCounterService.setCount(key, id, value);
		log.info("set start count success, id: {}, targetType: {}, value: {}", id, targetType, value);
	}

	public void setFavCount(Long id, int targetType, Long value) {
		String key = getFavCountKey(targetType);
		redisCounterService.setCount(key, id, value);
		log.info("set fav count success, id: {}, targetType: {}, value: {}", id, targetType, value);
	}

	public Map<Long, Long> findStartCounts(List<Long> ids, int targetType) {
		String key = getStartCountKey(targetType);
		return redisCounterService.getCounts(key, ids);
	}

	/**
	 * 更新 Redis 的收藏列表和收藏数量
	 */
	private void updateUserFavorites(ExamUserFav fav, boolean isFav, boolean updateFavCount) {
		Long userId = fav.getUserId();
		Long targetId = fav.getTargetId();
		Integer targetType = fav.getTargetType();
		String favKey = getUserFavKey(userId, targetType);
		String favCountKey = getFavCountKey(targetType);
		if (isFav) {
			longRedisTemplate.opsForZSet().add(favKey, targetId, Objects.requireNonNull(fav.getCreateTime()).getTime());
			if (updateFavCount) {
				redisCounterService.incrCount(favCountKey, targetId);
			}
		} else {
			longRedisTemplate.opsForZSet().remove(favKey, targetId);
			if (updateFavCount) {
				redisCounterService.decrCount(favCountKey, targetId);
			}
		}
	}

	private String getFavCountKey(int targetType) {
		String key = "";
		switch (targetType) {
			case FAV_TYPE_EXAM -> key = USER_FAVORITES_EXAMINATION_COUNT;
			case FAV_TYPE_COURSE -> key = USER_FAVORITES_COURSE_COUNT;
			case FAV_TYPE_SUBJECT -> key = USER_FAVORITES_SUBJECT_COUNT;
			default -> {
			}
		}
		return validKey(key);
	}

	private String getUserFavKey(Long userId, int targetType) {
		String key = null;
		switch (targetType) {
			case FAV_TYPE_EXAM -> key = USER_FAVORITES_EXAMINATION + userId;
			case FAV_TYPE_COURSE -> key = USER_FAVORITES_COURSE + userId;
			case FAV_TYPE_SUBJECT -> key = USER_FAVORITES_SUBJECT + userId;
			default -> {
			}
		}
		return Objects.requireNonNull(key);
	}

	private String getStartCountKey(int targetType) {
		String key = "";
		switch (targetType) {
			case FAV_TYPE_EXAM -> key = EXAMINATION_START_COUNT;
			case FAV_TYPE_COURSE -> key = COURSE_START_COUNT;
			case FAV_TYPE_SUBJECT -> key = SUBJECT_START_COUNT;
			default -> {
			}
		}
		return validKey(key);
	}

	private String validKey(String key) {
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Invalid targetType");
		}
		return key;
	}
}
