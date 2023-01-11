package com.github.tangyi.exam.service.fav;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFavService {

	/**
	 * 点击收藏、取消收藏
	 */
	boolean favorite(Long userId, Long targetId, Integer favType);

	Set<Long> findUserFavorites();

	Map<String, Set<String>> getFavRelation();

	void updateFavorite(List<Long> ids);

	void updateFavCount(List<Long> ids);

	void updateFavRelation();

	void incrStartCount(Long id);

	Map<Long, Long> findStartCounts(List<Long> ids);

	void updateStartCount(List<Long> ids);
}
