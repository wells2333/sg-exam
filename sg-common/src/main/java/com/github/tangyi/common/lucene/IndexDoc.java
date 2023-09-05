package com.github.tangyi.common.lucene;

import lombok.Data;

@Data
public class IndexDoc {

	private String id;

	private String type;

	private String content;

	public IndexDoc() {

	}
}
