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

package com.github.tangyi.user.service.sys;

import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.*;
import com.github.tangyi.api.user.service.IUserRoleService;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.sys.RoleMapper;
import com.github.tangyi.user.mapper.sys.UserMapper;
import com.github.tangyi.user.mapper.sys.UserRoleMapper;
import com.github.tangyi.user.service.attach.AttachmentService;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

@Slf4j
@Service
@AllArgsConstructor
public class UserService extends CrudService<UserMapper, User> implements IUserService, UserCacheName {

	public static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
	private static final String DEFAULT_AVATAR_URL = EnvUtils.getValue("DEFAULT_AVATAR_URL", "");

	private final IUserRoleService userRoleService;
	private final UserRoleMapper userRoleMapper;
	private final RoleMapper roleMapper;
	private final MenuService menuService;
	private final RedisTemplate redisTemplate;
	private final AttachmentService attachmentService;
	private final AttachmentManager attachmentManager;
	private final UserAuthsService userAuthsService;

	@Override
	@Cacheable(value = USER, key = "#id", unless = "#result == null")
	public User get(Long id) {
		return super.get(id);
	}

	@Override
	public Long findAllUserCount() {
		return this.dao.findAllUserCount();
	}

	/**
	 * 获取用户信息，包括头像、角色、权限信息
	 */
	@Cacheable(value = USER_DTO, key = "#userVo.tenantCode + ':' + #userVo.identifier")
	public UserInfoDto findUserInfo(UserVo userVo) {
		UserInfoDto dto = new UserInfoDto();
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
		List<Role> roles = this.userRoleService.getUserRoles(userAuths);
		// 根据角色查询权限
		Set<String> permissions = this.userRoleService.getUserPermissions(user, identifier, roles);
		dto.setRoles(roles.stream().map(Role::getRoleCode).toArray(String[]::new));
		dto.setPermissions(permissions.toArray(new String[0]));
		UserUtils.toUserInfoDto(dto, user, userAuths);
		// 头像信息
		getUserAvatar(dto, user);
		// 用户的菜单
		dto.setMenus(menuService.findUserMenus(tenantCode, identifier));
		return dto;
	}

	public List<Role> getUsersRoles(List<Long> userIds) {
		List<UserRole> userRoles = userRoleMapper.getByUserIds(userIds);
		if (CollectionUtils.isNotEmpty(userRoles)) {
			return roleMapper.findListById(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
		}
		return Collections.emptyList();
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

	public Attachment uploadAvatar(MultipartFile file) throws IOException {
		return attachmentManager.upload(MultipartFileUploadContext.of(AttachTypeEnum.AVATAR, file));
	}

	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public String updateAvatar(UserDto userDto) {
		User user = this.get(userDto.getId());
		SgPreconditions.checkNull(user, "The user does not exists.");
		if (user.getAvatarId() != null) {
			Attachment attach = attachmentService.get(user.getAvatarId());
			if (attach != null) {
				attachmentService.delete(attach);
			}
		}
		user.setAvatarId(userDto.getAvatarId());
		this.update(user);
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
		return ENCODER.encode(credential);
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

	private void getUserAvatar(UserInfoDto userInfoDto, User user) {
		if (user.getAvatarId() != null && user.getAvatarId() != 0L) {
			userInfoDto.setAvatar(attachmentManager.getPreviewUrlIgnoreException(user.getAvatarId()));
		} else {
			userInfoDto.setAvatar(DEFAULT_AVATAR_URL);
		}
	}
}
