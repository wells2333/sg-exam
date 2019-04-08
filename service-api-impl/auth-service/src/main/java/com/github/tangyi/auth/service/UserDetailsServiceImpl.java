package com.github.tangyi.auth.service;

import com.github.tangyi.common.core.vo.Role;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.security.core.GrantedAuthorityImpl;
import com.github.tangyi.common.security.core.UserDetailsImpl;
import com.github.tangyi.user.api.feign.UserServiceClient;
import com.github.tangyi.user.api.module.Menu;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 模拟从数据库获取用户信息
 *
 * @author tangyi
 * @date 2019-03-14 14:36
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 加载用户信息
     *
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails;
        try {
            UserVo userVo = userServiceClient.findUserByUsername(username);
            if (userVo == null)
                throw new UsernameNotFoundException("User name not found.");
            userDetails = new UserDetailsImpl(username, userVo.getPassword(), getAuthority(userVo));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Exception occurred wile reading user info.");
        }
        return userDetails;
    }

    /**
     * 获取用户权限
     *
     * @param userVo userVo
     * @return Set
     * @author tangyi
     * @date 2019/03/17 14:41
     */
    private Set<GrantedAuthority> getAuthority(UserVo userVo) {
        // 权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 角色
        List<Role> roleList = userVo.getRoleList();
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (Role role : roleList) {
                // 权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_ADMIN就是ADMIN角色，MENU:ADD就是MENU:ADD权限
                authorities.add(new GrantedAuthorityImpl(role.getRoleCode().toUpperCase()));
                // 根据角色查找菜单权限
                List<Menu> menuList = userServiceClient.findMenuByRole(role.getRoleCode());
                if (CollectionUtils.isNotEmpty(menuList)) {
                    for (Menu menu : menuList) {
                        // 菜单权限
                        authorities.add(new GrantedAuthorityImpl(menu.getPermission()));
                    }
                }
            }
        }
        return authorities;
    }
}
