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

package com.github.tangyi.user.controller.sys;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.model.*;
import com.github.tangyi.api.user.service.IIdentifyService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TenantHolder;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.user.excel.listener.UserImportListener;
import com.github.tangyi.user.excel.model.UserExcelModel;
import com.github.tangyi.user.service.ValidateCodeService;
import com.github.tangyi.user.service.sys.DeptService;
import com.github.tangyi.user.service.sys.UserAuthsService;
import com.github.tangyi.user.service.sys.UserRoleService;
import com.github.tangyi.user.service.sys.UserService;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Tag(name = "用户信息管理")
@RestController
@RequestMapping(value = "/v1/user")
public class UserController extends BaseController {

	private final UserService userService;
	private final IIdentifyService identifyService;
	private final UserRoleService userRoleService;
	private final DeptService deptService;
	private final UserAuthsService userAuthsService;
	private final ValidateCodeService validateCodeService;

	@GetMapping("/{id}")
	@Operation(summary = "获取用户信息", description = "根据用户 ID 获取用户详细信息")
	public R<User> get(@PathVariable Long id) {
		return R.success(userService.get(id));
	}

	@GetMapping("info")
	@Operation(summary = "获取用户信息（角色、权限）", description = "获取当前登录用户详细信息")
	public R<UserInfoDto> userInfo(@RequestParam(required = false) String identityType) {
		try {
			UserVo userVo = new UserVo();
			if (StringUtils.isNotEmpty(identityType)) {
				userVo.setIdentityType(Integer.valueOf(identityType));
			}
			userVo.setIdentifier(SysUtil.getUser());
			userVo.setTenantCode(SysUtil.getTenantCode());
			return R.success(userService.findUserInfo(userVo));
		} catch (Exception e) {
			throw new CommonException(e, "Failed to get user info.");
		}
	}

	@GetMapping("anonymousUser/findUserByIdentifier/{identifier}")
	@Operation(summary = "根据用户唯一标识获取用户详细信息", description = "根据用户 name 获取用户详细信息")
	public R<UserVo> findUserByIdentifier(@PathVariable String identifier,
			@RequestParam(required = false) Integer identityType, @RequestParam @NotBlank String tenantCode) {
		return R.success(identifyService.findUserByIdentifier(identityType, identifier, tenantCode));
	}

	@GetMapping("userList")
	@Operation(summary = "获取用户列表")
	public R<PageInfo<UserDto>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<UserDto> result = new PageInfo<>();
		List<UserDto> dtoList = Lists.newArrayListWithExpectedSize(pageSize);
		PageInfo<User> page = userService.findPage(condition, pageNum, pageSize);
		List<User> users = page.getList();
		if (CollectionUtils.isNotEmpty(users)) {
			List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
			List<Dept> deptList = deptService.getListByUsers(users);
			List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
			List<UserRole> userRoles = userRoleService.getByUserIds(userIds);
			List<Role> roles = userService.getUsersRoles(userIds);
			Map<Long, String> avatarMap = userService.findUserAvatarUrl(users);
			for (User u : users) {
				UserDto dto = userService.getUserDtoByUserAndUserAuths(u, userAuths, deptList, userRoles, roles);
				dto.setAvatarUrl(avatarMap.get(dto.getId()));
				dtoList.add(dto);
			}
		}
		PageUtil.copyProperties(page, result);
		result.setList(dtoList);
		return R.success(result);
	}

	@PostMapping
	@Operation(summary = "创建用户", description = "创建用户")
	@SgLog(value = "新增用户", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid UserDto userDto) {
		return R.success(identifyService.createUser(userDto) > 0);
	}

	@PutMapping("/{id}")
	@Operation(summary = "更新用户信息", description = "根据用户 id 更新用户的基本信息、角色信息")
	@SgLog(value = "修改用户", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody UserDto userDto) {
		userDto.setTenantCode(TenantHolder.getTenantCode());
		return R.success(identifyService.updateUser(id, userDto));
	}

	@PutMapping("updateInfo")
	@Operation(summary = "更新用户基本信息", description = "根据用户 id 更新用户的基本信息")
	@SgLog(value = "更新用户基本信息", operationType = OperationType.UPDATE)
	public R<Boolean> updateInfo(@RequestBody UserDto userDto) {
		Preconditions.checkNotNull(userDto.getId());
		checkCode(userDto);
		return R.success(userService.updateInfo(userDto));
	}

	@PutMapping("bindPhoneNumber")
	@Operation(summary = "绑定手机号", description = "绑定手机号")
	@SgLog(value = "绑定手机号", operationType = OperationType.UPDATE)
	public R<Boolean> bindPhoneNumber(@RequestBody UserDto userDto) {
		Preconditions.checkNotNull(userDto.getId());
		checkCode(userDto);
		User user = identifyService.findUserByPhone(userDto.getPhone(), userDto.getTenantCode());
		if (user != null && !user.getId().equals(userDto.getId())) {
			log.error("Phone has used by user, phone: {}, user: {}", userDto.getPhone(), user.getName());
			return R.success(Boolean.FALSE);
		}
		return R.success(userService.updateInfo(userDto));
	}

	@PutMapping("anonymousUser/updatePassword")
	@Operation(summary = "修改用户密码", description = "修改用户密码")
	@SgLog(value = "更新用户密码", operationType = OperationType.UPDATE)
	public R<Boolean> updatePassword(@RequestBody UserDto userDto) {
		userDto.setTenantCode(TenantHolder.getTenantCode());
		return R.success(identifyService.updatePassword(userDto) > 0);
	}

	@PostMapping("uploadAvatar")
	@Operation(summary = "上传头像")
	@SgLog(value = "上传头像", operationType = OperationType.UPLOAD)
	public R<Attachment> uploadAvatar(
			@Parameter(description = "要上传的头像", required = true) @RequestParam("file") MultipartFile file)
			throws IOException {
		return R.success(userService.uploadAvatar(file));
	}

	@PostMapping("updateAvatar")
	@Operation(summary = "更新用户头像")
	@SgLog(value = "更新用户头像", operationType = OperationType.UPDATE)
	public R<String> updateAvatar(@RequestBody UserDto userDto) {
		userDto.setTenantCode(TenantHolder.getTenantCode());
		return R.success(userService.updateAvatar(userDto));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除用户", description = "根据 ID 删除用户")
	@SgLog(value = "删除用户", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		User user = userService.get(id);
		UserAuths userAuths = new UserAuths();
		userAuths.setUserId(user.getId());
		userAuths.setTenantCode(TenantHolder.getTenantCode());
		return R.success(userService.delete(user, userAuths) > 0);
	}

	@PostMapping("export")
	@Operation(summary = "导出用户", description = "根据用户 id 导出用户")
	@SgLog(value = "导出用户", operationType = OperationType.EXPORT)
	public void exportUser(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		List<User> users;
		if (ArrayUtils.isNotEmpty(ids)) {
			users = userService.findListById(ids);
		} else {
			// 导出本租户下的全部用户
			User user = new User();
			user.setTenantCode(SysUtil.getTenantCode());
			// TODO users = userService.findList(user);
			users = Lists.newArrayList();
		}
		SgPreconditions.checkCollectionEmpty(users, "Invalid user info.");
		// 查询用户授权信息
		List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
		// 组装数据，转成 dto
		List<UserInfoDto> userInfoDtos = users.stream().map(tempUser -> {
			UserInfoDto userDto = new UserInfoDto();
			userAuths.stream().filter(userAuth -> userAuth.getUserId().equals(tempUser.getId())).findFirst()
					.ifPresent(userAuth -> UserUtils.toUserInfoDto(userDto, tempUser, userAuth));
			return userDto;
		}).collect(Collectors.toList());
		String fileName = "用户信息" + DateUtils.localDateMillisToString(LocalDateTime.now());
		ExcelToolUtil.writeExcel(request, response, UserUtils.convertToExcelModel(userInfoDtos), fileName, "sheet1",
				UserExcelModel.class);
	}

	@PostMapping("import")
	@Operation(summary = "导入数据", description = "导入数据")
	@SgLog(value = "导入用户", operationType = OperationType.IMPORT)
	public R<Boolean> importUser(@Parameter(description = "要上传的文件", required = true) MultipartFile file)
			throws IOException {
		UserImportListener listener = new UserImportListener(userService);
		return R.success(ExcelToolUtil.readExcel(file.getInputStream(), UserExcelModel.class, listener));
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除用户", description = "根据用户 id 批量删除用户")
	@SgLog(value = "批量删除用户", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(ArrayUtils.isNotEmpty(ids) ? userService.deleteAll(ids) > 0 : Boolean.FALSE);
	}

	@PostMapping(value = "findById")
	@Operation(summary = "根据 ID 查询用户", description = "根据 ID 查询用户")
	public R<List<UserVo>> findById(@RequestBody Long[] ids) {
		return R.success(userService.findUserVoListById(ids));
	}

	@Operation(summary = "注册", description = "注册")
	@PostMapping("anonymousUser/register")
	@SgLog(value = "注册用户", operationType = OperationType.INSERT)
	public R<Boolean> register(@RequestBody @Valid UserDto userDto) {
		if (userDto.getIdentityType() == null) {
			userDto.setIdentityType(IdentityType.PASSWORD.getValue());
		}
		return R.success(identifyService.register(userDto));
	}

	@Operation(summary = "检查账号是否存在", description = "检查账号是否存在")
	@GetMapping("anonymousUser/checkExist/{identifier}")
	public R<Boolean> checkExist(@PathVariable("identifier") String identifier, @RequestParam Integer identityType,
			@RequestHeader(CommonConstant.TENANT_CODE_HEADER) String tenantCode) {
		return R.success(userService.checkIdentifierIsExist(identityType, identifier, tenantCode));
	}

	@Operation(summary = "查询用户数量")
	@PostMapping("userCount")
	public R<Integer> userCount(UserVo userVo) {
		return R.success(userService.userCount(userVo));
	}

	@PutMapping("anonymousUser/resetPassword")
	@Operation(summary = "重置密码", description = "根据用户 id 重置密码")
	@SgLog(value = "重置密码", operationType = OperationType.UPDATE)
	public R<Boolean> resetPassword(@RequestBody UserDto userDto) {
		userDto.setTenantCode(TenantHolder.getTenantCode());
		return R.success(identifyService.resetPassword(userDto));
	}

	@PutMapping("anonymousUser/updateLoginInfo")
	@Operation(summary = "更新用户登录信息", description = "根据用户 id 更新用户的登录信息")
	public R<Boolean> updateLoginInfo(@RequestBody UserDto userDto) {
		return R.success(identifyService.updateLoginInfo(userDto));
	}

	private void checkCode(UserDto userDto) {
		if (StringUtils.isNotEmpty(userDto.getCode())) {
			validateCodeService.checkCode(userDto.getCode(), userDto.getPhone());
		}
	}
}
