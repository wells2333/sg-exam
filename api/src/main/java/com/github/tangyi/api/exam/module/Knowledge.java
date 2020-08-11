package com.github.tangyi.api.exam.module;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 知识库
 *
 * @author tangyi
 * @date 2019/1/1 15:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Knowledge extends BaseEntity<Knowledge> {

    /**
     * 知识名称
     */
    @NotBlank(message = "知识名称不能为空")
    private String knowledgeName;

    /**
     * 知识描述
     */
    private String knowledgeDesc;

    /**
     * 附件ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long attachmentId;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空")
    private String status;
}
