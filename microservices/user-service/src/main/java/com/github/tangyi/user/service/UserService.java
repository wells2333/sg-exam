package com.github.tangyi.user.service;

import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.constant.MenuConstant;
import com.github.tangyi.api.user.constant.RoleConstant;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.module.*;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.AesUtil;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.IdGen;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.user.mapper.RoleMapper;
import com.github.tangyi.user.mapper.UserMapper;
import com.github.tangyi.user.mapper.UserRoleMapper;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.collect.Lists;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final UserRoleMapper userRoleMapper;

    private final RoleMapper roleMapper;

    private final MenuService menuService;

    private final RedisTemplate redisTemplate;

    private final AttachmentService attachmentService;

    private final SysProperties sysProperties;

    private final UserAuthsService userAuthsService;

    /**
     * 新建用户
     *
     * @param userDto userDto
     * @return int
     * @author tangyi
     * @date 2019/07/03 12:17:44
     */
    @Transactional
    public int createUser(UserDto userDto) {
        int update;
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 先保存用户基本信息
        user.setCommonValue();
        // 保存父子账号关系
        UserVo currentUser = this.findUserByIdentifier(userDto.getIdentityType(), SysUtil.getUser(), SysUtil.getTenantCode());
        user.setParentUid(currentUser.getId());
        if ((update = this.insert(user)) > 0) {
            // 保存用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(user.getCreator(), user.getApplicationCode(), user.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            if (userDto.getIdentityType() == null)
                userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
            // 默认密码为123456
            if (StringUtils.isBlank(userDto.getCredential()))
                userDto.setCredential(CommonConstant.DEFAULT_PASSWORD);
            userAuths.setCredential(encoder.encode(userDto.getCredential()));
            update = userAuthsService.insert(userAuths);
            // 分配默认角色
            this.defaultRole(user, SysUtil.getTenantCode(), userDto.getIdentifier());
        }
        return update;
    }

    /**
     * 新增用户
     *
     * @param user user
     * @return int
     * @author tangyi
     * @date 2018/10/30 12:43
     */
    @Override
    @Transactional
    public int insert(User user) {
        // 保存角色
        if (CollectionUtils.isNotEmpty(user.getRole())) {
            user.getRole().forEach(roleId -> {
                UserRole sysUserRole = new UserRole();
                sysUserRole.setCommonValue();
                sysUserRole.setUserId(user.getId());
                sysUserRole.setRoleId(roleId);
                // 保存角色
                userRoleMapper.insert(sysUserRole);
            });
        }
        return super.insert(user);
    }

    /**
     * 获取用户信息，包括头像、角色、权限信息
     *
     * @param userVo userVo
     * @return User
     * @author tangyi
     * @date 2018/9/11 23:44
     */
    public UserInfoDto findUserInfo(UserVo userVo) {
        // 返回结果
        UserInfoDto userInfoDto = new UserInfoDto();
        String tenantCode = userVo.getTenantCode(), identifier = userVo.getIdentifier();
        // 根据唯一标识查询账号信息
        UserAuths userAuths = new UserAuths();
        if (userVo.getIdentityType() != null)
            userAuths.setIdentityType(userVo.getIdentityType());
        userAuths.setIdentifier(userVo.getIdentifier());
        userAuths.setTenantCode(tenantCode);
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("Identifier " + identifier + "does not exist");
        // 根据用户id查询用户详细信息
        User user = this.get(userAuths.getUserId());
        if (user == null)
            throw new CommonException("Get user info failed");
        // 查询用户的角色信息
        List<Role> roles = this.getUserRoles(user);
        // 根据角色查询权限
        List<String> permissions = this.getUserPermissions(user, identifier, roles);
        userInfoDto.setRoles(roles.stream().map(Role::getRoleCode).toArray(String[]::new));
        userInfoDto.setPermissions(permissions.toArray(new String[0]));
        UserUtils.toUserInfoDto(userInfoDto, user, userAuths);
        // 头像信息
        this.initUserAvatar(userInfoDto, user);
        return userInfoDto;
    }

    /**
     * 查询用户权限数组
     *
     * @param user       user
     * @param identifier identifier
     * @return List
     * @author tangyi
     * @date 2019/07/04 00:12:44
     */
    public List<String> getUserPermissions(User user, String identifier) {
        return this.getUserPermissions(user, identifier, this.getUserRoles(user));
    }

    /**
     * 根据指定角色集合，查询用户权限数组
     *
     * @param user       user
     * @param identifier identifier
     * @param roles      roles
     * @return List
     * @author tangyi
     * @date 2019/07/04 00:14:44
     */
    public List<String> getUserPermissions(User user, String identifier, List<Role> roles) {
        // 用户权限
        List<String> permissions = new ArrayList<>();
        List<Menu> menuList = new ArrayList<>();
        // 管理员
        if (UserUtils.isAdmin(identifier)) {
            Menu menu = new Menu();
            menu.setTenantCode(user.getTenantCode());
            menu.setApplicationCode(user.getApplicationCode());
            menu.setType(MenuConstant.MENU_TYPE_PERMISSION);
            menuList = menuService.findAllList(menu);
        } else {
            for (Role role : roles) {
                // 根据角色查找菜单
                List<Menu> roleMenuList = menuService.findMenuByRole(role.getRoleCode(), user.getTenantCode());
                if (CollectionUtils.isNotEmpty(roleMenuList))
                    menuList.addAll(roleMenuList);
            }
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            permissions = menuList.stream()
                    // 获取权限菜单
                    .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getType()))
                    // 获取权限
                    .map(Menu::getPermission).collect(Collectors.toList());

        }
        return permissions;
    }

    /**
     * 获取用户的角色
     *
     * @param user user
     * @return List
     * @author tangyi
     * @date 2019/07/03 12:03:17
     */
    private List<Role> getUserRoles(User user) {
        List<Role> roles = Lists.newArrayList();
        List<UserRole> userRoles = userRoleMapper.getByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(userRoles)) {
            roles = roleMapper.findListById(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
        }
        return roles;
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
        // 流处理获取用户ID集合，根据用户ID批量查找角色
        List<UserRole> userRoles = userRoleMapper.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
        List<Role> roleList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(userRoles)) {
            // 批量查找角色
            roleList = roleMapper.findListById(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
        }
        return roleList;
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
    @CacheEvict(value = "user", key = "#id")
    public boolean updateUser(Long id, UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setId(id);
        user.setCommonValue();
        // 更新用户信息
        super.update(user);
        // 更新用户角色关系
        UserRole sysUserRole = new UserRole();
        sysUserRole.setUserId(user.getId());
        // 删除原有的角色信息
        userRoleMapper.delete(sysUserRole);
        if (CollectionUtils.isNotEmpty(userDto.getRole())) {
            userDto.getRole().forEach(roleId -> {
                UserRole role = new UserRole();
                role.setId(IdGen.snowflakeId());
                role.setUserId(user.getId());
                role.setRoleId(roleId);
                // 保存角色信息
                userRoleMapper.insert(role);
            });
        }
        return Boolean.TRUE;
    }

    /**
     * 更新用户基本信息
     *
     * @param user user
     * @return int
     */
    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#user.id")
    public int update(User user) {
        return super.update(user);
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
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public int updatePassword(UserDto userDto) {
        userDto.setTenantCode(SysUtil.getTenantCode());
        if (StringUtils.isBlank(userDto.getNewPassword()))
            throw new CommonException("新密码不能为空.");
        if (StringUtils.isBlank(userDto.getIdentifier()))
            throw new CommonException("用户名不能为空.");
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths.setTenantCode(userDto.getTenantCode());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号不存在.");
        if (!encoder.matches(userDto.getOldPassword(), userAuths.getCredential())) {
            throw new CommonException("新旧密码不匹配");
        } else {
            // 新旧密码一致，修改密码
            userAuths.setCredential(encoder.encode(userDto.getNewPassword()));
            return userAuthsService.update(userAuths);
        }
    }

    /**
     * 更新头像
     *
     * @param userDto userDto
     * @return int
     * @author tangyi
     * @date 2019/06/21 18:14
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public int updateAvatar(UserDto userDto) {
        User user = this.get(userDto.getId());
        if (user == null)
            throw new CommonException("用户不存在.");
        // 先删除旧头像
        if (user.getAvatarId() != null) {
            Attachment attachment = attachmentService.get(user.getAvatarId());
            if (attachment != null)
                attachmentService.delete(attachment);
        }
        user.setAvatarId(userDto.getAvatarId());
        return super.update(user);
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
    @Cacheable(value = "user#" + CommonConstant.CACHE_EXPIRE, key = "#identifier")
    public UserVo findUserByIdentifier(Integer identityType, String identifier, String tenantCode) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(identifier);
        if (identityType != null)
            userAuths.setIdentityType(IdentityType.matchByType(identityType).getValue());
        userAuths.setTenantCode(tenantCode);
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            return null;
        // 查询用户信息
        User user = this.get(userAuths.getUserId());
        if (user == null)
            return null;
        // 查询用户角色
        List<Role> roles = this.getUserRoles(user);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        BeanUtils.copyProperties(userAuths, userVo);
        userVo.setRoleList(UserUtils.rolesToVo(roles));
        userVo.setUserId(user.getId());
        return userVo;
    }

    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identifier identifier
     * @param tenantCode tenantCode
     * @return UserVo
     * @author tangyi
     * @date 2019/07/10 18:04:15
     */
    public UserVo findUserByIdentifier(String identifier, String tenantCode) {
        return this.findUserByIdentifier(null, identifier, tenantCode);
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
        redisTemplate.opsForValue().set(CommonConstant.DEFAULT_CODE_KEY + LoginTypeEnum.PWD.getType() + "@" + random, imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 删除用户
     *
     * @param user user
     * @return int
     */
    @Transactional
    @Override
    @CacheEvict(value = "user", key = "#user.id")
    public int delete(User user) {
        // 删除用户角色关系
        userRoleMapper.deleteByUserId(user.getId());
        // 删除用户授权信息
        UserAuths userAuths = new UserAuths();
        userAuths.setUserId(user.getId());
        userAuthsService.deleteByUserId(userAuths);
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
    @CacheEvict(value = "user", allEntries = true)
    public int deleteAll(Long[] ids) {
        String currentUser = SysUtil.getUser(), applicationCode = SysUtil.getSysCode(), tenantCode = SysUtil.getTenantCode();
        for (Long id : ids) {
            // 删除用户角色关系
            userRoleMapper.deleteByUserId(id);
            // 删除用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setNewRecord(false);
            userAuths.setUserId(id);
            userAuths.setCommonValue(currentUser, applicationCode, tenantCode);
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
     * 初始化用户头像信息
     *
     * @param userInfoDto userInfoDto
     * @param user        user
     * @author tangyi
     * @date 2019/06/21 17:49
     */
    private void initUserAvatar(UserInfoDto userInfoDto, User user) {
        try {
            // 附件id不为空，获取对应的预览地址，否则获取配置默认头像地址
            if (user.getAvatarId() != null && user.getAvatarId() != 0L) {
                Attachment attachment = new Attachment();
                attachment.setId(user.getAvatarId());
                userInfoDto.setAvatarUrl(attachmentService.getPreviewUrl(attachment));
            } else {
                userInfoDto.setAvatarUrl(sysProperties.getDefaultAvatar() + (new Random().nextInt(4) + 1) + ".jpg");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public boolean resetPassword(UserDto userDto) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号不存在.");
        // 重置密码为123456
        userAuths.setCredential(encoder.encode(CommonConstant.DEFAULT_PASSWORD));
        return userAuthsService.update(userAuths) > 0;
    }

    /**
     * 注册，注意要清除缓存
     *
     * @param userDto userDto
     * @return boolean
     * @author tangyi
     * @date 2019/07/03 13:30:03
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public boolean register(UserDto userDto) {
        boolean success = false;
        if (userDto.getIdentityType() == null)
            userDto.setIdentityType(IdentityType.PASSWORD.getValue());
        // 解密
        String password = this.decryptCredential(userDto.getCredential(), userDto.getIdentityType());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 初始化用户名，系统编号，租户编号
        user.setCommonValue(userDto.getIdentifier(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
        // 初始化头像
        if (StringUtils.isNotBlank(userDto.getAvatarUrl())) {
            Attachment attachment = new Attachment();
            attachment.setCommonValue(userDto.getIdentifier(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            attachment.setBusiType(AttachmentConstant.BUSI_TYPE_USER_AVATAR);
            attachment.setPreviewUrl(userDto.getAvatarUrl());
            if (attachmentService.insert(attachment) > 0)
                user.setAvatarId(attachment.getId());
        }
        // 保存用户基本信息
        if (this.insert(user) > 0) {
            // 保存账号信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(userDto.getIdentifier(), user.getApplicationCode(), user.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            if (userDto.getIdentityType() != null)
                userAuths.setIdentityType(userDto.getIdentityType());
            // 设置密码
            userAuths.setCredential(encoder.encode(password));
            userAuthsService.insert(userAuths);
            // 分配默认角色
            success = this.defaultRole(user, userDto.getTenantCode(), userDto.getIdentifier());
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
        if (StringUtils.isBlank(encoded))
            return CommonConstant.DEFAULT_PASSWORD;
        // 微信、手机号注册不需要解密
        if (IdentityType.WE_CHAT.getValue().equals(identityType) || IdentityType.PHONE_NUMBER.getValue().equals(identityType))
            return encoded;
        // 解密密码
        try {
            encoded = AesUtil.decryptAES(encoded, sysProperties.getKey()).trim();
            log.info("Decrypt result: {}", encoded);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("Decrypt failed: " + e.getMessage());
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
        Role role = new Role();
        role.setIsDefault(RoleConstant.IS_DEFAULT_ROLE);
        role.setTenantCode(tenantCode);
        // 查询默认角色
        Stream<Role> roleStream = roleMapper.findList(role).stream();
        if (Optional.ofNullable(roleStream).isPresent()) {
            Role defaultRole = roleStream.findFirst().orElse(null);
            if (defaultRole != null) {
                UserRole userRole = new UserRole();
                userRole.setCommonValue(identifier, user.getApplicationCode(), user.getTenantCode());
                userRole.setUserId(user.getId());
                userRole.setRoleId(defaultRole.getId());
                // 保存用户角色关系
                return userRoleMapper.insert(userRole) > 0;
            }
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
        List<UserVo> userVos = Lists.newArrayList();
        Stream<User> userStream = this.findListById(ids).stream();
        if (Optional.ofNullable(userStream).isPresent()) {
			Attachment attachment = new Attachment();
            userVos = userStream.map(tempUser -> {
                UserVo tempUserVo = new UserVo();
                BeanUtils.copyProperties(tempUser, tempUserVo);
                if (tempUser.getAvatarId() != null) {
                    attachment.setId(tempUser.getAvatarId());
                    tempUserVo.setAvatarUrl(attachmentService.getPreviewUrl(attachment));
                }
                return tempUserVo;
            }).collect(Collectors.toList());
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
    public UserDto getUserDtoByUserAndUserAuths(User tempUser, List<UserAuths> userAuths, List<Dept> deptList, List<UserRole> userRoles, List<Role> finalRoleList) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(tempUser, userDto);
        // 设置账号信息
        if (CollectionUtils.isNotEmpty(userAuths)) {
            userAuths.stream().filter(tempUserAuths -> tempUserAuths.getUserId().equals(tempUser.getId()))
                    .findFirst().ifPresent(tempUserAuths -> userDto.setIdentifier(tempUserAuths.getIdentifier()));
        }
        // 设置部门信息
        if (CollectionUtils.isNotEmpty(deptList)) {
            // 用户所属部门
            deptList.stream()
                    .filter(tempDept -> tempDept.getId().equals(tempUser.getDeptId()))
                    .findFirst().ifPresent(userDept -> {
                userDto.setDeptId(userDept.getId());
                userDto.setDeptName(userDept.getDeptName());
            });
        }
        // 设置角色信息
        if (CollectionUtils.isNotEmpty(userRoles)) {
            List<Role> userRoleList = new ArrayList<>();
            userRoles.stream()
                    .filter(tempUserRole -> tempUser.getId().equals(tempUserRole.getUserId()))
                    .forEach(tempUserRole -> finalRoleList.stream()
                            .filter(role -> role.getId().equals(tempUserRole.getRoleId()))
                            .forEach(userRoleList::add));
            userDto.setRoleList(userRoleList);
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
    @CacheEvict(value = "user", allEntries = true)
    public boolean importUsers(List<UserInfoDto> userInfoDtos) {
        String currentUser = SysUtil.getUser(), applicationCode = SysUtil.getSysCode(), tenantCode = SysUtil.getTenantCode();
        Date currentDate = DateUtils.asDate(LocalDateTime.now());
        for (UserInfoDto userInfoDto : userInfoDtos) {
            User user = new User();
            BeanUtils.copyProperties(userInfoDto, user);
            user.setModifier(currentUser);
            user.setModifyDate(currentDate);
            if (this.update(user) < 1) {
                user.setCommonValue(currentUser, applicationCode, tenantCode);
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
            userAuths.setModifier(currentUser);
            userAuths.setModifyDate(currentDate);
            userAuths.setTenantCode(tenantCode);
            userAuthsService.deleteByIdentifier(userAuths);
            // 重新insert
            userAuths.setCommonValue(currentUser, applicationCode, tenantCode);
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
}
