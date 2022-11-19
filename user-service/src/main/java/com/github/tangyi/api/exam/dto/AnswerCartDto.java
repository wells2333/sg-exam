package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 答题卡
 *
 * @author tangyi
 * @date 2019/10/10 21:23
 */
@Data
public class AnswerCartDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ToStringSerializer.class)
	private List<Long> subjectIds;

}
