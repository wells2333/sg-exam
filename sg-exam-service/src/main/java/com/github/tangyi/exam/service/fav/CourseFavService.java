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

import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.constants.UserFavConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CourseFavService extends AbstractFavService implements IFavService, UserFavConstant, ExamCacheName {

	public CourseFavService(UserFavService userFavService) {
		super(userFavService, FAV_TYPE_COURSE);
	}

	public void fillUserFavorites(List<Course> list) {
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
}
