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

import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.constants.UserFavConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ExamFavService extends AbstractFavService implements IFavService, UserFavConstant, ExamCacheName {

	public ExamFavService(UserFavService userFavService) {
		super(userFavService, FAV_TYPE_EXAM);
	}

	public void findUserFavorites(List<ExaminationDto> dtoList) {
		Set<Long> favorites = null;
		try {
			favorites = findUserFavorites();
		} catch (Exception e) {
			log.error("Failed to find user favorites.", e);
		}
		if (CollectionUtils.isEmpty(favorites)) {
			return;
		}

		for (ExaminationDto dto : dtoList) {
			dto.setFavorite(favorites.contains(dto.getId()));
		}
	}

	public void fillFavCount(List<ExaminationDto> list) {
		Map<Long, Long> map = super.findFavCount(list);
		for (ExaminationDto dto : list) {
			dto.setFavoritesCount(map.get(dto.getId()));
		}
	}

	public void fillStartCounts(List<ExaminationDto> list) {
		Map<Long, Long> map = findStartCounts(list);
		if (MapUtils.isEmpty(map)) {
			return;
		}

		for (ExaminationDto dto : list) {
			dto.setStartCount(map.get(dto.getId()));
		}
	}
}
