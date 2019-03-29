package com.github.tangyi.exam.api.dto;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * @author tangyi
 * @date 2019/1/9 21:09
 */
public class SubjectBankDto extends BaseEntity<SubjectBankDto> {

    /**
     * 分类ID
     */
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
