package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * 知识库
 *
 * @author tangyi
 * @date 2019/1/1 15:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_knowledge")
public class Knowledge extends BaseEntity<Knowledge> {

	/**
	 * 知识名称
	 */
	@NotBlank(message = "知识名称不能为空")
	@Column(name = "knowledge_name")
	private String knowledgeName;

	/**
	 * 知识描述
	 */
	@Column(name = "knowledge_desc")
	private String knowledgeDesc;

	/**
	 * 附件ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "attachment_id")
	private Long attachmentId;

	/**
	 * 状态
	 */
	@NotBlank(message = "状态不能为空")
	@Column(name = "status")
	private String status;
}
