package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * 题目分类
 *
 * @author tangyi
 * @date 2018-12-04 11:18
 */
public class SubjectCategory extends BaseEntity<SubjectCategory> {

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类描述
     */
    private String categoryDesc;

    /**
     * 父分类id
     */
    private String parentId;

    /**
     * 排序号
     */
    private String sort;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
