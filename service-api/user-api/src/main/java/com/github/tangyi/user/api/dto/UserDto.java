package com.github.tangyi.user.api.dto;


import com.github.tangyi.user.api.module.User;
import lombok.Data;

import java.util.List;

/**
 * userDto
 *
 * @author tangyi
 * @date 2018/8/26 14:36
 */
@Data
public class UserDto extends User {

    /**
     * 角色
     */
    private List<String> role;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
