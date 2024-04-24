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

import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.user.excel.model.MenuExcelModel;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class MenuUtil {

	private MenuUtil() {
	}

	public static boolean isPermission(Menu menu) {
		return UserServiceConstant.MENU_TYPE_PERMISSION.equals(menu.getType());
	}

	public static List<MenuExcelModel> convertToExcelModel(List<Menu> menus) {
		List<MenuExcelModel> models = Lists.newArrayListWithExpectedSize(menus.size());
		menus.forEach(menu -> {
			MenuExcelModel model = new MenuExcelModel();
			BeanUtils.copyProperties(menu, model);
			models.add(model);
		});
		return models;
	}
}
