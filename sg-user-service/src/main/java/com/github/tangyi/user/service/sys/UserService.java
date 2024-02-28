package com.github.tangyi.user.service.sys;

import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.model.*;
import com.github.tangyi.api.user.service.IUserService;
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
import com.github.tangyi.user.mapper.sys.RoleMapper;
import com.github.tangyi.user.mapper.sys.UserMapper;
import com.github.tangyi.user.mapper.sys.UserRoleMapper;
import com.github.tangyi.user.service.attach.AttachmentService;
import com.github.tangyi.user.utils.MenuUtil;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.base.Preconditions;
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

@Slf4j
@Service
@AllArgsConstructor
public class UserService extends CrudService<UserMapper, User> implements IUserService, UserCacheName {

	private static final String DEFAULT_AVATAR_URL = EnvUtils.getValue("DEFAULT_AVATAR_URL", "");
	private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

	private final UserRoleMapper userRoleMapper;
	private final RoleMapper roleMapper;
	private final MenuService menuService;
	private final RedisTemplate redisTemplate;
	private final AttachmentService attachmentService;
	private final AttachmentManager attachmentManager;
	private final SysProperties sysProperties;
	private final UserAuthsService userAuthsService;

	private void getUserAvatar(UserInfoDto userInfoDto, User user) {
		if (user.getAvatarId() != null && user.getAvatarId() != 0L) {
			userInfoDto.setAvatar(attachmentManager.getPreviewUrlIgnoreException(user.getAvatarId()));
		} else {
			userInfoDto.setAvatar(DEFAULT_AVATAR_URL);
		}
	}

	private List<Menu> getAdminUserPermissions(String tenantCode) {
		Menu condition = new Menu();
		condition.setTenantCode(tenantCode);
		condition.setType(UserServiceConstant.MENU_TYPE_PERMISSION);
		return menuService.findList(condition);
	}

	@Override
	public Long findAllUserCount() {
		return this.dao.findAllUserCount();
	}

	@Override
	@Transactional
	public int createUser(UserDto userDto) {
		String tenantCode = SysUtil.getTenantCode();
		userDto.setTenantCode(tenantCode);
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setCommonValue();
		// 保存父子账号关系
		UserAuths current = findUserAuthsByIdentifier(userDto.getIdentityType(), SysUtil.getUser(), tenantCode);
		user.setParentUid(current.getUserId());
		int update = insert(user);
		if (user.getId() != null) {
			createUserAuths(userDto, user);
			createUserRole(userDto, user);
		}
		return update;
	}

	@Override
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

	@Override
	@Transactional
	public void createUserRole(UserDto userDto, User user) {
		if (CollectionUtils.isEmpty(user.getRole())) {
			defaultRole(user, user.getTenantCode(), userDto.getIdentifier());
			return;
		}
		createUserRole(user.getId(), user.getRole());
	}

	@Override
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
	 */
	@Override
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

	@Override
	public User findUserByPhone(String phone, String tenantCode) {
		Preconditions.checkNotNull(phone);
		Preconditions.checkNotNull(tenantCode);
		return this.dao.findUserByPhone(phone, tenantCode);
	}

	@Override
	public UserAuths findUserAuthsByIdentifier(String identifier) {
		return findUserAuthsByIdentifier(null, identifier, SysUtil.getTenantCode());
	}

	@Override
	public UserAuths findUserAuthsByIdentifier(String identifier, String tenantCode) {
		return findUserAuthsByIdentifier(null, identifier, tenantCode);
	}

	@Override
	public UserAuths findUserAuthsByIdentifier(Integer identityType, String identifier, String tenantCode) {
		UserAuths condition = new UserAuths();
		condition.setIdentifier(identifier);
		if (identityType != null) {
			condition.setIdentityType(IdentityType.matchByType(identityType).getValue());
		}
		condition.setTenantCode(tenantCode);
		return userAuthsService.getByIdentifier(condition);
	}

	@Override
	public UserAuths findUserAuthsByUserId(Integer identityType, Long userId, String tenantCode) {
		UserAuths condition = new UserAuths();
		condition.setUserId(userId);
		condition.setTenantCode(tenantCode);
		if (identityType != null) {
			condition.setIdentityType(IdentityType.matchByType(identityType).getValue());
		}
		return userAuthsService.getByUserId(condition);
	}

	/**
	 * 获取用户信息，包括头像、角色、权限信息
	 */
	@Cacheable(value = USER_DTO, key = "#userVo.tenantCode + ':' + #userVo.identifier", unless = "#result == null")
	public UserInfoDto findUserInfo(UserVo userVo) {
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
		// 根据用户 ID 查询用户详细信息
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
		userInfoDto.setMenus(menuService.findUserMenus(tenantCode, identifier));
		return userInfoDto;
	}

	@Cacheable(value = USER_PERMISSION, key = "#user.tenantCode + ':' + #identifier", unless = "#result == null")
	public Set<String> getUserPermissions(User user, String identifier, List<Role> roles) {
		List<Menu> menuList = Lists.newArrayList();
		// 超级管理员
		if (SysUtil.isAdmin(identifier)) {
			menuList = getAdminUserPermissions(user.getTenantCode());
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

	@Cacheable(value = USER_ROLE, key = "#userAuths.tenantCode + ':' + #userAuths.identifier", unless = "#result == null")
	public List<Role> getUserRoles(UserAuths userAuths) {
		List<UserRole> userRoles = userRoleMapper.getByUserId(userAuths.getUserId());
		if (CollectionUtils.isNotEmpty(userRoles)) {
			return roleMapper.findListById(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
		}
		return Lists.newArrayList();
	}

	public List<Role> getUsersRoles(List<Long> userIds) {
		List<UserRole> userRoles = userRoleMapper.getByUserIds(userIds);
		if (CollectionUtils.isNotEmpty(userRoles)) {
			return roleMapper.findListById(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
		}
		return Lists.newArrayList();
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public boolean updateUser(Long id, UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setId(id);
		user.setCommonValue();
		this.dao.updateByPrimaryKeySelective(user);
		UserRole sysUserRole = new UserRole();
		sysUserRole.setUserId(user.getId());
		userRoleMapper.delete(sysUserRole);
		if (CollectionUtils.isNotEmpty(userDto.getRole())) {
			createUserRole(user.getId(), userDto.getRole());
		}
		return Boolean.TRUE;
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#user.tenantCode + ':' + #userAuths.identifier")
	public int update(User user, UserAuths userAuths) {
		user.setCommonValue();
		return this.dao.updateByPrimaryKeySelective(user);
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public boolean updateInfo(UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		UserAuths condition = new UserAuths();
		condition.setIdentifier(userDto.getIdentifier());
		condition.setTenantCode(userDto.getTenantCode());
		UserAuths userAuths = userAuthsService.getByIdentifier(condition);
		user.setId(userAuths.getUserId());
		return this.update(user, userAuths) > 0;
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public int updatePassword(UserDto userDto) {
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

	public Attachment uploadAvatar(MultipartFile file) throws IOException {
		return attachmentManager.upload(MultipartFileUploadContext.of(AttachTypeEnum.AVATAR, file));
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
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
		return attachmentManager.getPreviewUrl(userDto.getAvatarId());
	}

	public boolean checkIdentifierIsExist(Integer identityType, String identifier, String tenantCode) {
		UserAuths userAuths = new UserAuths();
		userAuths.setIdentifier(identifier);
		userAuths.setIdentityType(identityType);
		userAuths.setTenantCode(tenantCode);
		return userAuthsService.getByIdentifier(userAuths) != null;
	}

	@SuppressWarnings("unchecked")
	public void saveImageCode(String random, String imageCode) {
		redisTemplate.opsForValue()
				.set(CommonConstant.VERIFICATION_CODE_KEY + random, imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE,
						TimeUnit.SECONDS);
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userAuths.tenantCode + ':' + #userAuths.identifier")
	public int delete(User user, UserAuths userAuths) {
		userRoleMapper.deleteByUserId(userAuths.getUserId());
		userAuthsService.deleteByUserId(userAuths);
		user.setCommonValue();
		return super.delete(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, allEntries = true)
	public int deleteAll(Long[] ids) {
		String currentUser = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		for (Long id : ids) {
			userRoleMapper.deleteByUserId(id);
			UserAuths userAuths = new UserAuths();
			userAuths.setNewRecord(false);
			userAuths.setUserId(id);
			userAuths.setCommonValue(currentUser, tenantCode);
			userAuthsService.deleteByUserId(userAuths);
		}
		return super.deleteAll(ids);
	}

	public Integer userCount(UserVo userVo) {
		return this.dao.userCount(userVo);
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public boolean resetPassword(UserDto userDto) {
		UserAuths userAuths = findUserAuthsByIdentifier(null, userDto.getIdentifier(), userDto.getTenantCode());
		SgPreconditions.checkNull(userAuths, "账号不存在");
		userAuths.setCredential(encoder.encode(CommonConstant.DEFAULT_PASSWORD));
		return userAuthsService.update(userAuths) > 0;
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public boolean register(UserDto userDto) {
		Preconditions.checkNotNull(userDto.getIdentityType());
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
		if (this.insert(user) > 0) {
			registerUserAuths(user, userDto.getIdentifier(), userDto.getIdentityType(), password);
			// 分配默认角色
			return defaultRole(user, userDto.getTenantCode(), userDto.getIdentifier());
		}
		return false;
	}

	@Transactional
	public void registerUserAuths(User user, String identifier, Integer identityType, String password) {
		UserAuths userAuths = new UserAuths();
		userAuths.setCommonValue(identifier, user.getTenantCode());
		userAuths.setUserId(user.getId());
		userAuths.setIdentifier(identifier);
		if (identityType != null) {
			userAuths.setIdentityType(identityType);
		}
		userAuths.setCredential(encoder.encode(password));
		userAuthsService.insert(userAuths);
	}

	private String decryptCredential(String encoded, Integer identityType) {
		if (StringUtils.isBlank(encoded)) {
			return CommonConstant.DEFAULT_PASSWORD;
		}
		// 微信、手机号注册不需要解密
		if (IdentityType.WE_CHAT.getValue().equals(identityType) || IdentityType.PHONE_NUMBER.getValue()
				.equals(identityType)) {
			return encoded;
		}
		try {
			encoded = AesUtil.decryptAES(encoded, sysProperties.getKey()).trim();
		} catch (Exception e) {
			throw new CommonException(e, "Failed to decrypt credential");
		}
		return encoded;
	}

	@Transactional
	public boolean defaultRole(User user, String tenantCode, String identifier) {
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

	public List<UserVo> findUserVoListById(Long[] ids) {
		List<UserVo> userVos = Lists.newArrayListWithExpectedSize(ids.length);
		List<User> users = this.findListById(ids);
		if (CollectionUtils.isNotEmpty(users)) {
			for (User temp : users) {
				UserVo vo = new UserVo();
				BeanUtils.copyProperties(temp, vo);
				if (temp.getAvatarId() != null) {
					vo.setAvatarUrl(attachmentManager.getPreviewUrlIgnoreException(temp.getAvatarId()));
				}
				userVos.add(vo);
			}
		}
		return userVos;
	}

	public UserDto getUserDtoByUserAndUserAuths(User temp, List<UserAuths> uAuths, List<Dept> deptList,
			List<UserRole> uRoles, List<Role> roleList) {
		UserDto dto = new UserDto(temp);
		if (CollectionUtils.isNotEmpty(uAuths)) {
			uAuths.stream().filter(tempUAuth -> tempUAuth.getUserId().equals(temp.getId())).findFirst()
					.ifPresent(tempUAuth -> dto.setIdentifier(tempUAuth.getIdentifier()));
		}
		if (CollectionUtils.isNotEmpty(deptList)) {
			deptList.stream().filter(t -> t.getId().equals(temp.getDeptId())).findFirst().ifPresent(t -> {
				dto.setDeptId(t.getId());
				dto.setDeptName(t.getDeptName());
			});
		}
		if (CollectionUtils.isNotEmpty(uRoles)) {
			List<String> roleNames = Lists.newArrayList();
			List<Long> roleIds = Lists.newArrayList();
			uRoles.stream().filter(r -> temp.getId().equals(r.getUserId()))
					.forEach(r -> roleList.stream().filter(role -> role.getId().equals(r.getRoleId())).forEach(role -> {
						roleNames.add(role.getRoleName());
						roleIds.add(role.getId());
					}));
			dto.setRoleNames(StringUtils.join(roleNames, CommonConstant.COMMA));
			dto.setRole(roleIds);
		}
		return dto;
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS}, allEntries = true)
	public boolean importUsers(List<UserInfoDto> dtoList) {
		String currentUser = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		Date now = DateUtils.asDate(LocalDateTime.now());
		for (UserInfoDto dto : dtoList) {
			User user = new User();
			BeanUtils.copyProperties(dto, user);
			user.setOperator(currentUser);
			user.setUpdateTime(now);
			if (this.update(user) < 1) {
				user.setCommonValue(currentUser, tenantCode);
				user.setStatus(CommonConstant.STATUS_NORMAL);
				this.insert(user);
			}

			// 先删除用户授权信息
			UserAuths uAuths = new UserAuths();
			uAuths.setIdentifier(dto.getIdentifier());
			// 默认密码
			if (StringUtils.isBlank(dto.getCredential())) {
				dto.setCredential(encodeCredential(CommonConstant.DEFAULT_PASSWORD));
			}
			uAuths.setCredential(dto.getCredential());
			uAuths.setOperator(currentUser);
			uAuths.setUpdateTime(now);
			uAuths.setTenantCode(tenantCode);
			userAuthsService.deleteByIdentifier(uAuths);
			uAuths.setCommonValue(currentUser, tenantCode);
			uAuths.setUserId(user.getId());
			uAuths.setIdentityType(dto.getIdentityType());
			userAuthsService.insert(uAuths);
		}
		return true;
	}

	public String encodeCredential(String credential) {
		return encoder.encode(credential);
	}

	public Map<Long, String> findUserAvatarUrl(List<User> users) {
		Map<Long, String> result = Maps.newHashMapWithExpectedSize(users.size());
		for (User user : users) {
			if (user.getAvatarId() != null) {
				result.put(user.getId(), attachmentManager.getPreviewUrlIgnoreException(user.getAvatarId()));
			}
		}
		return result;
	}

	public boolean updateLoginInfo(UserDto userDto) {
		if (StringUtils.isBlank(userDto.getIdentifier())) {
			return false;
		}

		UserAuths userAuths = findUserAuthsByIdentifier(userDto.getIdentifier(), userDto.getTenantCode());
		if (userAuths == null) {
			return false;
		}

		User user = new User();
		user.setId(userAuths.getUserId());
		user.setLoginTime(userDto.getLoginTime());
		user.setUpdateTime(userDto.getLoginTime());
		user.setOperator(userAuths.getIdentifier());
		return update(user) > 0;
	}

	@Override
	public UserVo getUserInfo(Long id) {
		User user = this.get(id);
		if (user == null) {
			return null;
		}

		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		if (user.getAvatarId() != null) {
			userVo.setAvatarUrl(attachmentManager.getPreviewUrlIgnoreException(user.getAvatarId()));
		}
		return userVo;
	}
}
