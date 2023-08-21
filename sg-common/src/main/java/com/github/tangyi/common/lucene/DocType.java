package com.github.tangyi.common.lucene;

public enum DocType {

	COURSE("course"), EXAM("exam"), OTHER("other");

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

	public static DocType matchByType(String type) {
		for (DocType item : DocType.values()) {
			if (item.type.equals(type)) {
				return item;
			}
		}
		return null;
	}
}
