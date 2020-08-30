package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.module.*;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.user.excel.listener.UserImportListener;
import com.github.tangyi.user.excel.model.UserExcelModel;
import com.github.tangyi.user.service.DeptService;
import com.github.tangyi.user.service.UserAuthsService;
import com.github.tangyi.user.service.UserRoleService;
import com.github.tangyi.user.service.UserService;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import com.github.tangyi.common.log.annotation.Log;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangyi
 * @date 2018-08-25 16:20
 */
@Slf4j
@AllArgsConstructor
@Api("用户信息管理")
@RestController
@RequestMapping(value = "/v1/user")
public class UserController extends BaseController {

	private final UserService userService;

	private final UserRoleService userRoleService;

	private final DeptService deptService;

	private final UserAuthsService userAuthsService;

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return ResponseBean
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "获取用户信息", notes = "根据用户id获取用户详细信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
	public ResponseBean<User> user(@PathVariable Long id) {
		return new ResponseBean<>(userService.get(id));
	}

	/**
	 * 获取当前用户信息（角色、权限）
	 *
	 * @return 用户名
	 */
	@GetMapping("info")
	@ApiOperation(value = "获取用户信息", notes = "获取当前登录用户详细信息")
	@ApiImplicitParam(name = "identityType", value = "账号类型", required = true, dataType = "String")
	public ResponseBean<UserInfoDto> userInfo(@RequestParam(required = false) String identityType) {
		try {
			UserVo userVo = new UserVo();
			if (StringUtils.isNotEmpty(identityType))
				userVo.setIdentityType(Integer.valueOf(identityType));
			userVo.setIdentifier(SysUtil.getUser());
			userVo.setTenantCode(SysUtil.getTenantCode());
			return new ResponseBean<>(userService.findUserInfo(userVo));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException("获取当前登录用户详细信息");
		}
	}

	/**
	 * 根据用户唯一标识获取用户详细信息
	 *
	 * @param identifier   identifier
	 * @param identityType identityType
	 * @param tenantCode   tenantCode
	 * @return ResponseBean
	 */
	@GetMapping("anonymousUser/findUserByIdentifier/{identifier}")
	@ApiOperation(value = "获取用户信息", notes = "根据用户name获取用户详细信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "identityType", value = "用户授权类型", dataType = "Integer"),
			@ApiImplicitParam(name = "tenantCode", value = "租户标识", required = true, dataType = "String"),})
	public ResponseBean<UserVo> findUserByIdentifier(@PathVariable String identifier,
			@RequestParam(required = false) Integer identityType, @RequestParam @NotBlank String tenantCode) {
		return new ResponseBean<>(userService.findUserByIdentifier(identityType, identifier, tenantCode));
	}

	/**
	 * 获取分页数据
	 *
	 * @param pageNum  pageNum
	 * @param pageSize pageSize
	 * @param sort     sort
	 * @param order    order
	 * @param userVo   userVo
	 * @return PageInfo
	 * @author tangyi
	 * @date 2018/8/26 22:56
	 */
	@GetMapping("userList")
	@ApiOperation(value = "获取用户列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
			@ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
			@ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
			@ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
			@ApiImplicitParam(name = "userVo", value = "用户信息", dataType = "UserVo")})
	public PageInfo<UserDto> userList(
			@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
			@RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
			@RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
			@RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
			@RequestParam(value = "name", required = false, defaultValue = "") String name, UserVo userVo) {
		PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
		List<UserDto> userDtos = Lists.newArrayList();
		userVo.setTenantCode(SysUtil.getTenantCode());
		User user = new User();
		BeanUtils.copyProperties(userVo, user);
		user.setName(name);
		PageInfo<User> page = userService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), user);
		List<User> users = page.getList();
		if (CollectionUtils.isNotEmpty(users)) {
			// 批量查询账户
			List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
			// 批量查找部门
			List<Dept> deptList = deptService.getListByUsers(users);
			// 查询用户角色关联关系
			List<UserRole> userRoles = userRoleService
					.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
			// 批量查找角色
			List<Role> finalRoleList = userService.getUsersRoles(users);
			// 组装数据
			users.forEach(tempUser -> userDtos.add(userService
					.getUserDtoByUserAndUserAuths(tempUser, userAuths, deptList, userRoles, finalRoleList)));
		}
		PageUtil.copyProperties(page, userDtoPageInfo);
		userDtoPageInfo.setList(userDtos);
		return userDtoPageInfo;
	}

	/**
	 * 创建用户
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2018/8/26 14:34
	 */
	@PostMapping
	@ApiOperation(value = "创建用户", notes = "创建用户")
	@ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
	@Log("新增用户")
	public ResponseBean<Boolean> addUser(@RequestBody @Valid UserDto userDto) {
		return new ResponseBean<>(userService.createUser(userDto) > 0);
	}

	/**
	 * 更新用户
	 *
	 * @param id      id
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2018/8/26 15:06
	 */
	@PutMapping("/{id:[a-zA-Z0-9,]+}")
	@ApiOperation(value = "更新用户信息", notes = "根据用户id更新用户的基本信息、角色信息")
	@ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
	@Log("修改用户")
	public ResponseBean<Boolean> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
		try {
			return new ResponseBean<>(userService.updateUser(id, userDto));
		} catch (Exception e) {
			log.error("Update user failed", e);
			throw new CommonException("Update user failed");
		}
	}

	/**
	 * 更新用户的基本信息
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2018/10/30 10:06
	 */
	@PutMapping("updateInfo")
	@ApiOperation(value = "更新用户基本信息", notes = "根据用户id更新用户的基本信息")
	@ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
	@Log("更新用户基本信息")
	public ResponseBean<Boolean> updateInfo(@RequestBody UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setCommonValue();
		return new ResponseBean<>(userService.update(user) > 0);
	}

	/**
	 * 修改密码
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/06/21 20:09
	 */
	@PutMapping("anonymousUser/updatePassword")
	@ApiOperation(value = "修改用户密码", notes = "修改用户密码")
	@ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
	@Log("更新用户密码")
	public ResponseBean<Boolean> updatePassword(@RequestBody UserDto userDto) {
		return new ResponseBean<>(userService.updatePassword(userDto) > 0);
	}

	/**
	 * 更新头像
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/06/21 18:08
	 */
	@PutMapping("updateAvatar")
	@ApiOperation(value = "更新用户头像", notes = "根据用户id更新用户的头像信息")
	@ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
	@Log("更新用户头像")
	public ResponseBean<Boolean> updateAvatar(@RequestBody UserDto userDto) {
		return new ResponseBean<>(userService.updateAvatar(userDto) > 0);
	}

	/**
	 * 删除用户
	 *
	 * @param id id
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2018/8/26 15:28
	 */
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除用户", notes = "根据ID删除用户")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
	@Log("删除用户")
	public ResponseBean<Boolean> deleteUser(@PathVariable Long id) {
		try {
			User user = userService.get(id);
			user.setCommonValue();
			return new ResponseBean<>(userService.delete(user) > 0);
		} catch (Exception e) {
			log.error("Delete user failed: {}", e.getMessage(), e);
			throw new CommonException("Delete user failed");
		}
	}

	/**
	 * 导出
	 *
	 * @param ids ids
	 * @author tangyi
	 * @date 2018/11/26 22:11
	 */
	@PostMapping("export")
	@ApiOperation(value = "导出用户", notes = "根据用户id导出用户")
	@ApiImplicitParam(name = "userVo", value = "用户信息", required = true, dataType = "UserVo")
	@Log("导出用户")
	public void exportUser(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<User> users;
			if (ArrayUtils.isNotEmpty(ids)) {
				users = userService.findListById(ids);
			} else {
				// 导出本租户下的全部用户
				User user = new User();
				user.setTenantCode(SysUtil.getTenantCode());
				users = userService.findList(user);
			}
			if (CollectionUtils.isEmpty(users))
				throw new CommonException("无用户数据.");
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
			log.error("Export user data failed", e);
			throw new CommonException("Export user data failed");
		}
	}

	/**
	 * 导入数据
	 *
	 * @param file file
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2018/11/28 12:44
	 */
	@PostMapping("import")
	@ApiOperation(value = "导入数据", notes = "导入数据")
	@Log("导入用户")
	public ResponseBean<Boolean> importUser(@ApiParam(value = "要上传的文件", required = true) MultipartFile file,
			HttpServletRequest request) {
		try {
			return new ResponseBean<>(ExcelToolUtil
					.readExcel(file.getInputStream(), UserExcelModel.class, new UserImportListener(userService)));
		} catch (Exception e) {
			log.error("Import user failed", e);
			throw new CommonException("Import user failed");
		}
	}

	/**
	 * 批量删除
	 *
	 * @param ids ids
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2018/12/4 9:58
	 */
	@PostMapping("deleteAll")
	@ApiOperation(value = "批量删除用户", notes = "根据用户id批量删除用户")
	@ApiImplicitParam(name = "ids", value = "用户信息", dataType = "Long")
	@Log("批量删除用户")
	public ResponseBean<Boolean> deleteAllUsers(@RequestBody Long[] ids) {
		try {
			boolean success = Boolean.FALSE;
			if (ArrayUtils.isNotEmpty(ids))
				success = userService.deleteAll(ids) > 0;
			return new ResponseBean<>(success);
		} catch (Exception e) {
			log.error("Delete user failed", e);
			throw new CommonException("Delete user failed");
		}
	}

	/**
	 * 根据ID查询
	 *
	 * @param ids ids
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2018/12/31 21:16
	 */
	@PostMapping(value = "findById")
	@ApiOperation(value = "根据ID查询用户", notes = "根据ID查询用户")
	@ApiImplicitParam(name = "ids", value = "用户ID", required = true, paramType = "Long")
	public ResponseBean<List<UserVo>> findById(@RequestBody Long[] ids) {
		return new ResponseBean<>(userService.findUserVoListById(ids));
	}

	/**
	 * 注册
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/01/10 22:35
	 */
	@ApiOperation(value = "注册", notes = "注册")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "grant_type", value = "授权类型（password、mobile）", required = true, defaultValue = "password", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "randomStr", value = "随机数", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query")})
	@PostMapping("anonymousUser/register")
	@Log("注册用户")
	public ResponseBean<Boolean> register(@RequestBody @Valid UserDto userDto) {
		return new ResponseBean<>(userService.register(userDto));
	}

	/**
	 * 检查账号是否存在
	 *
	 * @param identityType identityType
	 * @param identifier   identifier
	 * @param tenantCode   tenantCode
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/04/23 15:35
	 */
	@ApiOperation(value = "检查账号是否存在", notes = "检查账号是否存在")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "identityType", value = "用户唯一标识类型", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "tenantCode", value = "租户标识", required = true, dataType = "String"),})
	@GetMapping("anonymousUser/checkExist/{identifier}")
	public ResponseBean<Boolean> checkExist(@PathVariable("identifier") String identifier,
			@RequestParam Integer identityType, @RequestHeader(CommonConstant.TENANT_CODE_HEADER) String tenantCode) {
		return new ResponseBean<>(userService.checkIdentifierIsExist(identityType, identifier, tenantCode));
	}

	/**
	 * 查询用户数量
	 *
	 * @param userVo userVo
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/05/09 22:09
	 */
	@PostMapping("userCount")
	public ResponseBean<Integer> userCount(UserVo userVo) {
		return new ResponseBean<>(userService.userCount(userVo));
	}

	/**
	 * 重置密码
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/6/7 12:00
	 */
	@PutMapping("anonymousUser/resetPassword")
	@ApiOperation(value = "重置密码", notes = "根据用户id重置密码")
	@ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
	@Log("重置密码")
	public ResponseBean<Boolean> resetPassword(@RequestBody UserDto userDto) {
		return new ResponseBean<>(userService.resetPassword(userDto));
	}

	/**
	 * 更新用户的基本信息
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2020/02/29 16:55
	 */
	@PutMapping("anonymousUser/updateLoginInfo")
	@ApiOperation(value = "更新用户登录信息", notes = "根据用户id更新用户的登录信息")
	@ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
	public ResponseBean<Boolean> updateLoginInfo(@RequestBody UserDto userDto) {
		Boolean success = Boolean.FALSE;
		if (StringUtils.isNotBlank(userDto.getIdentifier())) {
			UserAuths userAuths = new UserAuths();
			userAuths.setIdentifier(userDto.getIdentifier());
			userAuths = userAuthsService.getByIdentifier(userAuths);
			if (userAuths != null) {
				User user = new User();
				user.setId(userAuths.getUserId());
				user.setLoginTime(userDto.getLoginTime());
				user.setModifyDate(userDto.getLoginTime());
				user.setModifier(userAuths.getIdentifier());
				success = userService.update(user) > 0;
			}
		}
		return new ResponseBean<>(success);
	}
}
