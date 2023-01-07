package com.github.tangyi.user.utils;

import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.user.excel.model.MenuExcelModel;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuUtil {

	private MenuUtil() {
	}

	public static boolean isPermission(Menu menu) {
		return UserServiceConstant.MENU_TYPE_PERMISSION.equals(menu.getType());
	}

	public static List<MenuExcelModel> convertToExcelModel(List<Menu> menus) {
		List<MenuExcelModel> menuExcelModels = new ArrayList<>(menus.size());
		menus.forEach(menu -> {
			MenuExcelModel menuExcelModel = new MenuExcelModel();
			BeanUtils.copyProperties(menu, menuExcelModel);
			menuExcelModels.add(menuExcelModel);
		});
		return menuExcelModels;
	}
}
