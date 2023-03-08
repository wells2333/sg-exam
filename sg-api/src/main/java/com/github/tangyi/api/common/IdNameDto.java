package com.github.tangyi.api.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class IdNameDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;
}
