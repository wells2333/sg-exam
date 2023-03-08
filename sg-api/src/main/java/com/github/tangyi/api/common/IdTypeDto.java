package com.github.tangyi.api.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class IdTypeDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	private Integer type;

	public static IdTypeDto of(Long id, Integer type) {
		IdTypeDto dto = new IdTypeDto();
		dto.setId(id);
		dto.setType(type);
		return dto;
	}
}