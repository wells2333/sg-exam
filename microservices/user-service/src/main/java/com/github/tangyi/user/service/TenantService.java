package com.github.tangyi.user.service;

import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.module.*;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.mapper.TenantMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户Service
 *
 * @author tangyi
 * @date 2019/5/22 22:51
 */
@Slf4j
@AllArgsConstructor
@Service
public class TenantService extends CrudService<TenantMapper, Tenant> {

    private final UserService userService;

    private final UserAuthsService userAuthsService;

    private final UserRoleService userRoleService;

    private final RoleService roleService;

    private final MenuService menuService;

    /**
     * 根据租户标识获取
     *
     * @param tenantCode tenantCode
     * @return Tenant
     * @author tangyi
     * @date 2019/05/26 10:28
     */
    @Cacheable(value = "tenant#" + CommonConstant.CACHE_EXPIRE, key = "#tenantCode")
    public Tenant getByTenantCode(String tenantCode) {
        return this.dao.getByTenantCode(tenantCode);
    }

    /**
     * 新增租户，自动初始化租户管理员账号
     *
     * @param tenant tenant
     * @return int
     * @author tangyi
     * @date 2019-09-02 11:41
     */
    @Transactional
    @CacheEvict(value = "tenant", key = "#tenant.tenantCode")
    public int add(Tenant tenant) {
        return this.insert(tenant);
    }

    /**
     * 更新
     *
     * @param tenant tenant
     * @return Tenant
     * @author tangyi
     * @date 2019/05/26 10:28
     */
    @Transactional
    @CacheEvict(value = "tenant", key = "#tenant.tenantCode")
    @Override
    public int update(Tenant tenant) {
        Integer status = tenant.getStatus();
        Tenant currentTenant = this.get(tenant);
        // 待审核 -> 审核通过
        if (currentTenant != null && currentTenant.getStatus().equals(TenantConstant.PENDING_AUDIT) && status.equals(TenantConstant.APPROVAL)) {
            log.info("Pending review -> review passed：{}", tenant.getTenantCode());
            // 用户基本信息
            User user = new User();
            user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), tenant.getTenantCode());
            user.setStatus(CommonConstant.STATUS_NORMAL);
            user.setName(tenant.getTenantName());
            userService.insert(user);
            // 用户账号
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), tenant.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(tenant.getTenantCode());
            userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
            userAuths.setCredential(userService.encodeCredential(CommonConstant.DEFAULT_PASSWORD));
            userAuthsService.insert(userAuths);
            // 绑定角色
            Role role = new Role();
            role.setRoleCode(SecurityConstant.ROLE_TENANT_ADMIN);
            role = roleService.findByRoleCode(role);
            UserRole userRole = new UserRole();
            userRole.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), tenant.getTenantCode());
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleService.insert(userRole);
        }
        return super.update(tenant);
    }

    /**
     * 删除
     *
     * @param tenant tenant
     * @return Tenant
     * @author tangyi
     * @date 2019/05/26 10:28
     */
    @Transactional
    @CacheEvict(value = "tenant", key = "#tenant.tenantCode")
    @Override
    public int delete(Tenant tenant) {
        // 删除菜单
        Menu menu = new Menu();
        menu.setTenantCode(tenant.getTenantCode());
        menuService.deleteByTenantCode(menu);
        // TODO 删除用户

        // TODO 删除权限
        return super.delete(tenant);
    }

    /**
     * 删除
     *
     * @param ids ids
     * @return Tenant
     * @author tangyi
     * @date 2019/05/26 10:37
     */
    @Transactional
    @CacheEvict(value = "tenant", allEntries = true)
    @Override
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }

    /**
     * 查询单位数量
     * @return Integer
     * @author tangyi
     * @date 2019/12/18 5:09 下午
     */
	public Integer tenantCount() {
		return this.dao.tenantCount();
	}
}
