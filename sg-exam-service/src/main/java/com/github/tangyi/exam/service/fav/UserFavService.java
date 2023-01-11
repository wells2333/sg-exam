package com.github.tangyi.exam.service.fav;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamFavStartCount;
import com.github.tangyi.api.exam.model.ExamUserFav;
import com.github.tangyi.api.exam.service.IUserFavService;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.constants.UserFavConstant;
import com.github.tangyi.exam.mapper.ExamFavStartCountMapper;
import com.github.tangyi.exam.mapper.UserFavMapper;
import com.github.tangyi.exam.service.data.RedisCounterService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class UserFavService extends CrudService<UserFavMapper, ExamUserFav>
		implements IUserFavService, UserFavConstant, ExamCacheName {

	private final RedisTemplate<String, Long> longRedisTemplate;

	private final RedisCounterService redisCounterService;

	private final UserFavMapper userFavoritesMapper;

	private final ExamFavStartCountMapper favStartCountMapper;

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

	public ExamUserFav getByUserIdAndTarget(ExamUserFav favorites) {
		return this.dao.getByUserIdAndTarget(favorites);
	}

	public String getFavCountKey(int targetType) {
		String key = "";
		switch (targetType) {
			case FAV_TYPE_EXAM -> key = USER_FAVORITES_EXAMINATION_COUNT;
			case FAV_TYPE_COURSE -> key = USER_FAVORITES_COURSE_COUNT;
			case FAV_TYPE_SUBJECT -> key = USER_FAVORITES_SUBJECT_COUNT;
			default -> {
			}
		}
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("invalid targetType");
		}
		return key;
	}

	public String getUserFavKey(int targetType) {
		String key = "";
		switch (targetType) {
			case FAV_TYPE_EXAM -> key = USER_FAVORITES_EXAMINATION;
			case FAV_TYPE_COURSE -> key = USER_FAVORITES_COURSE;
			case FAV_TYPE_SUBJECT -> key = USER_FAVORITES_SUBJECT;
			default -> {
			}
		}
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("invalid targetType");
		}
		return key;
	}

	public String getStartCountKey(int targetType) {
		String key = "";
		switch (targetType) {
			case FAV_TYPE_EXAM -> key = EXAMINATION_START_COUNT;
			case FAV_TYPE_COURSE -> key = COURSE_START_COUNT;
			case FAV_TYPE_SUBJECT -> key = SUBJECT_START_COUNT;
			default -> {
			}
		}
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("invalid targetType");
		}
		return key;
	}

	@Transactional
	public boolean favorite(Long userId, Long targetId, int targetType, Integer favType) {
		String favCountKey = getFavCountKey(targetType);
		Set<String> set = findUserFavoritesFromCache(userId, targetType);
		ExamUserFav fav = new ExamUserFav();
		fav.setTargetId(targetId);
		fav.setTargetType(targetType);
		fav.setUserId(userId);
		if (Status.ONE == favType) {
			set.add(targetId.toString());
			redisCounterService.incrCount(favCountKey, targetId);
			fav.setCommonValue();
			userFavoritesMapper.insert(fav);
		} else {
			set.remove(targetId.toString());
			userFavoritesMapper.deleteByUserIdAndTarget(fav);
			redisCounterService.decrCount(favCountKey, targetId);
		}
		String newValue = CollectionUtils.isNotEmpty(set) ? StringUtils.join(set, ",") : "";
		String userFavKey = getUserFavKey(targetType);
		longRedisTemplate.opsForHash().put(userFavKey, userId, newValue);
		log.info("favorite success, targetId: {}, targetType: {}", targetId, targetType);
		return true;
	}

	public Set<Long> findUserFavoritesIds(Long userId, int targetType) {
		Map<Long, ExamUserFav> favorites = findUserFavorites(userId, targetType);
		return favorites.keySet();
	}

	public Map<Long, ExamUserFav> findUserFavorites(Long userId, int targetType) {
		if (userId == null) {
			return Maps.newHashMap();
		}
		Map<Long, ExamUserFav> favorites = Maps.newHashMap();
		Set<String> set = findUserFavoritesFromCache(userId, targetType);
		for (String eId : set) {
			Long targetId = Long.valueOf(eId);
			ExamUserFav fav = new ExamUserFav();
			fav.setUserId(userId);
			fav.setTargetId(targetId);
			favorites.put(targetId, fav);
		}
		return favorites;
	}

	public Set<String> findUserFavoritesFromCache(Long userId, int targetType) {
		String favKey = getUserFavKey(targetType);
		Object value = longRedisTemplate.opsForHash().get(favKey, userId);
		if (value != null) {
			return Stream.of(value.toString().split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		}
		return Sets.newHashSet();
	}

	public Map<Long, Long> findFavCount(List<Long> ids, int targetType) {
		String facCountKey = getFavCountKey(targetType);
		return redisCounterService.getCounts(facCountKey, ids);
	}

	public Map<String, Set<String>> getFavRelation(int targetType) {
		String userFavKey = getUserFavKey(targetType);
		Map<String, Set<String>> user2FavIdMap = Maps.newHashMap();
		Set<Object> fields = longRedisTemplate.opsForHash().keys(userFavKey);
		if (CollectionUtils.isNotEmpty(fields)) {
			for (Object fieldObj : fields) {
				Object value = longRedisTemplate.opsForHash().get(userFavKey, fieldObj);
				if (value != null) {
					String userId = fieldObj.toString();
					Stream.of(value.toString().split(",")).filter(StringUtils::isNotBlank)
							.forEach(eId -> user2FavIdMap.computeIfAbsent(userId, s -> Sets.newHashSet()).add(eId));
				}
			}
		}
		log.info("get redis fav relation success, size: {}", user2FavIdMap.size());
		return user2FavIdMap;
	}

	public void updateFavRelation(int targetType) {
		List<ExamUserFav> favorites = Lists.newArrayList();
		Map<String, Set<String>> user2FavIdMap = getFavRelation(targetType);
		if (MapUtils.isNotEmpty(user2FavIdMap)) {
			for (Map.Entry<String, Set<String>> entry : user2FavIdMap.entrySet()) {
				Long userId = Long.valueOf(entry.getKey());
				for (String eId : entry.getValue()) {
					ExamUserFav fav = new ExamUserFav();
					fav.setUserId(userId);
					fav.setTargetId(Long.valueOf(eId));
					fav.setTargetType(targetType);
					favorites.add(fav);
				}
			}
			for (ExamUserFav fav : favorites) {
				ExamUserFav tempFav = getByUserIdAndTarget(fav);
				if (tempFav == null) {
					fav.setCommonValue(TenantConstant.DEFAULT_TENANT_CODE, TenantConstant.DEFAULT_TENANT_CODE);
					insert(fav);
				}
			}
		}
		log.info("update fav relation success, size: {}", favorites.size());
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

	@Transactional
	public void updateFavCount(List<Long> ids, int targetType) {
		Map<Long, Long> countMap = findFavCount(ids, targetType);
		if (MapUtils.isEmpty(countMap)) {
			return;
		}
		List<ExamFavStartCount> list = Lists.newArrayListWithExpectedSize(countMap.size());
		List<ExamFavStartCount> inserts = Lists.newArrayList();
		for (Map.Entry<Long, Long> entry : countMap.entrySet()) {
			ExamFavStartCount count = favStartCountMapper.findByTarget(entry.getKey(), targetType);
			if (count != null) {
				if (!entry.getValue().equals(count.getFavCount())) {
					count.setFavCount(entry.getValue());
					list.add(count);
				}
			} else {
				count = new ExamFavStartCount();
				count.setCommonValue(TenantConstant.DEFAULT_TENANT_CODE, TenantConstant.DEFAULT_TENANT_CODE);
				count.setTargetId(entry.getKey());
				count.setTargetType(targetType);
				count.setFavCount(entry.getValue());
				count.setStartCount(0L);
				inserts.add(count);
			}
		}
		if (CollectionUtils.isNotEmpty(list)) {
			for (List<ExamFavStartCount> tempList : Lists.partition(list, 50)) {
				favStartCountMapper.bathUpdate(tempList);
			}
			log.info("update fav count success, size: {}", list.size());
		}
		if (CollectionUtils.isNotEmpty(inserts)) {
			favStartCountMapper.batchInsert(inserts);
			log.info("insert fav count success, size: {}", inserts.size());
		}
	}

	@Transactional
	public void updateStartCount(List<Long> ids, int targetType) {
		String key = getStartCountKey(targetType);
		Map<Long, Long> countMap = redisCounterService.getCounts(key, ids);
		if (MapUtils.isEmpty(countMap)) {
			return;
		}
		List<ExamFavStartCount> updates = Lists.newArrayList();
		List<ExamFavStartCount> inserts = Lists.newArrayList();
		for (Map.Entry<Long, Long> entry : countMap.entrySet()) {
			ExamFavStartCount count = favStartCountMapper.findByTarget(entry.getKey(), targetType);
			if (count != null) {
				if (!entry.getValue().equals(count.getStartCount())) {
					count.setStartCount(entry.getValue());
					updates.add(count);
				}
			} else {
				count = new ExamFavStartCount();
				count.setCommonValue(TenantConstant.DEFAULT_TENANT_CODE, TenantConstant.DEFAULT_TENANT_CODE);
				count.setTargetId(entry.getKey());
				count.setTargetType(targetType);
				count.setFavCount(0L);
				count.setStartCount(entry.getValue());
				inserts.add(count);
			}
		}
		if (CollectionUtils.isNotEmpty(updates)) {
			favStartCountMapper.bathUpdate(updates);
			log.info("update start count success, size: {}", updates.size());
		}
		if (CollectionUtils.isNotEmpty(inserts)) {
			favStartCountMapper.batchInsert(inserts);
			log.info("insert start count success, size: {}", inserts.size());
		}
	}
}
