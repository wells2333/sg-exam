package com.github.tangyi.common.service;

import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.lucene.DocType;
import com.github.tangyi.common.lucene.IndexDoc;
import com.github.tangyi.common.lucene.LuceneIndexManager;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public abstract class IndexCrudService extends BaseService {

	public void addIndex(Long id, DocType type, String... contents) {
		try {
			String content = StringUtils.join(contents, " ");
			IndexDoc doc = IndexDoc.builder().id(id.toString()).content(content).build();
			LuceneIndexManager.getInstance().addDocument(doc, type);
		} catch (IOException e) {
			throw new CommonException(e);
		}
	}

	public void updateIndex(Long id, DocType type, String... contents) {
		try {
			String content = StringUtils.join(contents, " ");
			IndexDoc doc = IndexDoc.builder().id(id.toString()).content(content).build();
			LuceneIndexManager.getInstance().updateDocument(doc, type);
		} catch (IOException e) {
			throw new CommonException(e);
		}
	}

	public void deleteIndex(Long id, DocType type) {
		try {
			IndexDoc doc = IndexDoc.builder().id(id.toString()).build();
			LuceneIndexManager.getInstance().deleteDocument(doc, type);
		} catch (IOException e) {
			throw new CommonException(e);
		}
	}
}
