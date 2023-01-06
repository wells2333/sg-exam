package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.Tenant;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantMapper extends CrudMapper<Tenant> {

    /**
     * 根据租户标识获取
     *
     * @param tenantCode tenantCode
     * @return Tenant
     * @author tangyi
     * @date 2019/05/26 10:29
     */
    Tenant getByTenantCode(String tenantCode);

	/**
	 * 查询租户数量
	 *
	 * @return Integer
	 */
	Integer tenantCount();
}
