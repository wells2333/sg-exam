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

package com.github.tangyi.user.excel.listener;

import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.common.excel.AbstractExcelImportListener;
import com.github.tangyi.user.excel.model.UserExcelModel;
import com.github.tangyi.user.service.sys.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class UserImportListener extends AbstractExcelImportListener<UserExcelModel> {

	private final UserService userService;

	public UserImportListener(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void saveData(List<UserExcelModel> models) {
		logger.info("SaveData size: {}", models.size());
		List<UserInfoDto> dtoList = Lists.newArrayListWithExpectedSize(models.size());
		models.forEach(data -> {
			UserInfoDto userInfoDto = new UserInfoDto();
			BeanUtils.copyProperties(data, userInfoDto);
			dtoList.add(userInfoDto);
		});
		userService.importUsers(dtoList);
	}
}
