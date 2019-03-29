package com.github.tangyi.user.api.dto;


import com.github.tangyi.user.api.module.User;

import java.util.List;

/**
 * userDto
 *
 * @author tangyi
 * @date 2018/8/26 14:36
 */
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

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
