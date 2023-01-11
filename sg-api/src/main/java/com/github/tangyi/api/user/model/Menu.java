package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_menu")
public class Menu extends BaseEntity<Menu> {

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String name;

	/**
	 * 类型 （1菜单，2权限)
	 */
	private Byte type;

	/**
	 * 组件路径
	 */
	private String component;

    /**
     * 路由地址
     */
    private String path;

	/**
	 * 重定向地址
	 */
	private String redirect;

	/**
	 * 权限标识
	 */
	private String permission;

    /**
     * 父菜单ID
     */
	@Column(name = "parent_id")
    private Long parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

	/**
	 * 是否外链
	 */
	@Column(name = "is_ext")
	private Integer isExt;

	/**
	 * 是否缓存
	 */
	private Integer keepalive;

	/**
	 * 是否显示，1：显示，0：不显示
	 */
	@Column(name = "hide_menu")
	private Boolean hideMenu;

	@Column(name = "current_active_menu")
	private String currentActiveMenu;
}
