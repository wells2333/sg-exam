package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_speech_synthesis")
public class SpeechSynthesis extends BaseEntity<SpeechSynthesis> {

	@Column(name = "name")
	private String name;

	@Column(name = "text")
	private String text;

	@Column(name = "attach_id")
	private Long attachId;

	@Column(name = "remark")
	private String remark;

	@Transient
	private String url;
}
