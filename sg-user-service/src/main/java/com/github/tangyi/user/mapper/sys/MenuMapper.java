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
     * @return List
     */
    List<Menu> findByRole(@Param("role") String role, @Param("tenantCode") String tenantCode);

    /**
     * 批量插入
     *
     * @param menus menus
     * @return int
     */
    int insertBatch(List<Menu> menus);

    /**
     * 根据租户code删除
     * @param menu menu
     * @return int
     */
    int deleteByTenantCode(Menu menu);
}
