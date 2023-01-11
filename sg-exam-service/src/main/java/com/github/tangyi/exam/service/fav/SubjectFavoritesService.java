package com.github.tangyi.exam.service.fav;

import com.github.tangyi.api.exam.model.ExamUserFav;
import com.github.tangyi.api.exam.service.ISubjectFavoritesService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.constants.UserFavConstant;
import com.github.tangyi.exam.mapper.UserFavMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class SubjectFavoritesService extends CrudService<UserFavMapper, ExamUserFav>
		implements ISubjectFavoritesService, IFavService, UserFavConstant, ExamCacheName {

	private final UserFavService userFavService;

	@Override
	public boolean favorite(Long userId, Long targetId, Integer favType) {
		return userFavService.favorite(userId, targetId, FAV_TYPE_SUBJECT, favType);
	}

	@Override
	public Set<Long> findUserFavorites() {
		return userFavService.findUserFavoritesIds(SysUtil.getUserId(), FAV_TYPE_SUBJECT);
	}

	@Override
	public Map<String, Set<String>> getFavRelation() {
		return userFavService.getFavRelation(FAV_TYPE_SUBJECT);
	}

	@Override
	public void updateFavorite(List<Long> ids) {

	}

	@Override
	public void updateFavCount(List<Long> ids) {

	}

	@Override
	public void updateFavRelation() {

	}

	@Override
	public void incrStartCount(Long id) {
		userFavService.incrStartCount(id, FAV_TYPE_SUBJECT);
	}

	@Override
	public Map<Long, Long> findStartCounts(List<Long> ids) {
		return userFavService.findStartCounts(ids, FAV_TYPE_SUBJECT);
	}

	@Override
	public void updateStartCount(List<Long> ids) {
		userFavService.updateStartCount(ids, FAV_TYPE_SUBJECT);
	}
}
