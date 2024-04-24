/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
