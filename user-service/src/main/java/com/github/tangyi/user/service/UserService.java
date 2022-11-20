package com.github.tangyi.user.service;

import com.github.tangyi.api.user.constant.MenuConstant;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.model.*;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.AesUtil;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.enums.AttachTypeEnum;
import com.github.tangyi.user.mapper.RoleMapper;
import com.github.tangyi.user.mapper.UserMapper;
import com.github.tangyi.user.mapper.UserRoleMapper;
import com.github.tangyi.user.utils.MenuUtil;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户service实现
 *
 * @author tangyi
 * @date 2018-08-25 16:17
 */
@AllArgsConstructor
@Slf4j
@Service
public class UserService extends CrudService<UserMapper, User> {

	private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

	public static final String DEFAULT_AVATAR_URL = EnvUtils.getValue("DEFAULT_AVATAR_URL", "");

	private final UserRoleMapper userRoleMapper;

	private final RoleMapper roleMapper;

	private final MenuService menuService;

	private final RedisTemplate redisTemplate;

	private final AttachmentService attachmentService;

	private final QiNiuService qiNiuService;

	private final SysProperties sysProperties;

	private final UserAuthsService userAuthsService;

	/**
	 * 新增用户
	 */
	@Transactional
	public int createUser(UserDto userDto) {
		userDto.setTenantCode(SysUtil.getTenantCode());
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setCommonValue();
		// 保存父子账号关系
		UserAuths current = findUserAuthsByIdentifier(userDto.getIdentityType(), SysUtil.getUser(),
				SysUtil.getTenantCode());
		user.setParentUid(current.getUserId());
		int update = insert(user);
		if (user.getId() != null) {
			createUserAuths(userDto, user);
			createUserRole(userDto, user);
		}
		return update;
	}

	@Transactional
	public UserAuths createUserAuths(UserDto userDto, User user) {
		UserAuths userAuths = new UserAuths();
		userAuths.setCommonValue(user.getCreator(), user.getTenantCode());
		userAuths.setUserId(user.getId());
		userAuths.setIdentifier(userDto.getIdentifier());
		// 默认为账号密码类型
		if (userDto.getIdentityType() == null) {
			userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
		}
		// 设置默认密码
		if (StringUtils.isBlank(userDto.getCredential())) {
			userDto.setCredential(CommonConstant.DEFAULT_PASSWORD);
		}
		userAuths.setCredential(encoder.encode(userDto.getCredential()));
		userAuthsService.insert(userAuths);
		return userAuths;
	}

	@Transactional
	public void createUserRole(UserDto userDto, User user) {
		if (CollectionUtils.isEmpty(user.getRole())) {
			// 分配默认角色
			defaultRole(user, user.getTenantCode(), userDto.getIdentifier());
			return;
		}
		createUserRole(user.getId(), user.getRole());
	}

	@Transactional
	public void createUserRole(Long userId, List<Long> roleIds) {
		roleIds.forEach(roleId -> {
			UserRole ur = new UserRole();
			ur.setNewRecord(true);
			ur.setCommonValue();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			userRoleMapper.insert(ur);
		});
	}

	/**
	 * 根据用户唯一标识获取用户详细信息
	 *
	 * @param identityType identityType
	 * @param identifier   identifier
	 * @param tenantCode   tenantCode
	 * @return UserVo
	 * @author tangyi
	 * @date 2019/07/03 13:00:39
	 */
	@Cacheable(value = UserCacheName.USER_VO, key = "#identifier")
	public UserVo findUserByIdentifier(Integer identityType, String identifier, String tenantCode) {
		UserAuths userAuths = findUserAuthsByIdentifier(identityType, identifier, tenantCode);
		if (userAuths == null) {
			return null;
		}
		User user = this.get(userAuths.getUserId());
		if (user == null) {
			return null;
		}
		List<Role> roles = this.getUserRoles(userAuths);
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		BeanUtils.copyProperties(userAuths, userVo);
		userVo.setRoleList(UserUtils.rolesToVo(roles));
		userVo.setUserId(user.getId());
		return userVo;
	}

	public UserAuths findUserAuthsByIdentifier(String identifier) {
		return findUserAuthsByIdentifier(null, identifier, SysUtil.getTenantCode());
	}

	public UserAuths findUserAuthsByIdentifier(String identifier, String tenantCode) {
		return findUserAuthsByIdentifier(null, identifier, tenantCode);
	}

	public UserAuths findUserAuthsByIdentifier(Integer identityType, String identifier, String tenantCode) {
		UserAuths condition = new UserAuths();
		condition.setIdentifier(identifier);
		if (identityType != null) {
			condition.setIdentityType(IdentityType.matchByType(identityType).getValue());
		}
		condition.setTenantCode(tenantCode);
		return userAuthsService.getByIdentifier(condition);
	}

	/**
	 * 获取用户信息，包括头像、角色、权限信息
	 *
	 * @param userVo userVo
	 * @return User
	 * @author tangyi
	 * @date 2018/9/11 23:44
	 */
	@Cacheable(value = UserCacheName.USER_DTO, key = "#userVo.identifier")
	public UserInfoDto findUserInfo(UserVo userVo) {
		// 返回结果
		UserInfoDto userInfoDto = new UserInfoDto();
		String tenantCode = userVo.getTenantCode();
		String identifier = userVo.getIdentifier();
		// 根据唯一标识查询账号信息
		final UserAuths condition = new UserAuths();
		Optional.ofNullable(userVo.getIdentityType()).ifPresent(condition::setIdentityType);
		condition.setIdentifier(userVo.getIdentifier());
		condition.setTenantCode(tenantCode);
		UserAuths userAuths = userAuthsService.getByIdentifier(condition);
		SgPreconditions.checkNull(userAuths, "identifier " + identifier + "does not exist");
		// 根据用户id查询用户详细信息
		User user = this.get(userAuths.getUserId());
		SgPreconditions.checkNull(user, "get user info failed");
		// 查询用户的角色信息
		List<Role> roles = getUserRoles(userAuths);
		// 根据角色查询权限
		Set<String> permissions = getUserPermissions(user, identifier, roles);
		userInfoDto.setRoles(roles.stream().map(Role::getRoleCode).toArray(String[]::new));
		userInfoDto.setPermissions(permissions.toArray(new String[0]));
		UserUtils.toUserInfoDto(userInfoDto, user, userAuths);
		// 头像信息
		getUserAvatar(userInfoDto, user);
		// 用户的菜单
		userInfoDto.setMenus(menuService.findUserMenu(MenuConstant.MENU_TYPE_MENU, identifier, tenantCode, true));
		return userInfoDto;
	}

	/**
	 * 根据指定角色集合，查询用户权限数组
	 *
	 * @param user       user
	 * @param identifier identifier
	 * @param roles      roles
	 * @return Set
	 * @author tangyi
	 * @date 2019/07/04 00:14:44
	 */
	@Cacheable(value = UserCacheName.USER_PERMISSION, key = "#identifier")
	public Set<String> getUserPermissions(User user, String identifier, List<Role> roles) {
		List<Menu> menuList = Lists.newArrayList();
		// 超级管理员
		if (SysUtil.isAdmin(identifier)) {
			Menu condition = new Menu();
			condition.setTenantCode(user.getTenantCode());
			condition.setType(MenuConstant.MENU_TYPE_PERMISSION);
			menuList = menuService.findList(condition);
		} else {
			if (CollectionUtils.isNotEmpty(roles)) {
				for (Role role : roles) {
					// 根据角色查找菜单
					List<Menu> roleMenuList = menuService.findMenuByRole(role.getRoleCode(), user.getTenantCode());
					if (CollectionUtils.isNotEmpty(roleMenuList)) {
						menuList.addAll(roleMenuList);
					}
				}
			}
		}
		return CollectionUtils.isNotEmpty(menuList) ?
				menuList.stream().filter(MenuUtil::isPermission).map(Menu::getPermission).collect(Collectors.toSet()) :
				Sets.newHashSet();
	}

	/**
	 * 获取用户的角色
	 *
	 * @param userAuths userAuths
	 * @return List
	 * @author tangyi
	 * @date 2019/07/03 12:03:17
	 */
	@Cacheable(value = UserCacheName.USER_ROLE, key = "#userAuths.identifier")
	public List<Role> getUserRoles(UserAuths userAuths) {
		List<UserRole> userRoles = userRoleMapper.getByUserId(userAuths.getUserId());
		if (CollectionUtils.isNotEmpty(userRoles)) {
			return roleMapper.findListById(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
		}
		return Lists.newArrayList();
	}

	/**
	 * 批量查询用户的角色
	 *
	 * @param users users
	 * @return List
	 * @author tangyi
	 * @date 2019/07/03 12:13:38
	 */
	public List<Role> getUsersRoles(List<User> users) {
		List<UserRole> userRoles = userRoleMapper.getByUserIds(
				users.stream().map(User::getId).collect(Collectors.toList()));
		if (CollectionUtils.isNotEmpty(userRoles)) {
			return roleMapper.findListById(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
		}
		return Lists.newArrayList();
	}

	/**
	 * 更新用户
	 * @param id id
	 * @param userDto userDto
	 * @return int
	 * @author tangyi
	 * @date 2018/8/26 15:15
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_ROLE, UserCacheName.USER_PERMISSION,
			UserCacheName.USER_AUTHS}, key = "#userDto.identifier")
	public boolean updateUser(Long id, UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setId(id);
		user.setCommonValue();
		// 更新用户信息
		this.dao.updateByPrimaryKeySelective(user);
		// 更新用户角色关系
		UserRole sysUserRole = new UserRole();
		sysUserRole.setUserId(user.getId());
		// 删除原有的角色信息
		userRoleMapper.delete(sysUserRole);
		if (CollectionUtils.isNotEmpty(userDto.getRole())) {
			createUserRole(user.getId(), userDto.getRole());
		}
		return Boolean.TRUE;
	}

	/**
	 * 更新用户基本信息
	 *
	 * @param user user
	 * @return int
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, key = "#userAuths.identifier")
	public int update(User user, UserAuths userAuths) {
		user.setCommonValue();
		return this.dao.updateByPrimaryKeySelective(user);
	}

	/**
	 * 更新密码
	 *
	 * @param userDto userDto
	 * @return int
	 * @author tangyi
	 * @date 2019/07/03 12:26:24
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, key = "#userDto.identifier")
	public int updatePassword(UserDto userDto) {
		userDto.setTenantCode(SysUtil.getTenantCode());
		SgPreconditions.checkBlank(userDto.getNewPassword(), "新密码不能为空");
		SgPreconditions.checkBlank(userDto.getIdentifier(), "用户名不能为空");
		UserAuths userAuths = findUserAuthsByIdentifier(null, userDto.getIdentifier(), userDto.getTenantCode());
		SgPreconditions.checkNull(userAuths, "账号不存在");
		if (!encoder.matches(userDto.getOldPassword(), userAuths.getCredential())) {
			throw new CommonException("新旧密码不匹配");
		} else {
			userAuths.setCredential(encoder.encode(userDto.getNewPassword()));
			return userAuthsService.update(userAuths);
		}
	}

	/**
	 * 更新头像
	 *
	 * @param file file
	 * @return Attachment
	 * @author tangyi
	 * @date 2019/06/21 18:14
	 */
	public Attachment uploadAvatar(MultipartFile file) throws IOException {
		return qiNiuService.upload(file, AttachTypeEnum.AVATAR.getValue(), SysUtil.getUser(), SysUtil.getTenantCode());
	}

	/**
	 * 更新头像
	 *
	 * @param userDto userDto
	 * @return String
	 * @author tangyi
	 * @date 2019/06/21 18:14
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, key = "#userDto.identifier")
	public String updateAvatar(UserDto userDto) {
		User user = this.get(userDto.getId());
		SgPreconditions.checkNull(user, "用户不存在");
		if (user.getAvatarId() != null) {
			Attachment attach = attachmentService.get(user.getAvatarId());
			if (attach != null) {
				attachmentService.delete(attach);
			}
		}
		user.setAvatarId(userDto.getAvatarId());
		update(user);
		return qiNiuService.getPreviewUrl(userDto.getAvatarId());
	}

	/**
	 * 查询账号是否存在
	 *
	 * @param identityType identityType
	 * @param identifier   identifier
	 * @param tenantCode   tenantCode
	 * @return boolean
	 * @author tangyi
	 * @date 2019/07/03 13:23:10
	 */
	public boolean checkIdentifierIsExist(Integer identityType, String identifier, String tenantCode) {
		UserAuths userAuths = new UserAuths();
		userAuths.setIdentifier(identifier);
		userAuths.setIdentityType(identityType);
		userAuths.setTenantCode(tenantCode);
		return userAuthsService.getByIdentifier(userAuths) != null;
	}

	/**
	 * 保存验证码
	 *
	 * @param random    random
	 * @param imageCode imageCode
	 * @author tangyi
	 * @date 2018/9/14 20:12
	 */
	public void saveImageCode(String random, String imageCode) {
		redisTemplate.opsForValue()
				.set(CommonConstant.DEFAULT_CODE_KEY + random, imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE,
						TimeUnit.SECONDS);
	}

	/**
	 * 删除用户
	 *
	 * @param userAuths userAuths
	 * @return int
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, key = "#userAuths.identifier")
	public int delete(User user, UserAuths userAuths) {
		userRoleMapper.deleteByUserId(userAuths.getUserId());
		userAuthsService.deleteByUserId(userAuths);
		user.setCommonValue();
		return super.delete(user);
	}

	/**
	 * 批量删除用户
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019/07/04 11:44:45
	 */
	@Transactional
	@Override
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, allEntries = true)
	public int deleteAll(Long[] ids) {
		String currentUser = SysUtil.getUser(), tenantCode = SysUtil.getTenantCode();
		for (Long id : ids) {
			// 删除用户角色关系
			userRoleMapper.deleteByUserId(id);
			// 删除用户授权信息
			UserAuths userAuths = new UserAuths();
			userAuths.setNewRecord(false);
			userAuths.setUserId(id);
			userAuths.setCommonValue(currentUser, tenantCode);
			userAuthsService.deleteByUserId(userAuths);
		}
		return super.deleteAll(ids);
	}

	/**
	 * 查询用户数量
	 *
	 * @param userVo userVo
	 * @return int
	 * @author tangyi
	 * @date 2019/05/09 22:10
	 */
	public Integer userCount(UserVo userVo) {
		return this.dao.userCount(userVo);
	}

	/**
	 * 用户头像
	 *
	 * @param userInfoDto userInfoDto
	 * @param user        user
	 * @author tangyi
	 * @date 2019/06/21 17:49
	 */
	private void getUserAvatar(UserInfoDto userInfoDto, User user) {
		try {
			if (user.getAvatarId() != null && user.getAvatarId() != 0L) {
				userInfoDto.setAvatar(qiNiuService.getPreviewUrl(user.getAvatarId()));
			} else {
				userInfoDto.setAvatar(DEFAULT_AVATAR_URL);
			}
		} catch (Exception e) {
			log.error("getUserAvatar failed", e);
		}
	}

	/**
	 * 重置密码
	 *
	 * @param userDto userDto
	 * @return int
	 * @author tangyi
	 * @date 2019/07/03 13:27:39
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, key = "#userDto.identifier")
	public boolean resetPassword(UserDto userDto) {
		UserAuths userAuths = findUserAuthsByIdentifier(null, userDto.getIdentifier(), userDto.getTenantCode());
		SgPreconditions.checkNull(userAuths, "账号不存在");
		// reset password
		userAuths.setCredential(encoder.encode(CommonConstant.DEFAULT_PASSWORD));
		return userAuthsService.update(userAuths) > 0;
	}

	/**
	 * 注册
	 *
	 * @param userDto userDto
	 * @return boolean
	 * @author tangyi
	 * @date 2019/07/03 13:30:03
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, key = "#userDto.identifier")
	public boolean register(UserDto userDto) {
		boolean success = false;
		if (userDto.getIdentityType() == null) {
			userDto.setIdentityType(IdentityType.PASSWORD.getValue());
		}
		// 解密
		String password = decryptCredential(userDto.getCredential(), userDto.getIdentityType());
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		// 初始化用户名，系统编号，租户编号
		user.setCommonValue(userDto.getIdentifier(), SysUtil.getTenantCode());
		user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
		// 初始化头像
		if (StringUtils.isNotBlank(userDto.getAvatarUrl())) {
			Attachment attachment = new Attachment();
			attachment.setCommonValue(userDto.getIdentifier(), SysUtil.getTenantCode());
			attachment.setUrl(userDto.getAvatarUrl());
			attachment.setGroupCode(AttachTypeEnum.AVATAR.getValue());
			if (attachmentService.insert(attachment) > 0) {
				user.setAvatarId(attachment.getId());
			}
		}
		// 保存用户基本信息
		if (this.insert(user) > 0) {
			// 保存账号信息
			UserAuths userAuths = new UserAuths();
			userAuths.setCommonValue(userDto.getIdentifier(), user.getTenantCode());
			userAuths.setUserId(user.getId());
			userAuths.setIdentifier(userDto.getIdentifier());
			if (userDto.getIdentityType() != null) {
				userAuths.setIdentityType(userDto.getIdentityType());
			}
			// 设置密码
			userAuths.setCredential(encoder.encode(password));
			userAuthsService.insert(userAuths);
			// 分配默认角色
			success = defaultRole(user, userDto.getTenantCode(), userDto.getIdentifier());
		}
		return success;
	}

	/**
	 * 解密密码
	 *
	 * @param encoded encoded
	 * @return String
	 * @author tangyi
	 * @date 2019/07/05 12:39:13
	 */
	private String decryptCredential(String encoded, Integer identityType) {
		// 返回默认密码
		if (StringUtils.isBlank(encoded)) {
			return CommonConstant.DEFAULT_PASSWORD;
		}
		// 微信、手机号注册不需要解密
		if (IdentityType.WE_CHAT.getValue().equals(identityType) || IdentityType.PHONE_NUMBER.getValue()
				.equals(identityType)) {
			return encoded;
		}
		// 解密密码
		try {
			encoded = AesUtil.decryptAES(encoded, sysProperties.getKey()).trim();
			log.info("decrypt result: {}", encoded);
		} catch (Exception e) {
			throw new CommonException(e, "Decrypt failed");
		}
		return encoded;
	}

	/**
	 * 分配默认角色
	 *
	 * @param user user
	 * @param tenantCode tenantCode
	 * @param identifier identifier
	 * @return boolean
	 * @author tangyi
	 * @date 2019/07/04 11:30:27
	 */
	@Transactional
	public boolean defaultRole(User user, String tenantCode, String identifier) {
		// 设置默认的租户标识
		if (StringUtils.isEmpty(tenantCode)) {
			tenantCode = TenantConstant.DEFAULT_TENANT_CODE;
		}
		Role defaultRole = roleMapper.findDefaultRole(tenantCode);
		if (defaultRole != null) {
			UserRole userRole = new UserRole();
			userRole.setCommonValue(identifier, tenantCode);
			userRole.setUserId(user.getId());
			userRole.setRoleId(defaultRole.getId());
			return userRoleMapper.insert(userRole) > 0;
		}
		return false;
	}

	/**
	 * 根据用户id批量查询UserVo
	 *
	 * @param ids ids
	 * @return List
	 * @author tangyi
	 * @date 2019/07/03 13:59:32
	 */
	public List<UserVo> findUserVoListById(Long[] ids) {
		List<UserVo> userVos = Lists.newArrayListWithExpectedSize(ids.length);
		List<User> users = this.findListById(ids);
		if (CollectionUtils.isNotEmpty(users)) {
			for (User tempUser : users) {
				UserVo tempUserVo = new UserVo();
				BeanUtils.copyProperties(tempUser, tempUserVo);
				if (tempUser.getAvatarId() != null) {
					tempUserVo.setAvatarUrl(qiNiuService.getPreviewUrl(tempUser.getAvatarId()));
				}
				userVos.add(tempUserVo);
			}
		}
		return userVos;
	}

	/**
	 * 组装用户数据，包括账号、部门、角色
	 *
	 * @param tempUser      tempUser
	 * @param userAuths     userAuths
	 * @param deptList      deptList
	 * @param userRoles     userRoles
	 * @param finalRoleList finalRoleList
	 * @return UserDto
	 * @author tangyi
	 * @date 2019/07/03 22:35:50
	 */
	public UserDto getUserDtoByUserAndUserAuths(User tempUser, List<UserAuths> userAuths, List<Dept> deptList,
			List<UserRole> userRoles, List<Role> finalRoleList) {
		UserDto userDto = new UserDto(tempUser);
		// 设置账号信息
		if (CollectionUtils.isNotEmpty(userAuths)) {
			userAuths.stream().filter(tempUserAuths -> tempUserAuths.getUserId().equals(tempUser.getId())).findFirst()
					.ifPresent(tempUserAuths -> userDto.setIdentifier(tempUserAuths.getIdentifier()));
		}
		// 设置部门信息
		if (CollectionUtils.isNotEmpty(deptList)) {
			// 用户所属部门
			deptList.stream().filter(tempDept -> tempDept.getId().equals(tempUser.getDeptId())).findFirst()
					.ifPresent(userDept -> {
						userDto.setDeptId(userDept.getId());
						userDto.setDeptName(userDept.getDeptName());
					});
		}
		// 设置角色信息
		if (CollectionUtils.isNotEmpty(userRoles)) {
			List<String> roleNames = Lists.newArrayList();
			List<Long> roleIds = Lists.newArrayList();
			userRoles.stream().filter(tempUserRole -> tempUser.getId().equals(tempUserRole.getUserId())).forEach(
					tempUserRole -> finalRoleList.stream().filter(role -> role.getId().equals(tempUserRole.getRoleId()))
							.forEach(role -> {
								roleNames.add(role.getRoleName());
								roleIds.add(role.getId());
							}));
			userDto.setRoleNames(StringUtils.join(roleNames, CommonConstant.COMMA));
			userDto.setRole(roleIds);
		}
		return userDto;
	}

	/**
	 * 导入用户
	 *
	 * @param userInfoDtos userInfoDtos
	 * @return boolean
	 * @author tangyi
	 * @date 2019/07/04 12:46:01
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.USER, UserCacheName.USER_VO, UserCacheName.USER_DTO, UserCacheName.USER_ROLE,
			UserCacheName.USER_PERMISSION, UserCacheName.USER_AUTHS}, allEntries = true)
	public boolean importUsers(List<UserInfoDto> userInfoDtos) {
		String currentUser = SysUtil.getUser(), tenantCode = SysUtil.getTenantCode();
		Date currentDate = DateUtils.asDate(LocalDateTime.now());
		for (UserInfoDto userInfoDto : userInfoDtos) {
			User user = new User();
			BeanUtils.copyProperties(userInfoDto, user);
			user.setOperator(currentUser);
			user.setUpdateTime(currentDate);
			if (this.update(user) < 1) {
				user.setCommonValue(currentUser, tenantCode);
				user.setStatus(CommonConstant.STATUS_NORMAL);
				this.insert(user);
			}
			// 先删除用户授权信息
			UserAuths userAuths = new UserAuths();
			userAuths.setIdentifier(userInfoDto.getIdentifier());
			// 默认密码
			if (StringUtils.isBlank(userInfoDto.getCredential())) {
				userInfoDto.setCredential(encodeCredential(CommonConstant.DEFAULT_PASSWORD));
			}
			userAuths.setCredential(userInfoDto.getCredential());
			userAuths.setOperator(currentUser);
			userAuths.setUpdateTime(currentDate);
			userAuths.setTenantCode(tenantCode);
			userAuthsService.deleteByIdentifier(userAuths);
			userAuths.setCommonValue(currentUser, tenantCode);
			userAuths.setUserId(user.getId());
			userAuths.setIdentityType(userInfoDto.getIdentityType());
			userAuthsService.insert(userAuths);
		}
		return true;
	}

	/**
	 * 加密
	 *
	 * @param credential 明文
	 * @return 密文
	 */
	public String encodeCredential(String credential) {
		return encoder.encode(credential);
	}

	/**
	 * 获取用户头像地址
	 * @param users users
	 * @return Map
	 */
	public Map<Long, String> findUserAvatarUrl(List<User> users) {
		Map<Long, String> result = Maps.newHashMapWithExpectedSize(users.size());
		for (User user : users) {
			if (user.getAvatarId() != null) {
				try {
					result.put(user.getId(), qiNiuService.getPreviewUrl(user.getAvatarId()));
				} catch (Exception e) {
					log.error("findUserAvatarUrl failed, userId: {}", user.getId(), e);
				}
			}
		}
		return result;
	}

	public boolean updateLoginInfo(UserDto userDto) {
		if (StringUtils.isNotBlank(userDto.getIdentifier())) {
			UserAuths userAuths = findUserAuthsByIdentifier(userDto.getIdentifier(), userDto.getTenantCode());
			if (userAuths != null) {
				User user = new User();
				user.setId(userAuths.getUserId());
				user.setLoginTime(userDto.getLoginTime());
				user.setUpdateTime(userDto.getLoginTime());
				user.setOperator(userAuths.getIdentifier());
				return update(user) > 0;
			}
		}
		return false;
	}
}
