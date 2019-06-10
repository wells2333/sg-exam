package com.github.tangyi.user.api.dto;

import com.github.tangyi.common.core.vo.UserVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tangyi
 * @date 2018-09-13 17:18
 */
@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private UserVo user;

    /**
     * 权限信息
     */
    private String[] permissions;

    /**
     * 角色信息
     */
    private String[] roles;
}
