package com.github.tangyi.user.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tangyi
 * @date 2018-09-13 17:18
 */
@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像对应的附件id
     */
    private String avatarId;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private String born;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态，0-启用，1-禁用
     */
    private String status;

    /**
     * 权限信息
     */
    private String[] permissions;

    /**
     * 角色信息
     */
    private String[] roles;
}
