package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 部门
 *
 * @author tangyi
 * @date 2018/8/26 22:25
 */
@Data
public class Dept extends BaseEntity<Dept> {

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
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
     * 排序
     */
    private String sort;

    /**
     * 状态， 0-启用，1-禁用
     */
    @NotBlank(message = "部门状态不能为空")
    private String status;
}
