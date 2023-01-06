package com.github.tangyi.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.Tenant;
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
public class TenantService extends CrudService<TenantMapper, Tenant> {

	private final MenuService menuService;

	private final TenantInitService tenantInitService;

	private final CommonExecutorService commonExecutorService;

	private final QiNiuService qiNiuService;

	@Cacheable(value = UserCacheName.TENANT, key = "#tenantCode")
	public Tenant getByTenantCode(String tenantCode) {
		return this.dao.getByTenantCode(tenantCode);
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
		tenant.setInitStatus(TenantConstant.INIT_ING);
		int update = this.insert(tenant);
		if (update > 0) {
			asyncInit(tenant);
		}
		return update;
	}

	/**
	 * 异步初始化
	 * @param tenant tenant
	 */
	public void asyncInit(Tenant tenant) {
		String identifier = SysUtil.getUser();
		commonExecutorService.getCommonExecutor().execute(() -> {
			try {
				Role role = tenantInitService.initTenant(tenant, identifier);
				tenant.setRoleId(role.getId());
				tenant.setInitStatus(TenantConstant.INIT_SUCCESS);
			} catch (Exception e) {
				tenant.setInitStatus(TenantConstant.INIT_FAILED);
				log.error("async init tenant failed", e);
			} finally {
				update(tenant);
			}
		});
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.TENANT, key = "#tenant.tenantCode")
	public int update(Tenant tenant) {
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
