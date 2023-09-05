package com.github.tangyi.common.service;

import com.github.tangyi.common.lucene.DocType;
import com.github.tangyi.common.lucene.IndexDoc;
import com.github.tangyi.common.lucene.IndexDocOperation;
import com.github.tangyi.common.lucene.LuceneMessageManager;
import com.github.tangyi.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;

public abstract class IndexCrudService extends BaseService {

	public void addIndex(Long id, DocType type, String... contents) {
		String content = StringUtils.join(contents, " ");
		IndexDoc doc = new IndexDoc();
		doc.setId(id.toString());
		doc.setContent(content);
		SpringContextHolder.getApplicationContext().getBean(LuceneMessageManager.class)
				.sendMessage(doc, type, IndexDocOperation.ADD);
	}

	public void updateIndex(Long id, DocType type, String... contents) {
		String content = StringUtils.join(contents, " ");
		IndexDoc doc = new IndexDoc();
		doc.setId(id.toString());
		doc.setContent(content);
		SpringContextHolder.getApplicationContext().getBean(LuceneMessageManager.class)
				.sendMessage(doc, type, IndexDocOperation.UPDATE);
	}

	public void deleteIndex(Long id, DocType type) {
		IndexDoc doc = new IndexDoc();
		doc.setId(id.toString());
		SpringContextHolder.getApplicationContext().getBean(LuceneMessageManager.class)
				.sendMessage(doc, type, IndexDocOperation.DEL);
	}
}
