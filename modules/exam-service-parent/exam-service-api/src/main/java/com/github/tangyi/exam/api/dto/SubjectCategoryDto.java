package com.github.tangyi.exam.api.dto;

import com.github.tangyi.common.core.persistence.TreeEntity;
import com.github.tangyi.exam.api.module.SubjectCategory;
import lombok.Data;

/**
 * @author tangyi
 * @date 2018/12/4 22:04
 */
@Data
public class SubjectCategoryDto extends TreeEntity<SubjectCategoryDto> {

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

    public SubjectCategoryDto(SubjectCategory subjectCategory) {
        this.id = subjectCategory.getId();
        this.categoryName = subjectCategory.getCategoryName();
        this.categoryDesc = subjectCategory.getCategoryDesc();
        this.parentId = subjectCategory.getParentId();
        this.sort = Integer.parseInt(subjectCategory.getSort());
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
