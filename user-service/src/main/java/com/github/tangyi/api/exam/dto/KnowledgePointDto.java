package com.github.tangyi.api.exam.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author tangyi
 * @date 2022/12/9 10:06 下午
 */
@Data
public class KnowledgePointDto {

	@JsonSerialize(using = ToStringSerializer.class)
	protected Long id;

	private String title;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long sort;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long sectionId;

	private String content;

	private int learnHour;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long videoId;

	private String videoName;

	private Integer contentType;

	private String videoUrl;

	private String operator;

	@JSONField(format = BaseEntity.DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	protected Date updateTime;
}
