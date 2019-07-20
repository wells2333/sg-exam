package com.github.tangyi.user.api.dto;


import com.github.tangyi.user.api.module.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * userDto
 *
 * @author tangyi
 * @date 2018/8/26 14:36
 */
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
     */
    @ApiModelProperty(value = "授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ", dataType = "Integer", example = "1")
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
    @ApiModelProperty(value = "账号唯一标识，如用户名、手机号", example = "git")
    private String identifier;

    /**
     * 密码凭证，跟授权类型有关，如密码、第三方系统的token等
     */
    @ApiModelProperty(value = "密码，需要使用AES加密", example = "lBTqrKS0kZixOFXeZ0HRng==")
    private String credential;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色", hidden = true)
    private List<String> role;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private String deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", hidden = true)
    private String deptName;

    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码", hidden = true)
    private String oldPassword;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", hidden = true)
    private String newPassword;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", example = "git")
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", example = "15521089184")
    @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    private String phone;

    /**
     * 头像id
     */
    @ApiModelProperty(value = "头像id", hidden = true)
    private String avatarId;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL", hidden = true)
    private String avatarUrl;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", example = "1633736729@qq.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别，0：男，1：女", dataType = "Integer", example = "0")
    private Integer sex;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期", dataType = "Date")
    private Date born;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", example = "git")
    private String userDesc;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态，0：启用，1：禁用", example = "0")
    private Integer status;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表", hidden = true)
    private List<Role> roleList;

    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", example = "EXAM")
    private String applicationCode;

    /**
     * 租户标识
     */
    @ApiModelProperty(value = "租户标识", example = "gitee")
    private String tenantCode;

    /**
     * 引导注册人
     */
    @ApiModelProperty(value = "引导注册人")
    private String parentUid;

    /**
     * 乡/镇
     */
    @ApiModelProperty(value = "乡/镇")
    private String streetId;

    /**
     * 县
     */
    @ApiModelProperty(value = "县")
    private String countyId;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String cityId;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String provinceId;

    /**
     * 最近登录时间
     */
    @ApiModelProperty(value = "最近登录时间", hidden = true)
    private Date loginTime;

    /**
     * 用户归档时间
     */
    @ApiModelProperty(value = "用户归档时间", hidden = true)
    private Date lockTime;

    /**
     * 微信
     */
    @ApiModelProperty(value = "微信")
    private String wechat;
}
