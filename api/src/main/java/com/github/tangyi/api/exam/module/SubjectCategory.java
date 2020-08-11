package com.github.tangyi.api.exam.module;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 题目分类
 *
 * @author tangyi
 * @date 2018-12-04 11:18
 */
@EqualsAndHashCode(callSuper = true)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 类型: 0-私共,1-公有
     */
    private Integer type;
}
