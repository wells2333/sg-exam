package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectDto extends BaseEntity<SubjectDto> {

	/**
	 * 考试ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examinationId;

	/**
	 * 考试记录ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examinationRecordId;

	/**
	 * 题目分类ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long categoryId;

	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 题目名称
	 */
	private String subjectName;

	/**
	 * 题目类型
	 */
	private Integer type;

	private String typeLabel;

	/**
	 * 选择题类型
	 */
	private Integer choicesType;

	/**
	 * 分值
	 */
	private Double score;


	/**
	 * 解析
	 */
	private String analysis;

	/**
	 * 难度等级
	 */
	private Integer level;

	/**
	 * 答题
	 */
	private Answer answer;

	/**
	 * 选项列表
	 */
	private List<SubjectOption> options;

	private Integer sort;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long speechId;

	private String speechUrl;

	/**
	 * 题目总数
	 */
	private Integer total;

	/**
	 * 是否还有题目
	 */
	private boolean hasMore;

	/**
	 * 视频ID
	 */
	private Long videoId;

	/**
	 * 视频URL
	 */
	private String videoUrl;

	/**
	 * 图片ID
	 */
	private Long imageId;

	/**
	 * 判分类型
	 */
	private Integer judgeType;

	/**
	 * 视频名称
	 */
	private String videoName;

	/**
	 * 是否已收藏
	 */
	private boolean favorite;
}
