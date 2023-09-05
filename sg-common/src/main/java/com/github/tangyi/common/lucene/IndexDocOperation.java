package com.github.tangyi.common.lucene;

public enum IndexDocOperation {

	ADD(1), UPDATE(2), DEL(3);

	private int type;

	IndexDocOperation(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
