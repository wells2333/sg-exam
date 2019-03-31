package com.github.tangyi.user.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.user.api.module.RoleDept;
import com.github.tangyi.user.mapper.RoleDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangyi
 * @date 2018/8/26 22:47
 */
@Service
public class RoleDeptService extends CrudService<RoleDeptMapper, RoleDept> {

    @Autowired
    private RoleDeptMapper roleDeptMapper;

    /**
     * 根据部门ID查询
     *
     * @param deptId 部门ID
     * @return List
     */
    public List<RoleDept> getRoleByDeptId(String deptId) {
        return roleDeptMapper.getByDeptId(deptId);
    }
}
