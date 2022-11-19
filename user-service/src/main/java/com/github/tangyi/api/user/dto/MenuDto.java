package com.github.tangyi.api.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.common.base.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;

/**
 * 菜单dto
 *
 * @author tangyi
 * @date 2018-09-13 20:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class MenuDto extends TreeEntity<MenuDto> {

	/**
	 * 父菜单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	private String icon;

	private String name;

	private String path;

	private String redirect;

	private String component;

	private String permission;

	private String code;

	private Byte type;

	private String label;

	private String[] roles;

	private String remark;

	private Date createDate;

	private Integer isExt;

	private Integer keepalive;

	private Boolean hideMenu;

	private String currentActiveMenu;

	private MenuMetaDto meta;

	public MenuDto() {

	}

	public MenuDto(Menu menu) {
		try {
			BeanUtils.copyProperties(this, menu);
		} catch (Exception e) {
			log.error("copy properties failed", e);
		}
		this.meta = new MenuMetaDto();
		this.meta.setIcon(this.icon);
		this.meta.setTitle(this.name);
		this.meta.setOrderNo(this.sort);
		this.meta.setHideMenu(this.hideMenu);
		this.meta.setCurrentActiveMenu(this.currentActiveMenu);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}
}
