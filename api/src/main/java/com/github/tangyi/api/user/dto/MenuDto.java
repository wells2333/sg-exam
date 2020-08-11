package com.github.tangyi.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tangyi.api.user.module.Menu;
import com.github.tangyi.common.persistence.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单dto
 *
 * @author tangyi
 * @date 2018-09-13 20:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuDto extends TreeEntity<MenuDto> {

    /**
     * 父菜单ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private String icon;

    private String name;

    private String url;

    private String redirect;

    private boolean spread = false;

    private String path;

    private String component;

    private String authority;

    private String code;

    private Integer type;

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
