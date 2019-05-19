package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 菜单
 *
 * @author tangyi
 * @date 2018/8/26 22:21
 */
@Data
public class Menu extends BaseEntity<Menu> {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单权限标识
     */
    private String permission;

    /**
     * url
     */
    private String url;

    /**
     * 重定向url
     */
    private String redirect;

    /**
     * 父菜单ID
     */
    private String parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序号
     */
    private String sort;

    /**
     * 类型
     */
    private String type;

    /**
     * 模块
     */
    private String component;

    /**
     * 路径
     */
    private String path;

    /**
     * 备注
     */
    private String remark;
}
