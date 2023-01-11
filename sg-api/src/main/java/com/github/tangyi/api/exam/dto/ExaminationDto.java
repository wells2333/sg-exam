package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.model.Examination;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ExaminationDto extends Examination {

	private Course course;

	/**
	 * 封面地址
	 */
	private String imageUrl;

	/**
	 * 考试类型名称
	 */
	private String typeLabel;

	/**
	 * 是否收藏
	 */
	private boolean favorite;

}
