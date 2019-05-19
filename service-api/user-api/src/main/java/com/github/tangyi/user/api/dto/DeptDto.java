package com.github.tangyi.user.api.dto;

import com.github.tangyi.common.core.persistence.TreeEntity;
import com.github.tangyi.user.api.module.Dept;
import lombok.Data;

/**
 * 部门dto
 *
 * @author tangyi
 * @date 2018-10-25 12:49
 */
@Data
public class DeptDto extends TreeEntity<DeptDto> {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门描述
     */
    private String deptDesc;

    /**
     * 部门负责人
     */
    private String deptLeader;

    /**
     * 父部门ID
     */
    private String parentId;

    /**
     * 状态， 0-启用，1-禁用
     */
    private String status;

    public DeptDto(Dept dept) {
        this.id = dept.getId();
        this.deptName = dept.getDeptName();
        this.deptDesc = dept.getDeptDesc();
        this.deptLeader = dept.getDeptLeader();
        this.parentId = dept.getParentId();
        this.status = dept.getStatus();
        this.sort = Integer.parseInt(dept.getSort());
        this.creator = dept.getCreator();
        this.createDate = dept.getCreateDate();
        this.modifier = dept.getModifier();
        this.modifyDate = dept.getModifyDate();
    }
}
