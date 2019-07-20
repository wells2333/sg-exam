package com.github.tangyi.user.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.user.api.module.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色mapper
 *
 * @author tangyi
 * @date 2018/8/26 09:33
 */
@Mapper
public interface RoleMapper extends CrudMapper<Role> {
}
