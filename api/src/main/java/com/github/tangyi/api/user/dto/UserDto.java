package com.github.tangyi.api.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tangyi.api.user.module.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * userDto
 *
 * @author tangyi
 * @date 2018/8/26 14:36
 */
@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "UserDto", description = "用户返回结果")
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> role = new ArrayList<>();

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long avatarId;

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
    private List<Role> roleList = new ArrayList<>();

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentUid;

    /**
     * 乡/镇
     */
    @ApiModelProperty(value = "乡/镇")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long streetId;

    /**
     * 县
     */
    @ApiModelProperty(value = "县")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long countyId;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cityId;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long provinceId;

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

    /**
     * 家庭角色
     */
    @ApiModelProperty(value = "家庭角色，0：爸爸，1：妈妈，2：爷爷，3：奶奶，5：外公，6：外婆", example = "0")
    private Integer familyRole;

    /**
     * 个性签名
     */
    @ApiModelProperty(value = "个性签名")
    private String signature;
}
