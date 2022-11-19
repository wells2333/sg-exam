package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户授权信息
 *
 * @author tangyi
 * @date 2019/07/03 11:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_user_auths")
public class UserAuths extends BaseEntity<UserAuths> {

    /**
     * 用户id
     */
	@Column(name = "user_id")
    private Long userId;

    /**
     * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
     */
	@Column(name = "identity_type")
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
	@Column(name = "identifier")
    private String identifier;

    /**
     * 密码凭证，跟授权类型有关，如密码、第三方系统的token等
     */
	@Column(name = "credential")
    private String credential;

}
