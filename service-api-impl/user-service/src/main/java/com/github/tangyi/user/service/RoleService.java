package com.github.tangyi.user.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.IdGen;
import com.github.tangyi.user.api.module.Role;
import com.github.tangyi.user.api.module.RoleDept;
import com.github.tangyi.user.mapper.RoleDeptMapper;
import com.github.tangyi.user.mapper.RoleMapper;
import com.github.tangyi.user.mapper.RoleMenuMapper;
import com.github.tangyi.user.mapper.UserRoleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色service
 *
 * @author tangyi
 * @date 2018/8/26 14:16
 */
@Service
public class RoleService extends CrudService<RoleMapper, Role> {

    @Autowired
    private RoleDeptMapper roleDeptMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 新增
     *
     * @param role
     * @return int
     */
    @Override
    @Transactional
    public int insert(Role role) {
        // 保存所属部门
        if (StringUtils.isNotBlank(role.getDeptId())) {
            RoleDept roleDept = new RoleDept();
            roleDept.setRoleId(role.getId());
            roleDept.setId(IdGen.snowflakeId());
            roleDept.setDeptId(role.getDeptId());
            roleDeptMapper.insert(roleDept);
        }
        return super.insert(role);
    }

    /**
     * 更新
     *
     * @param role role
     * @return int
     */
    @Override
    @Transactional
    public int update(Role role) {
        // 更新所属部门
        if (StringUtils.isNotBlank(role.getDeptId())) {
            RoleDept roleDept = new RoleDept();
            roleDept.setRoleId(role.getId());
            roleDeptMapper.deleteByRoleId(role.getId());
            roleDept.setId(IdGen.snowflakeId());
            roleDept.setDeptId(role.getDeptId());
            roleDeptMapper.insert(roleDept);
        }
        return super.update(role);
    }

    /**
     * 删除
     *
     * @param role role
     * @return int
     */
    @Override
    @Transactional
    public int delete(Role role) {
        // 删除所属部门
        roleDeptMapper.deleteByRoleId(role.getId());
        // 删除角色菜单关系
        roleMenuMapper.deleteByRoleId(role.getId());
        // 删除用户角色关系
        userRoleMapper.deleteByRoleId(role.getId());
        return super.delete(role);
    }
}
