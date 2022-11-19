package com.github.tangyi.exam.service.data;

import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.model.ExamExaminationFavorites;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamExaminationFavoritesMapper;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import com.github.tangyi.exam.service.ExamExaminationFavoritesService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 *
 * @author tangyi
 * @date 2022/11/12 10:53 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamFavoriteService {

	private final ExaminationMapper examinationMapper;

	private final ExamExaminationFavoritesService favoritesService;

	private final ExamExaminationFavoritesMapper favoritesMapper;

	private final RedisTemplate<String, Long> longRedisTemplate;

	private final RedisCounterService redisCounterService;

	/**
	 * 收藏考试
	 * @param examinationId examinationId
	 */
	@Transactional
	public boolean favoriteExam(Long userId, Long examinationId, Integer type) {
		Set<String> set = findUserFavoritesIdsFromRedis(userId);
		if (Status.ONE == type) {
			set.add(examinationId.toString());
			redisCounterService.incrCount(ExamCacheName.EXAMINATION_FAV_COUNT, examinationId);
		} else {
			set.remove(examinationId.toString());
			ExamExaminationFavorites fav = new ExamExaminationFavorites();
			fav.setExaminationId(examinationId);
			fav.setUserId(userId);
			favoritesService.deleteByUserIdAndExaminationId(fav);
			redisCounterService.decrCount(ExamCacheName.EXAMINATION_FAV_COUNT, examinationId);
		}
		String newValue = CollectionUtils.isNotEmpty(set) ? StringUtils.join(set, ",") : "";
		longRedisTemplate.opsForHash().put(ExamCacheName.EXAMINATION_USER_FAVORITES, userId, newValue);
		return true;
	}

	/**
	 * 查询用户的收藏
	 */
	public List<Long> findUserFavoritesExamIds() {
		List<ExamExaminationFavorites> favorites = findUserFavorites();
		if (CollectionUtils.isEmpty(favorites)) {
			return Lists.newArrayList();
		}
		return favorites.stream().map(ExamExaminationFavorites::getExaminationId).collect(Collectors.toList());
	}

	public void findUserFavorites(List<ExaminationDto> dtoList) {
		List<ExamExaminationFavorites> favorites = findUserFavorites();
		if (CollectionUtils.isEmpty(favorites)) {
			return;
		}
		for (ExaminationDto dto : dtoList) {
			for (ExamExaminationFavorites favorite : favorites) {
				if (favorite.getExaminationId().equals(dto.getId())) {
					dto.setFavorite(true);
					break;
				}
			}
		}
	}

	public List<ExamExaminationFavorites> findUserFavorites() {
		Long userId = SysUtil.getUserId();
		if (userId == null) {
			return Lists.newArrayList();
		}
		List<ExamExaminationFavorites> favorites = Lists.newArrayList();
		Set<String> set = findUserFavoritesIdsFromRedis(userId);
		for (String eId : set) {
			ExamExaminationFavorites fav = new ExamExaminationFavorites();
			fav.setUserId(userId);
			fav.setExaminationId(Long.valueOf(eId));
			favorites.add(fav);
		}
		return favorites;
	}

	/**
	 * 从Redis获取已收藏的考试ID
	 * @param userId userId
	 * @return Set
	 */
	public Set<String> findUserFavoritesIdsFromRedis(Long userId) {
		Object value = longRedisTemplate.opsForHash().get(ExamCacheName.EXAMINATION_USER_FAVORITES, userId);
		if (value != null) {
			return Stream.of(value.toString().split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		}
		return Sets.newHashSet();
	}

	/**
	 * 查询收藏次数
	 * @param dtoList dtoList
	 */
	public void findExamFavCount(List<ExaminationDto> dtoList) {
		List<Long> ids = dtoList.stream().map(ExaminationDto::getId).collect(Collectors.toList());
		Map<Long, Long> map = redisCounterService.getCounts(ExamCacheName.EXAMINATION_FAV_COUNT, ids);
		if (MapUtils.isEmpty(map)) {
			return;
		}
		for (ExaminationDto dto : dtoList) {
			Long count = map.get(dto.getId());
			if (count != null) {
				dto.setFavoritesCount(count);
			}
		}
	}

	/**
	 * 获取Redis中的收藏关系数据
	 * @return Map
	 */
	public Map<String, Set<String>> getRedisFavRelation() {
		Map<String, Set<String>> user2FavIdMap = Maps.newHashMap();
		Set<Object> fields = longRedisTemplate.opsForHash().keys(ExamCacheName.EXAMINATION_USER_FAVORITES);
		if (CollectionUtils.isNotEmpty(fields)) {
			for (Object fieldObj : fields) {
				Object value = longRedisTemplate.opsForHash().get(ExamCacheName.EXAMINATION_USER_FAVORITES, fieldObj);
				if (value != null) {
					String userId = fieldObj.toString();
					Stream.of(value.toString().split(",")).filter(StringUtils::isNotBlank).forEach(eId -> {
						user2FavIdMap.computeIfAbsent(userId, s -> Sets.newHashSet()).add(eId);
					});
				}
			}
		}
		log.info("get redis fav relation success, size: {}", user2FavIdMap.size());
		return user2FavIdMap;
	}

	@Transactional
	public void updateFavorite(List<Long> ids) {
		// 收藏数量
		updateFavCount(ids);
		// 更新用户收藏考试的关系
		updateFavRelation();
	}

	@Transactional
	public void updateFavCount(List<Long> ids) {
		Map<Long, Long> countMap = redisCounterService.getCounts(ExamCacheName.EXAMINATION_FAV_COUNT, ids);
		if (MapUtils.isNotEmpty(countMap)) {
			List<Examination> examinations = Lists.newArrayList();
			for (Map.Entry<Long, Long> entry : countMap.entrySet()) {
				Examination examination = examinationMapper.selectByPrimaryKey(entry.getKey());
				if (examination != null && !entry.getValue().equals(examination.getFavoritesCount())) {
					examination.setFavoritesCount(entry.getValue());
					examinations.add(examination);
				}
			}
			// 更新收藏数量
			if (CollectionUtils.isNotEmpty(examinations)) {
				for (List<Examination> tempList : Lists.partition(examinations, 50)) {
					examinationMapper.bathUpdateStartCountAndFavorite(tempList);
				}
			}
			log.info("update favorite count success, size: {}", examinations.size());
		}
	}

	@Transactional
	public void updateFavRelation() {
		List<ExamExaminationFavorites> favorites = Lists.newArrayList();
		Map<String, Set<String>> user2FavIdMap = getRedisFavRelation();
		if (MapUtils.isNotEmpty(user2FavIdMap)) {
			for (Map.Entry<String, Set<String>> entry : user2FavIdMap.entrySet()) {
				Long userId = Long.valueOf(entry.getKey());
				for (String eId : entry.getValue()) {
					ExamExaminationFavorites fav = new ExamExaminationFavorites();
					fav.setUserId(userId);
					fav.setExaminationId(Long.valueOf(eId));
					favorites.add(fav);
				}
			}
			// 用户收藏的数据
			for (ExamExaminationFavorites fav : favorites) {
				// 先查询，无结果再插入
				ExamExaminationFavorites tempFav = favoritesService.getByUserIdAndExaminationId(fav);
				if (tempFav == null) {
					fav.setCommonValue(TenantConstant.DEFAULT_TENANT_CODE, TenantConstant.DEFAULT_TENANT_CODE);
					favoritesMapper.insert(fav);
				}
			}
		}
		log.info("update fav relation success, size: {}", favorites.size());
	}
}
