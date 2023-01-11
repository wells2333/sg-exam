package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CardDto {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long subjectId;

	private Integer sort;
}
