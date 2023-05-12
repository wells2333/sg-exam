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
			if (menuService.update(menu) < 1)
				menuService.insert(menu);
		});
	}
}
