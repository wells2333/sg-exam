package com.github.tangyi.common.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class TreeEntity<T> extends BaseEntity<T> {

    @Serial
	private static final long serialVersionUID = 7265456426423066026L;

    protected String code;

    protected T parent;

    protected Long parentId;

    protected Integer sort;

    protected List<TreeEntity> children = new ArrayList<>();

    public void add(TreeEntity node) {
        children.add(node);
    }
}

