package com.github.tangyi.api.user.enums;

public enum MessageType {

	STATION_LETTER(0, "站内信"),
	EMAIL(1, "邮件"),
	TODO(2, "待办"),
	NOTICE(3, "公告");

	private Integer type;

	private String desc;

	MessageType(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
