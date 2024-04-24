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

package com.github.tangyi.user;

import com.github.tangyi.common.lucene.DocType;
import com.github.tangyi.common.lucene.IndexDoc;
import com.github.tangyi.common.lucene.LuceneIndexManager;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class LuceneIndexManagerTests {

	@Test
	public void testAddDocument() throws IOException, ParseException {
		IndexDoc doc = new IndexDoc();
		doc.setId("1");
		doc.setContent("test 测试");
		LuceneIndexManager.getInstance().addDocument(doc, DocType.COURSE);
		List<IndexDoc> docs = LuceneIndexManager.getInstance().search(DocType.COURSE, "测试", 50);
		System.out.println(docs);
		LuceneIndexManager.getInstance().destroy();
	}
}
