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

import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.common.excel.AbstractExcelImportListener;
import com.github.tangyi.user.excel.model.MenuExcelModel;
import com.github.tangyi.user.service.sys.MenuService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class MenuImportListener extends AbstractExcelImportListener<MenuExcelModel> {

	private final MenuService menuService;

	public MenuImportListener(MenuService menuService) {
		this.menuService = menuService;
	}

	@Override
	public void saveData(List<MenuExcelModel> models) {
		logger.info("SaveData size: {}", models.size());
		List<Menu> menuList = Lists.newArrayListWithExpectedSize(models.size());
		models.forEach(data -> {
			Menu menu = new Menu();
			BeanUtils.copyProperties(data, menu);
			menuList.add(menu);
		});
		menuList.forEach(menu -> {
			if (menuService.update(menu) < 1) {
				menuService.insert(menu);
			}
		});
	}
}
