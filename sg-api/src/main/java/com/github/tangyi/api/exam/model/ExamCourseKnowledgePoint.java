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
 * 章节知识点 exam_course_knowledge_point
 */
@Data
@Table(name = "exam_course_knowledge_point")
@EqualsAndHashCode(callSuper = true)
public class ExamCourseKnowledgePoint extends BaseEntity<ExamCourseKnowledgePoint> {

	/**
	 * 知识点标题
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
	 * 节ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "section_id")
	private Long sectionId;

	/**
	 * 知识点内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 学习时长
	 */
	@Column(name = "learn_hour")
	private int learnHour;

	/**
	 * 视频ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "video_id")
	private Long videoId;

	/**
	 * 视频名称
	 */
	@Column(name = "video_name")
	private String videoName;

	/**
	 * 内容类型
	 */
	@Column(name = "content_type")
	private Integer contentType;

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

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public int getLearnHour() {
		return learnHour;
	}

	public void setLearnHour(int learnHour) {
		this.learnHour = learnHour;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
