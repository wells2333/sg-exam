package com.github.tangyi.common.lucene;

import lombok.Data;

@Data
public class IndexDoc {

	private String id;

	private String type;

	private String content;

	private long updateTime;

	private long clickCnt;

	private long joinCnt;

	public IndexDoc() {

	}
}
