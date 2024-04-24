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

package com.github.tangyi.user.utils;

import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.common.vo.RoleVo;
import com.github.tangyi.user.excel.model.UserExcelModel;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserUtils {

	private UserUtils() {
	}

	public static List<UserExcelModel> convertToExcelModel(List<UserInfoDto> dtoList) {
		List<UserExcelModel> models = new ArrayList<>(dtoList.size());
		dtoList.forEach(dto -> {
			UserExcelModel model = new UserExcelModel();
			BeanUtils.copyProperties(dto, model);
			models.add(model);
		});
		return models;
	}

	public static List<RoleVo> rolesToVo(List<Role> roles) {
		return roles.stream().map(role -> {
			RoleVo vo = new RoleVo();
			vo.setRoleCode(role.getRoleCode());
			vo.setRoleName(role.getRoleName());
			vo.setRoleDesc(role.getRoleDesc());
			return vo;
		}).collect(Collectors.toList());
	}

	public static void toUserInfoDto(UserInfoDto dtoList, User user, UserAuths userAuths) {
		BeanUtils.copyProperties(userAuths, dtoList);
		BeanUtils.copyProperties(user, dtoList);
	}
}
