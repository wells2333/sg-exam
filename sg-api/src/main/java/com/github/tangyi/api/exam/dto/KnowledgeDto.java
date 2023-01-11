package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeDto extends BaseEntity<KnowledgeDto> {

	/**
	 * 知识名称
	 */
	private String knowledgeName;

	/**
	 * 知识描述
	 */
	private String knowledgeDesc;

	/**
	 * 附件ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long attachmentId;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 附件名称
	 */
	private String attachName;

	/**
	 * 附件大小
	 */
	private String attachSize;
}
