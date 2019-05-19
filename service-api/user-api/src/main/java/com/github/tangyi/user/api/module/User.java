package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 用户实体
 *
 * @author tangyi
 * @date 2018-08-25 15:30
 */
@Data
public class User extends BaseEntity<User> {

    private String name;

    private String username;

    private String password;

    private String salt;

    private String phone;

    private String avatar;

    private String avatarId;

    private String email;

    private String sex;

    private String born;

    private String remark;

    private String status;

    private String deptName;

    private String deptId;

    /**
     * 角色列表
     */
    private List<Role> roleList;

    /**
     * 角色
     */
    private List<String> role;
}
