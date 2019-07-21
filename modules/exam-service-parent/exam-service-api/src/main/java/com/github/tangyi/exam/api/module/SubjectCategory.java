package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 题目分类
 *
 * @author tangyi
 * @date 2018-12-04 11:18
 */
@Data
public class SubjectCategory extends BaseEntity<SubjectCategory> {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
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

    /**
     * 类型: 0-私共,1-公有
     */
    private String type;
}
