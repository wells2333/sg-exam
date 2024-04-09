package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

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

	protected Date updateTime;

	private String speechId;
	private String speechUrl;

}
