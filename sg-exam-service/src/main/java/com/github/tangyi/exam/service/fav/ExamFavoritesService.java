package com.github.tangyi.exam.service.fav;

import com.github.tangyi.api.exam.service.IExamFavoritesService;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.model.ExamUserFav;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.constants.UserFavConstant;
import com.github.tangyi.exam.mapper.UserFavMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ExamFavoritesService extends CrudService<UserFavMapper, ExamUserFav>
		implements IExamFavoritesService, IFavService, UserFavConstant, ExamCacheName {

	private final UserFavService userFavService;

	@Override
	public boolean favorite(Long userId, Long targetId, Integer favType) {
		return userFavService.favorite(userId, targetId, FAV_TYPE_EXAM, favType);
	}

	public void findUserFavorites(List<ExaminationDto> dtoList) {
		Set<Long> favorites = findUserFavorites();
		if (CollectionUtils.isEmpty(favorites)) {
			return;
		}
		for (ExaminationDto dto : dtoList) {
			dto.setFavorite(favorites.contains(dto.getId()));
		}
	}

	/**
	 * 查询用户的收藏
	 */
	@Override
	public Set<Long> findUserFavorites() {
		return userFavService.findUserFavoritesIds(SysUtil.getUserId(), FAV_TYPE_EXAM);
	}

	/**
	 * 查询收藏次数
	 * @param list list
	 */
	public void findFavCount(List<ExaminationDto> list) {
		List<Long> ids = list.stream().map(ExaminationDto::getId).collect(Collectors.toList());
		Map<Long, Long> map = userFavService.findFavCount(ids, FAV_TYPE_EXAM);
		if (MapUtils.isEmpty(map)) {
			return;
		}
		for (ExaminationDto dto : list) {
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
	@Override
	public Map<String, Set<String>> getFavRelation() {
		return userFavService.getFavRelation(FAV_TYPE_EXAM);
	}

	@Override
	@Transactional
	public void updateFavorite(List<Long> ids) {
		updateFavCount(ids);
		updateFavRelation();
	}

	@Override
	@Transactional
	public void updateFavCount(List<Long> ids) {
		userFavService.updateFavCount(ids, FAV_TYPE_EXAM);
	}

	@Override
	@Transactional
	public void updateFavRelation() {
		userFavService.updateFavRelation(FAV_TYPE_EXAM);
	}

	@Override
	public void incrStartCount(Long id) {
		userFavService.incrStartCount(id, FAV_TYPE_EXAM);
	}

	@Override
	public Map<Long, Long> findStartCounts(List<Long> ids) {
		return userFavService.findStartCounts(ids, FAV_TYPE_EXAM);
	}

	public void findExamStartCounts(List<ExaminationDto> list) {
		List<Long> ids = list.stream().map(ExaminationDto::getId).collect(Collectors.toList());
		Map<Long, Long> map = findStartCounts(ids);
		if (MapUtils.isEmpty(map)) {
			return;
		}
		for (ExaminationDto dto : list) {
			dto.setStartCount(map.get(dto.getId()));
		}
	}

	@Override
	public void updateStartCount(List<Long> ids) {
		userFavService.updateStartCount(ids, FAV_TYPE_EXAM);
	}
}
