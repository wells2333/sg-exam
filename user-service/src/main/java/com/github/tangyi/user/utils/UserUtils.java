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

/**
 * 用户工具类
 *
 * @author tangyi
 * @date 2018/11/26 22:32
 */
public class UserUtils {

	private UserUtils() {
	}

	public static List<UserExcelModel> convertToExcelModel(List<UserInfoDto> userInfoDtos) {
		List<UserExcelModel> userExcelModels = new ArrayList<>(userInfoDtos.size());
		userInfoDtos.forEach(userInfoDto -> {
			UserExcelModel userExcelModel = new UserExcelModel();
			BeanUtils.copyProperties(userInfoDto, userExcelModel);
			userExcelModels.add(userExcelModel);
		});
		return userExcelModels;
	}

	public static List<RoleVo> rolesToVo(List<Role> roles) {
		return roles.stream().map(role -> {
			RoleVo roleVo = new RoleVo();
			roleVo.setRoleCode(role.getRoleCode());
			roleVo.setRoleName(role.getRoleName());
			roleVo.setRoleDesc(role.getRoleDesc());
			return roleVo;
		}).collect(Collectors.toList());
	}

	public static void toUserInfoDto(UserInfoDto userInfoDto, User user, UserAuths userAuths) {
		BeanUtils.copyProperties(userAuths, userInfoDto);
		BeanUtils.copyProperties(user, userInfoDto);
	}
}
