package com.github.tangyi.exam.service.fav;

import com.github.tangyi.api.exam.service.ICourseFavoritesService;
import com.github.tangyi.api.exam.model.Course;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CourseFavoritesService extends CrudService<UserFavMapper, ExamUserFav>
		implements ICourseFavoritesService, IFavService, UserFavConstant, ExamCacheName {

	private final UserFavService userFavService;

	@Override
	public boolean favorite(Long userId, Long targetId, Integer favType) {
		return userFavService.favorite(userId, targetId, FAV_TYPE_COURSE, favType);
	}

	@Override
	public Set<Long> findUserFavorites() {
		return userFavService.findUserFavoritesIds(SysUtil.getUserId(), FAV_TYPE_COURSE);
	}

	public void findUserFavorites(List<Course> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		Set<Long> favIds = findUserFavorites();
		if (CollectionUtils.isNotEmpty(favIds)) {
			for (Course course : list) {
				course.setFavorite(favIds.contains(course.getId()));
			}
		}
	}

	/**
	 * 查询收藏次数
	 * @param list list
	 */
	public void findFavCount(List<Course> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		List<Long> ids = list.stream().map(Course::getId).collect(Collectors.toList());
		Map<Long, Long> map = userFavService.findFavCount(ids, FAV_TYPE_COURSE);
		if (MapUtils.isEmpty(map)) {
			return;
		}
		for (Course course : list) {
			Long count = map.get(course.getId());
			if (count != null) {
				course.setFavCount(count);
			}
		}
	}

	@Override
	public Map<String, Set<String>> getFavRelation() {
		return userFavService.getFavRelation(FAV_TYPE_COURSE);
	}

	@Override
	public void updateFavorite(List<Long> ids) {
		updateFavCount(ids);
		updateFavRelation();
	}

	@Override
	public void updateFavCount(List<Long> ids) {
		userFavService.updateFavCount(ids, FAV_TYPE_COURSE);
	}

	@Override
	public void updateFavRelation() {
		userFavService.updateFavRelation(FAV_TYPE_COURSE);
	}

	@Override
	public void incrStartCount(Long id) {
		userFavService.incrStartCount(id, FAV_TYPE_COURSE);
	}

	@Override
	public Map<Long, Long> findStartCounts(List<Long> ids) {
		return userFavService.findStartCounts(ids, FAV_TYPE_COURSE);
	}

	@Override
	public void updateStartCount(List<Long> ids) {
		userFavService.updateStartCount(ids, FAV_TYPE_COURSE);
	}
}
