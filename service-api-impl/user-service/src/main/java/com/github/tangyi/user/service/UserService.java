package com.github.tangyi.user.service;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.IdGen;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.user.api.constant.MenuConstant;
import com.github.tangyi.user.api.dto.UserDto;
import com.github.tangyi.user.api.dto.UserInfoDto;
import com.github.tangyi.user.api.module.Menu;
import com.github.tangyi.user.api.module.User;
import com.github.tangyi.user.api.module.UserRole;
import com.github.tangyi.user.mapper.UserMapper;
import com.github.tangyi.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户service实现
 *
 * @author tangyi
 * @date 2018-08-25 16:17
 */
@AllArgsConstructor
@Service
public class UserService extends CrudService<UserMapper, User> {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final MenuService menuService;

    private final RedisTemplate redisTemplate;

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
    @CacheEvict(value = "user", key = "#user.username")
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
     * @date 2018/9/11 23:44
     */
    public UserInfoDto findUserInfo(UserVo userVo) {
        // 根据用户名查询用户信息
        userVo = userMapper.selectUserVoByUsername(userVo.getUsername());
        UserInfoDto user = new UserInfoDto();
        if (userVo != null) {
            user.setUser(userVo);
            // 用户角色
            List<String> roles = new ArrayList<>();
            // 用户权限
            List<String> permissions = new ArrayList<>();
            // 根据角色获取权限
            if (CollectionUtils.isNotEmpty(userVo.getRoleList())) {
                userVo.getRoleList().stream()
                        // 过滤普通用户角色
                        .filter(role -> !SecurityConstant.BASE_ROLE.equals(role.getRoleName()))
                        .forEach(role -> {
                            // 根据角色查找菜单
                            List<Menu> menuList = menuService.findMenuByRole(role.getRoleCode());
                            if (CollectionUtils.isNotEmpty(menuList)) {
                                menuList.stream()
                                        // 获取权限菜单
                                        .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getType()))
                                        // 获取权限
                                        .forEach(menu -> permissions.add(menu.getPermission()));
                            }
                            // 保存角色code
                            roles.add(role.getRoleCode());
                        });
            }
            user.setRoles(roles.toArray(new String[0]));
            user.setPermissions(permissions.toArray(new String[0]));
        }
        return user;
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return int
     * @author tangyi
     * @date 2018/8/26 15:15
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
        return userMapper.selectUserVoByUsername(username);
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
        redisTemplate.opsForValue().set(CommonConstant.DEFAULT_CODE_KEY + random, imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
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
}
