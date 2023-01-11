package com.github.tangyi.api.exam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 语音题目
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_speech")
public class SubjectSpeech extends BaseSubject<SubjectSpeech> {

	/**
	 * 判分类型，1：自动判分，0：人工判分
	 */
	@Column(name = "judge_type")
	private Integer judgeType;

}
