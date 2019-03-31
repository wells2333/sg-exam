package com.github.tangyi.user.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.user.api.module.Dept;
import com.github.tangyi.user.api.module.Role;
import com.github.tangyi.user.api.module.RoleDept;
import com.github.tangyi.user.mapper.DeptMapper;
import com.github.tangyi.user.mapper.RoleDeptMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门service
 *
 * @author tangyi
 * @date 2018/8/26 22:46
 */
@Service
public class DeptService extends CrudService<DeptMapper, Dept> {

    @Autowired
    private RoleDeptMapper roleDeptMapper;

    @Autowired
    private RoleService roleService;

    /**
     * 删除部门
     *
     * @param dept dept
     * @return int
     */
    @Transactional
    @Override
    public int delete(Dept dept) {
        // 删除角色和部门角色关系
        List<RoleDept> roleDeptList = roleDeptMapper.getByDeptId(dept.getId());
        if (CollectionUtils.isNotEmpty(roleDeptList)) {
            roleDeptList.forEach(roleDept -> {
                Role role = new Role();
                role.setId(roleDept.getRoleId());
                // 删除角色
                roleService.delete(role);
            });
        }
        // 删除部门
        return super.delete(dept);
    }
}
