package com.github.tangyi.auth.security;

import com.github.tangyi.auth.model.CustomUserDetails;
import com.github.tangyi.auth.properties.SysProperties;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.TenantNotFoundException;
import com.github.tangyi.common.core.vo.Role;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.security.core.CustomUserDetailsService;
import com.github.tangyi.common.security.core.GrantedAuthorityImpl;
import com.github.tangyi.user.api.constant.MenuConstant;
import com.github.tangyi.user.api.feign.UserServiceClient;
import com.github.tangyi.user.api.module.Menu;
import com.github.tangyi.user.api.module.Tenant;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 从数据库获取用户信息
 *
 * @author tangyi
 * @date 2019-03-14 14:36
 */
@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserServiceClient userServiceClient;

    private final SysProperties sysProperties;

    /**
     * 加载用户信息
     *
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException,TenantNotFoundException
     */
    @Override
    public UserDetails loadUserByUsernameAndTenantCode(String username, String tenantCode) throws UsernameNotFoundException, TenantNotFoundException {
        Tenant tenant = this.validateTenantCode(tenantCode);
        UserVo userVo = userServiceClient.findUserByUsername(username, tenantCode);
        if (userVo == null)
            throw new UsernameNotFoundException("用户名不存在.");
        return new CustomUserDetails(username, userVo.getPassword(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode());
    }

    /**
     * 根据社交账号查询
     *
     * @param social     social
     * @param tenantCode tenantCode
     * @return UserDetails
     * @author tangyi
     * @date 2019/06/22 21:08
     */
    @Override
    public UserDetails loadUserBySocialAndTenantCode(String social, String tenantCode) throws UsernameNotFoundException {
        Tenant tenant = this.validateTenantCode(tenantCode);
        UserVo userVo = userServiceClient.findUserBySocial(social, tenantCode);
        if (userVo == null)
            throw new UsernameNotFoundException("用户手机号未注册.");
        return new CustomUserDetails(social, userVo.getPassword(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode());
    }

    /**
     * 校验租户标识
     *
     * @param tenantCode tenantCode
     * @return Tenant
     */
    private Tenant validateTenantCode(String tenantCode) throws TenantNotFoundException {
        if (StringUtils.isBlank(tenantCode))
            throw new TenantNotFoundException("租户code不能为空.");
        // 先获取租户信息
        Tenant tenant = userServiceClient.findTenantByTenantCode(tenantCode);
        if (tenant == null)
            throw new TenantNotFoundException("租户不存在.");
        return tenant;
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
            roleList.forEach(role -> {
                // 权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_ADMIN就是ADMIN角色，MENU:ADD就是MENU:ADD权限
                authorities.add(new GrantedAuthorityImpl(role.getRoleCode()));
                // 根据角色查找菜单权限
                Stream<Menu> menuStream;
                // 判断是否是管理员，是则查找所有菜单权限
                if (userVo.getUsername().equals(sysProperties.getAdminUser())) {
                    // 查找所有菜单权限，因为角色一般是一个，这里只会执行一次
                    menuStream = userServiceClient.findAllMenu(userVo.getTenantCode()).stream();
                } else {
                    // 根据角色查找菜单权限
                    menuStream = userServiceClient.findMenuByRole(role.getRoleCode(), userVo.getTenantCode()).stream();
                }
                // 菜单权限
                List<GrantedAuthority> menus = menuStream
                        .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getType()))
                        .map(menu -> new GrantedAuthorityImpl(menu.getPermission())).collect(Collectors.toList());
                authorities.addAll(menus);
            });
        }
        return authorities;
    }
}
