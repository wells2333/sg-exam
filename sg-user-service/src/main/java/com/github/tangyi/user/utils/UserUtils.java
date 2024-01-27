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
