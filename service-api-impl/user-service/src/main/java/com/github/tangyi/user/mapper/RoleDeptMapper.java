package com.github.tangyi.user.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.user.api.module.RoleDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单mapper
 *
 * @author tangyi
 * @date 2018/8/26 22:34
 */
@Mapper
public interface RoleDeptMapper extends CrudMapper<RoleDept> {

    /**
     * 根据部门ID查询
     *
     * @param deptId 部门ID
     * @return List
     */
    List<RoleDept> getByDeptId(String deptId);

    /**
     * 根据角色ID删除
     *
     * @param roleId 角色ID
     * @return int
     */
    int deleteByRoleId(String roleId);

    /**
     * 根据部门ID删除
     *
     * @param deptId 部门ID
     * @return int
     */
    int deleteByDeptId(String deptId);

}
