package com.github.tangyi.user.api.dto;

import com.github.tangyi.common.core.persistence.TreeEntity;
import com.github.tangyi.user.api.module.Menu;
import lombok.Data;

/**
 * 菜单dto
 * @author tangyi
 * @date 2018-09-13 20:39
 */
@Data
public class MenuDto extends TreeEntity<MenuDto> {

    /**
     * 父菜单ID
     */
    private String parentId;

    private String icon;

    private String name;

    private String url;

    private String redirect;

    private boolean spread = false;

    private String path;

    private String component;

    private String authority;

    private String code;

    private String type;

    private String label;

    private String[] roles;

    private String remark;

    public MenuDto(Menu menu) {
        this.id = menu.getId();
        this.parentId = menu.getParentId();
        this.icon = menu.getIcon();
        this.name = menu.getName();
        this.url = menu.getUrl();
        this.type = menu.getType();
        this.label = menu.getName();
        this.sort = Integer.parseInt(menu.getSort());
        this.component = menu.getComponent();
        this.path = menu.getPath();
        this.redirect = menu.getRedirect();
        this.remark = menu.getRemark();
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
