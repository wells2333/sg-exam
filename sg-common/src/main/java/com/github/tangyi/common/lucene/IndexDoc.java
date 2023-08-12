package com.github.tangyi.common.lucene;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexDoc {

	private String id;

	private String content;
}
