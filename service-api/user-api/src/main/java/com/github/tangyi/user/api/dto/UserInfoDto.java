package com.github.tangyi.user.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangyi
 * @date 2018-09-13 17:18
 */
@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
     */
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
    private String identifier;

    /**
     * 密码
     */
    @JsonIgnore
    private String credential;

    /**
     * 姓名
     */
    private String name;

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
    private Integer sex;

    /**
     * 生日
     */
    private Date born;

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
    private Integer status;

    /**
     * 权限信息
     */
    private String[] permissions;

    /**
     * 角色信息
     */
    private String[] roles;

    /**
     * 系统编号
     */
    private String applicationCode;

    /**
     * 租户标识
     */
    private String tenantCode;
}
