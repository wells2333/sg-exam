package com.github.tangyi.user.service;

import com.github.tangyi.common.core.constant.SecurityConstant;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.IdGen;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.vo.Role;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.user.api.dto.UserDto;
import com.github.tangyi.user.api.dto.UserInfoDto;
import com.github.tangyi.user.api.module.Menu;
import com.github.tangyi.user.api.module.User;
import com.github.tangyi.user.api.module.UserRole;
import com.github.tangyi.user.mapper.MenuMapper;
import com.github.tangyi.user.mapper.UserMapper;
import com.github.tangyi.user.mapper.UserRoleMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户service实现
 *
 * @author tangyi
 * @date 2018-08-25 16:17
 */
@Service
public class UserService extends CrudService<UserMapper, User> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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
                sysUserRole.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                sysUserRole.setUserId(user.getId());
                sysUserRole.setRoleId(roleId);
                // 保存角色
                userRoleMapper.insert(sysUserRole);
            });
        }
        return super.insert(user);
    }

    /**
     * 获取用户信息
     *
     * @param userVo userVo
     * @return User
     * @author tangyi
     * @date 2018/9/11 下午 11:44
     */
    public UserInfoDto findUserInfo(UserVo userVo) {
        // 根据用户名查询用户信息
        userVo = userMapper.selectUserVoByUsername(userVo.getUsername());
        UserInfoDto user = new UserInfoDto();
        if (userVo != null) {
            user.setUser(userVo);
            // 用户角色列表
            List<Role> roleList = userVo.getRoleList();
            List<String> roleCodes = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(roleList)) {
                roleList.forEach(role -> {
                    if (!SecurityConstant.BASE_ROLE.equals(role.getRoleName()))
                        roleCodes.add(role.getRoleCode());
                });
            }
            String[] roleCodeArray = roleCodes.toArray(new String[roleCodes.size()]);
            user.setRoles(roleCodeArray);
            // 菜单列表
            Set<Menu> menuSet = new HashSet<>();
            for (String role : roleCodeArray)
                menuSet.addAll(menuMapper.findByRole(role));
            // 权限列表
            Set<String> permissions = new HashSet<>();
            menuSet.forEach(menu -> {
                if (StringUtils.isNotEmpty(menu.getPermission()))
                    permissions.add(menu.getPermission());
            });
            user.setPermissions(permissions.toArray(new String[permissions.size()]));
        }
        return user;
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return int
     * @author tangyi
     * @date 2018/8/26 下午 3:15
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.username")
    public boolean updateUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
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
                role.setId(IdGen.uuid());
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
    @CacheEvict(value = "user", key = "#user.username")
    public int update(User user) {
        return super.update(user);
    }

    /**
     * 根据用户名查找
     *
     * @param username username
     * @return UserVo
     */
    @Cacheable(value = "user", key = "#username")
    public UserVo selectUserVoByUsername(String username) {
        UserVo userVo = userMapper.selectUserVoByUsername(username);
        return userVo;
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
        redisTemplate.opsForValue().set(SecurityConstant.DEFAULT_CODE_KEY + random, imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 删除用户
     *
     * @param user user
     * @return int
     */
    @Transactional
    @Override
    @CacheEvict(value = "user", key = "#user.username")
    public int delete(User user) {
        // 删除用户角色关系
        userRoleMapper.deleteByUserId(user.getId());
        return super.delete(user);
    }
}
