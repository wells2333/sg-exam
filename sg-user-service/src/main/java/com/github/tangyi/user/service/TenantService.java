package com.github.tangyi.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.Tenant;
import com.github.tangyi.api.user.service.ITenantService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.TenantMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class TenantService extends CrudService<TenantMapper, Tenant> implements ITenantService {

	private final MenuService menuService;

	private final TenantInitService tenantInitService;

	private final QiNiuService qiNiuService;

	private final RoleService roleService;

	@Cacheable(value = UserCacheName.TENANT, key = "#tenantCode")
	public Tenant getByTenantCode(String tenantCode) {
		return this.dao.getByTenantCode(tenantCode);
	}

	@Override
	public Tenant get(Long id) {
		Tenant tenant = super.get(id);
		if (tenant != null && tenant.getImageId() != null) {
			tenant.setImageUrl(qiNiuService.getPreviewUrl(tenant.getImageId()));
		}
		return tenant;
	}

	@Override
	public PageInfo<Tenant> findPage(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<Tenant> page = super.findPage(params, pageNum, pageSize);
		if (CollectionUtils.isNotEmpty(page.getList())) {
			for (Tenant tenant : page.getList()) {
				if (tenant.getImageId() != null) {
					tenant.setImageUrl(qiNiuService.getPreviewUrl(tenant.getImageId()));
				}
			}
		}
		return page;
	}

	/**
	 * 新增租户，自动初始化租户管理员账号
	 */
	@Transactional
	public int add(Tenant tenant) {
		int update = this.insert(tenant);
		if (TenantConstant.PASS_AUDIT.equals(tenant.getStatus())) {
			Role role = tenantInitService.initTenant(tenant, SysUtil.getUser());
			tenant.setRoleId(role.getId());
			tenant.setInitStatus(TenantConstant.INIT_SUCCESS);
			updateTenantInfo(tenant);
		}
		return update;
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.TENANT, key = "#tenant.tenantCode")
	public int update(Tenant tenant) {
		Tenant oldTenant = this.get(tenant.getId());
		// 未初始化 + 待审核 => 审核通过
		if (oldTenant != null && TenantConstant.NOT_INIT.equals(oldTenant.getInitStatus())
				&& TenantConstant.PENDING_AUDIT.equals(oldTenant.getStatus()) && TenantConstant.PASS_AUDIT.equals(
				tenant.getStatus())) {
			doInitTenant(tenant);
		} else if (TenantConstant.PASS_AUDIT.equals(tenant.getStatus())) {
			// 审核通过，但没初始化对应的角色
			if (tenant.getRoleId() == null) {
				doInitTenant(tenant);
			} else {
				// 已经初始化过，则更新菜单
				updateTenantMenu(tenant);
			}
		}
		return super.update(tenant);
	}

	@Transactional
	public void doInitTenant(Tenant tenant) {
		Role role = tenantInitService.initTenant(tenant, SysUtil.getUser());
		tenant.setRoleId(role.getId());
		tenant.setInitStatus(TenantConstant.INIT_SUCCESS);
	}

	@Transactional
	public void updateTenantMenu(Tenant tenant) {
		Role role = roleService.get(tenant.getRoleId());
		if (role != null) {
			tenantInitService.initTenantMenu(tenant, role, SysUtil.getUser());
			tenant.setInitStatus(TenantConstant.INIT_SUCCESS);
		}
	}

	@Transactional
	@CacheEvict(value = UserCacheName.TENANT, key = "#tenant.tenantCode")
	public int updateTenantInfo(Tenant tenant) {
		return super.update(tenant);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.TENANT, key = "#tenant.tenantCode")
	public int delete(Tenant tenant) {
		// 删除菜单
		Menu menu = new Menu();
		menu.setCommonValue(SysUtil.getUser(), tenant.getTenantCode());
		menuService.deleteByTenantCode(menu);
		// TODO 删除用户

		// TODO 删除权限
		return super.delete(tenant);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.TENANT, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	public Integer tenantCount() {
		return this.dao.tenantCount();
	}
}
