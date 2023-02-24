package com.github.tangyi.user.mapper.sys;

import com.github.tangyi.api.user.model.Tenant;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantMapper extends CrudMapper<Tenant> {

    Tenant getByTenantCode(String tenantCode);

	/**
	 * 查询租户数量
	 *
	 * @return Integer
	 */
	Integer tenantCount();
}
