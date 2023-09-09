package com.github.tangyi.common.lucene;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IndexCrudParam {

	private Long id;

	private DocType type;

	private List<String> contents;

	private long updateTime;

	private long clickCnt;

	private long joinCnt;

}
