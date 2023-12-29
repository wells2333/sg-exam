package com.github.tangyi.user.mapper.sys;

import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends CrudMapper<Menu> {

    /**
     * 根据角色查找菜单
     *
     * @param role       角色标识
     * @param tenantCode 租户标识
     */
    List<Menu> findByRole(@Param("role") String role, @Param("tenantCode") String tenantCode);

    /**
     * 根据角色查找菜单,无租户
     *
     * @param role 角色标识
     */
    List<Menu> findByRoleNoTeNantCode(@Param("role") List<String> role);

    /**
     * 批量插入
     */
    int insertBatch(List<Menu> menus);

    /**
     * 根据租户 code 删除
     */
    int deleteByTenantCode(Menu menu);
}
