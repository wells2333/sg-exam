package com.github.tangyi.common.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SysConfigDto implements Serializable {

    @Serial
	private static final long serialVersionUID = 1L;

    private String uploadUrl;

    private String defaultAvatar;

    private String adminUser;
}
