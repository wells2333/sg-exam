package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 课程节 exam_course_section
 *
 * @author tangyi
 * @date 2022-11-21
 */
@Data
@Table(name = "exam_course_section")
@EqualsAndHashCode(callSuper = true)
public class ExamCourseSection extends BaseEntity<ExamCourseSection> {

	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 序号
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "sort")
	private Long sort;

	/**
	 * 章ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "chapter_id")
	private Long chapterId;

	/**
	 * 时长
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "learn_hour")
	private Long learnHour;

	/**
	 * 视频ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "video_id")
	private Long videoId;

	/**
	 * 节描述
	 */
	@Column(name = "section_desc")
	private String sectionDesc;

	/**
	 * 视频名称
	 */
	@Column(name = "video_name")
	private String videoName;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Long getSort() {
		return sort;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

	public Long getChapterId() {
		return chapterId;
	}

	public void setLearnHour(Long learnHour) {
		this.learnHour = learnHour;
	}

	public Long getLearnHour() {
		return learnHour;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Long getVideoId() {
		return videoId;
	}

	public String getSectionDesc() {
		return sectionDesc;
	}

	public void setSectionDesc(String sectionDesc) {
		this.sectionDesc = sectionDesc;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
