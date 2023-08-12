package com.github.tangyi.common.lucene;

public enum DocType {

	COURSE("course"), EXAM("exam");

	private String type;

	DocType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
