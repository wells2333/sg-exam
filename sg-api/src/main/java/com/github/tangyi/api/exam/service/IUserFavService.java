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

package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.exam.model.ExamUserFav;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IUserFavService extends ICrudService<ExamUserFav> {

	boolean favorite(Long userId, Long targetId, int targetType, Integer favType);

	boolean isUserFavorites(Long userId, Long targetId, int targetType);

	Set<Long> findUserFavoritesIds(Long userId, int targetType);

	Map<Long, ExamUserFav> findUserFavorites(Long userId, int targetType);

	Set<Long> findUserFavoritesFromCache(Long userId, int targetType);

	Map<Long, Long> findFavCount(List<Long> ids, int targetType);
}
