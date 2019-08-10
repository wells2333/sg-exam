package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 用户基本信息
 *
 * @author tangyi
 * @date 2018-08-25 15:30
 */
@Data
public class User extends BaseEntity<User> {

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    private String phone;

    /**
     * 头像id
     */
    private String avatarId;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private Date born;

    /**
     * 描述
     */
    private String userDesc;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 角色列表
     */
    private List<Role> roleList;

    /**
     * 角色
     */
    private List<String> role;

    /**
     * 引导注册人
     */
    private String parentUid;

    /**
     * 乡/镇
     */
    private String streetId;

    /**
     * 县
     */
    private String countyId;

    /**
     * 城市
     */
    private String cityId;

    /**
     * 省份
     */
    private String provinceId;

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
     * 家庭角色，参考UserStudentConstant
     */
    private Integer familyRole;
}
