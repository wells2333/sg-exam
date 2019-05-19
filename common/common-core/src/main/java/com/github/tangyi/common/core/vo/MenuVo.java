package com.github.tangyi.common.core.vo;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * @author tangyi
 * @date 2018-08-28 20:40
 */
@Data
public class MenuVo extends BaseEntity<MenuVo> {

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
     * 路径
     */
    private String path;

    /**
     * VUE页面
     */
    private String component;
}
