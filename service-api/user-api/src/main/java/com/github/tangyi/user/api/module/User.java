package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String salt;

    @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    private String phone;

    private String avatarId;

    @Email(message = "邮箱格式不正确")
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
