package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.model.*;
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
import com.github.tangyi.user.service.DeptService;
import com.github.tangyi.user.service.UserAuthsService;
import com.github.tangyi.user.service.UserRoleService;
import com.github.tangyi.user.service.UserService;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

	private final UserRoleService userRoleService;

	private final DeptService deptService;

	private final UserAuthsService userAuthsService;

	@GetMapping("/{id}")
	@Operation(summary = "获取用户信息", description = "根据用户id获取用户详细信息")
	public R<User> get(@PathVariable Long id) {
		return R.success(userService.get(id));
	}

	/**
	 * 获取当前用户信息（角色、权限）
	 */
	@GetMapping("info")
	@Operation(summary = "获取用户信息", description = "获取当前登录用户详细信息")
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
			throw new CommonException(e, "获取当前登录用户详细信息");
		}
	}

	/**
	 * 根据用户唯一标识获取用户详细信息
	 */
	@GetMapping("anonymousUser/findUserByIdentifier/{identifier}")
	@Operation(summary = "获取用户信息", description = "根据用户name获取用户详细信息")
	public R<UserVo> findUserByIdentifier(@PathVariable String identifier,
			@RequestParam(required = false) Integer identityType, @RequestParam @NotBlank String tenantCode) {
		return R.success(userService.findUserByIdentifier(identityType, identifier, tenantCode));
	}

	@GetMapping("userList")
	@Operation(summary = "获取用户列表")
	public R<PageInfo<UserDto>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
		List<UserDto> userDtos = Lists.newArrayList();
		PageInfo<User> page = userService.findPage(condition, pageNum, pageSize);
		List<User> users = page.getList();
		if (CollectionUtils.isNotEmpty(users)) {
			// 查询账户
			List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
			// 查找部门
			List<Dept> deptList = deptService.getListByUsers(users);
			// 查询用户角色关联关系
			List<UserRole> userRoles = userRoleService.getByUserIds(
					users.stream().map(User::getId).collect(Collectors.toList()));
			// 查找角色
			List<Role> finalRoleList = userService.getUsersRoles(users);
			// 头像
			Map<Long, String> avatarUrlMap = userService.findUserAvatarUrl(users);
			users.forEach(tempUser -> {
				UserDto dto = userService.getUserDtoByUserAndUserAuths(tempUser, userAuths, deptList, userRoles,
						finalRoleList);
				dto.setAvatarUrl(avatarUrlMap.get(dto.getId()));
				userDtos.add(dto);
			});
		}
		PageUtil.copyProperties(page, userDtoPageInfo);
		userDtoPageInfo.setList(userDtos);
		return R.success(userDtoPageInfo);
	}

	@PostMapping
	@Operation(summary = "创建用户", description = "创建用户")
	@SgLog(value = "新增用户", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid UserDto userDto) {
		return R.success(userService.createUser(userDto) > 0);
	}

	@PutMapping("/{id:[a-zA-Z0-9,]+}")
	@Operation(summary = "更新用户信息", description = "根据用户id更新用户的基本信息、角色信息")
	@SgLog(value = "修改用户", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody UserDto userDto) {
		return R.success(userService.updateUser(id, userDto));
	}

	@PutMapping("updateInfo")
	@Operation(summary = "更新用户基本信息", description = "根据用户id更新用户的基本信息")
	@SgLog(value = "更新用户基本信息", operationType = OperationType.UPDATE)
	public R<Boolean> update(@RequestBody UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		UserAuths condition = new UserAuths();
		condition.setIdentifier(userDto.getIdentifier());
		condition.setTenantCode(userDto.getTenantCode());
		UserAuths userAuths = userAuthsService.getByIdentifier(condition);
		return R.success(userService.update(user, userAuths) > 0);
	}

	@PutMapping("anonymousUser/updatePassword")
	@Operation(summary = "修改用户密码", description = "修改用户密码")
	@SgLog(value = "更新用户密码", operationType = OperationType.UPDATE)
	public R<Boolean> updatePassword(@RequestBody UserDto userDto) {
		return R.success(userService.updatePassword(userDto) > 0);
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
		return R.success(userService.updateAvatar(userDto));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除用户", description = "根据ID删除用户")
	@SgLog(value = "删除用户", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		User user = userService.get(id);
		UserAuths userAuths = new UserAuths();
		userAuths.setUserId(user.getId());
		userAuths.setTenantCode(TenantHolder.getTenantCode());
		return R.success(userService.delete(user, userAuths) > 0);
	}

	@PostMapping("export")
	@Operation(summary = "导出用户", description = "根据用户id导出用户")
	@SgLog(value = "导出用户", operationType = OperationType.EXPORT)
	public void exportUser(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		try {
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
			SgPreconditions.checkCollectionEmpty(users, "无用户数据");
			// 查询用户授权信息
			List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
			// 组装数据，转成dto
			List<UserInfoDto> userInfoDtos = users.stream().map(tempUser -> {
				UserInfoDto userDto = new UserInfoDto();
				userAuths.stream().filter(userAuth -> userAuth.getUserId().equals(tempUser.getId())).findFirst()
						.ifPresent(userAuth -> UserUtils.toUserInfoDto(userDto, tempUser, userAuth));
				return userDto;
			}).collect(Collectors.toList());
			String fileName = "用户信息" + DateUtils.localDateMillisToString(LocalDateTime.now());
			ExcelToolUtil.writeExcel(request, response, UserUtils.convertToExcelModel(userInfoDtos), fileName, "sheet1",
					UserExcelModel.class);
		} catch (Exception e) {
			throw new CommonException(e, "export user data failed");
		}
	}

	@PostMapping("import")
	@Operation(summary = "导入数据", description = "导入数据")
	@SgLog(value = "导入用户", operationType = OperationType.IMPORT)
	public R<Boolean> importUser(@Parameter(description = "要上传的文件", required = true) MultipartFile file)
			throws IOException {
		return R.success(ExcelToolUtil.readExcel(file.getInputStream(), UserExcelModel.class,
				new UserImportListener(userService)));
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除用户", description = "根据用户id批量删除用户")
	@SgLog(value = "批量删除用户", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(ArrayUtils.isNotEmpty(ids) ? userService.deleteAll(ids) > 0 : Boolean.FALSE);
	}

	@PostMapping(value = "findById")
	@Operation(summary = "根据ID查询用户", description = "根据ID查询用户")
	public R<List<UserVo>> findById(@RequestBody Long[] ids) {
		return R.success(userService.findUserVoListById(ids));
	}

	@Operation(summary = "注册", description = "注册")
	@PostMapping("anonymousUser/register")
	@SgLog(value = "注册用户", operationType = OperationType.INSERT)
	public R<Boolean> register(@RequestBody @Valid UserDto userDto) {
		return R.success(userService.register(userDto));
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
	@Operation(summary = "重置密码", description = "根据用户id重置密码")
	@SgLog(value = "重置密码", operationType = OperationType.UPDATE)
	public R<Boolean> resetPassword(@RequestBody UserDto userDto) {
		userDto.setTenantCode(TenantHolder.getTenantCode());
		return R.success(userService.resetPassword(userDto));
	}

	@PutMapping("anonymousUser/updateLoginInfo")
	@Operation(summary = "更新用户登录信息", description = "根据用户id更新用户的登录信息")
	public R<Boolean> updateLoginInfo(@RequestBody UserDto userDto) {
		return R.success(userService.updateLoginInfo(userDto));
	}
}
