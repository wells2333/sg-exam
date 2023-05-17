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
