package com.github.tangyi.api.user.dto;

import lombok.Data;

/**
 *
 * @author tangyi
 * @date 2022/3/21 10:28 下午
 */
@Data
public class MenuMetaDto {

	private String title;

	private String icon;

	private Integer orderNo;

	private Boolean hideMenu;

	private String currentActiveMenu;

}
