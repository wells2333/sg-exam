package com.github.tangyi.user.service;

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

	private final MenuService menuService;

	private final TenantInitService tenantInitService;

	private final CommonExecutorService commonExecutorService;

	/**
	 * 根据租户标识获取
	 *
	 * @param tenantCode tenantCode
	 * @return Tenant
	 * @author tangyi
	 * @date 2019/05/26 10:28
	 */
	@Cacheable(value = UserCacheName.TENANT, key = "#tenantCode")
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

	/**
	 * 更新
	 *
	 * @param tenant tenant
	 * @return Tenant
	 * @author tangyi
	 * @date 2019/05/26 10:28
	 */
	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.TENANT, key = "#tenant.tenantCode")
	public int update(Tenant tenant) {
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

	/**
	 * 删除
	 *
	 * @param ids ids
	 * @return Tenant
	 * @author tangyi
	 * @date 2019/05/26 10:37
	 */
	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.TENANT, allEntries = true)
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
