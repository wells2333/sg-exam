package com.github.tangyi.common.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserToken implements Serializable {

	public static final long serialVersionUID = -1L;

	private String id;

	private String tenantCode;

	private Long userId;

	private String identify;

	private String role;

	private String loginType;

	private String ip;

	private String userAgent;

	private LocalDateTime issuedAt;

	private LocalDateTime expiresAt;

	private boolean remember;

}
