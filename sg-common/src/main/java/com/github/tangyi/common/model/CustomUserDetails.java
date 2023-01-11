package com.github.tangyi.common.model;

import com.github.tangyi.common.enums.LoginTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CustomUserDetails extends User {

	@Serial
	private static final long serialVersionUID = 1L;

	private String tenantCode;

	private Long id;

	private long startNanoTime;

	private LoginTypeEnum loginType;

	public CustomUserDetails(Long id, String username, String password,
			Collection<? extends GrantedAuthority> authorities, String tenantCode) {
		super(username, password, authorities);
		this.id = id;
		this.tenantCode = tenantCode;
	}

	public CustomUserDetails(String username, String password, boolean enabled,
			Collection<? extends GrantedAuthority> authorities, String tenantCode, Long id, long startNanoTime,
			LoginTypeEnum loginType) {
		super(username, password, enabled, true, true, true, authorities);
		this.tenantCode = tenantCode;
		this.id = id;
		this.startNanoTime = startNanoTime;
		this.loginType = loginType;
	}
}
