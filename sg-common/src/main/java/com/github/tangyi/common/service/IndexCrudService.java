package com.github.tangyi.common.service;

import com.github.tangyi.common.lucene.*;
import com.github.tangyi.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;

public abstract class IndexCrudService extends BaseService {

	private static final String BLANK_SPACE = " ";

	public void addIndex(IndexCrudParam param) {
		String content = StringUtils.join(param.getContents(), BLANK_SPACE);
		IndexDoc doc = new IndexDoc();
		doc.setId(param.getId().toString());
		doc.setContent(content);
		doc.setUpdateTime(param.getUpdateTime());
		doc.setClickCnt(param.getClickCnt());
		doc.setJoinCnt(param.getJoinCnt());
		SpringContextHolder.getApplicationContext().getBean(LuceneMessageManager.class)
				.sendMessage(doc, param.getType(), IndexDocOperation.ADD);
	}

	public void updateIndex(IndexCrudParam param) {
		String content = StringUtils.join(param.getContents(), BLANK_SPACE);
		IndexDoc doc = new IndexDoc();
		doc.setId(param.getId().toString());
		doc.setContent(content);
		SpringContextHolder.getApplicationContext().getBean(LuceneMessageManager.class)
				.sendMessage(doc, param.getType(), IndexDocOperation.UPDATE);
	}

	public void deleteIndex(Long id, DocType type) {
		IndexDoc doc = new IndexDoc();
		doc.setId(id.toString());
		SpringContextHolder.getApplicationContext().getBean(LuceneMessageManager.class)
				.sendMessage(doc, type, IndexDocOperation.DEL);
	}
}
