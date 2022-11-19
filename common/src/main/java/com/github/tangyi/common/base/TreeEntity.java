package com.github.tangyi.common.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形实体
 *
 * @author tangyi
 * @date 2018-09-13 20:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class TreeEntity<T> extends BaseEntity<T> {

    private static final long serialVersionUID = 7265456426423066026L;

    /**
     * code
     */
    protected String code;

    /**
     * 父节点
     */
    protected T parent;

    /**
     * 父节点id
     */
    protected Long parentId;

    /**
     * 排序号
     */
    protected Integer sort;

    /**
     * 子节点
     */
    protected List<TreeEntity> children = new ArrayList<>();

    public void add(TreeEntity node) {
        children.add(node);
    }
}

