/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
