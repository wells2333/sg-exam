package com.github.tangyi.user.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.user.api.module.Tenant;
import com.github.tangyi.user.mapper.TenantMapper;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Service
public class TenantService extends CrudService<TenantMapper, Tenant> {

    /**
     * 根据租户标识获取
     *
     * @param tenantCode tenantCode
     * @return Tenant
     * @author tangyi
     * @date 2019/05/26 10:28
     */
    @Cacheable(value = "tenant", key = "#tenantCode")
    public Tenant getByTenantCode(String tenantCode) {
        return this.dao.getByTenantCode(tenantCode);
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
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }
}
