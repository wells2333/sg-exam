package com.github.tangyi.common.core.vo;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 用户Vo
 *
 * @author tangyi
 * @date 2018-08-25 13:57
 */
@Data
public class UserVo extends BaseEntity<UserVo> {

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 随机盐
     */
    private String salt;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 头像对应的附件id
     */
    private String avatarId;

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
     * 角色列表
     */
    private List<Role> roleList;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态，0-启用，1-禁用
     */
    private String status;

}

