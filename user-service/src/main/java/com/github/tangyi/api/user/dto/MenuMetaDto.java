package com.github.tangyi.api.user.dto;

import lombok.Data;

@Data
public class MenuMetaDto {

	private String title;

	private String icon;

	private Integer orderNo;

	private Boolean hideMenu;

	private String currentActiveMenu;

}
