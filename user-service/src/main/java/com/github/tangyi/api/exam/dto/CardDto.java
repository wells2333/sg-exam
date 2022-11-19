package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 *
 * @author tangyi
 * @date 2022/8/3 10:33 下午
 */
@Data
public class CardDto {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long subjectId;

	private Integer sort;
}
