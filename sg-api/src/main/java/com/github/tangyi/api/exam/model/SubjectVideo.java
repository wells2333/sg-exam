package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 视频题目
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_video")
public class SubjectVideo extends BaseSubject<SubjectVideo> {

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "video_id")
	private Long videoId;

	/**
	 * 判分类型，0：自动判分，1：人工判分
	 */
	@Column(name = "judge_type")
	private Integer judgeType;

	@Column(name = "video_name")
	private String videoName;
}
