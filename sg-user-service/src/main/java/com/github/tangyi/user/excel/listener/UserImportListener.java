package com.github.tangyi.user.excel.listener;

import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.common.excel.AbstractExcelImportListener;
import com.github.tangyi.user.excel.model.UserExcelModel;
import com.github.tangyi.user.service.sys.UserService;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class UserImportListener extends AbstractExcelImportListener<UserExcelModel> {

	private UserService userService;

	public UserImportListener(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 存储到数据库
	 */
	@Override
	public void saveData(List<UserExcelModel> userExcelModels) {
		logger.info("SaveData size: {}", userExcelModels.size());
		List<UserInfoDto> userInfoDtoList = new ArrayList<>(userExcelModels.size());
		userExcelModels.forEach(data -> {
			UserInfoDto userInfoDto = new UserInfoDto();
			BeanUtils.copyProperties(data, userInfoDto);
			userInfoDtoList.add(userInfoDto);
		});
		userService.importUsers(userInfoDtoList);
	}
}
