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

import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.utils.SysUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractFavService {

	private final UserFavService userFavService;
	private final int targetType;

	public AbstractFavService(UserFavService userFavService, int targetType) {
		this.userFavService = userFavService;
		this.targetType = targetType;
	}

	public boolean favorite(Long userId, Long targetId, Integer favType) {
		return userFavService.favorite(userId, targetId, targetType, favType);
	}

	public boolean isUserFavorites(Long userId, Long targetId) {
		return userFavService.isUserFavorites(userId, targetId, targetType);
	}

	/**
	 * 查询用户的收藏
	 */
	public Set<Long> findUserFavorites() {
		return userFavService.findUserFavoritesIds(SysUtil.getUserId(), this.targetType);
	}

	/**
	 * 查询收藏数量
	 */
	public Map<Long, Long> findFavCount(List<? extends BaseEntity<?>> list) {
		List<Long> ids = list.stream().map(BaseEntity::getId).collect(Collectors.toList());
		return userFavService.findFavCount(ids, this.targetType);
	}

	public void incrStartCount(Long id) {
		userFavService.incrStartCount(id, this.targetType);
	}

	public Map<Long, Long> findStartCounts(List<? extends BaseEntity<?>> list) {
		List<Long> ids = list.stream().map(BaseEntity::getId).collect(Collectors.toList());
		return userFavService.findStartCounts(ids, this.targetType);
	}
}
