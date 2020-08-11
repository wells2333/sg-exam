package com.github.tangyi.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long avatarId;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

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

    /**
     * 引导注册人
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentUid;

    /**
     * 乡/镇
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long streetId;

    /**
     * 县
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long countyId;

    /**
     * 城市
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cityId;

    /**
     * 省份
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long provinceId;

    /**
     * 最近登录时间
     */
    private Date loginTime;

    /**
     * 用户归档时间
     */
    private Date lockTime;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 个性签名
     */
    private String signature;
}
