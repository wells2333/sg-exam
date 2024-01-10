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
