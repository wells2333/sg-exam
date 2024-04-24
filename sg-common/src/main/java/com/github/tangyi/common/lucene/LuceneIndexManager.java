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

package com.github.tangyi.common.lucene;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.github.tangyi.common.constant.PageConstant;
import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LuceneIndexManager {

	private static final String LUCENE_INDEX_DIR = EnvUtils.getValue("LUCENE_INDEX_DIR");
	private static final int LUCENE_INDEX_DOC_STATS_DELAY_SECOND = EnvUtils.getInt(
			"LUCENE_INDEX_DOC_STATS_DELAY_SECOND", 30);
	private static final Set<String> VALID_SORT_FIELDS = Sets.newHashSet(DocField.UPDATE_TIME, DocField.CLICK_CNT,
			DocField.JOIN_CNT);

	private final String indexDir;
	private final Directory directory;
	private final IndexWriter indexWriter;
	private final Analyzer analyzer;
	private final AtomicInteger docNum;
	private SearcherManager searcherManager;
	private ScheduledExecutorService docStatsExecutor;
	private RunnableScheduledFuture<?> docStatsTask;

	private static final class LuceneIndexManagerInstance {
		private static final LuceneIndexManager instance = new LuceneIndexManager();
	}

	private static final class DocField {
		private static final String ID = "id";
		private static final String TYPE = "type";
		private static final String CONTENT = "content";
		private static final String UPDATE_TIME = "update_time";
		private static final String CLICK_CNT = "click_cnt";
		private static final String JOIN_CNT = "join_cnt";
	}

	public static LuceneIndexManager getInstance() {
		return LuceneIndexManagerInstance.instance;
	}

	public LuceneIndexManager() {
		try {
			this.indexDir = getLuceneIndexDir();
			log.info("Lucene index dir: {}", indexDir);
			this.directory = FSDirectory.open(new File(indexDir).toPath());
			// 使用 IK 分词器
			this.analyzer = new IKAnalyzer();
			this.docNum = new AtomicInteger(0);
			this.indexWriter = new IndexWriter(directory, new IndexWriterConfig(this.analyzer));
			// 清空索引
			this.indexWriter.deleteAll();
			this.initSearcherManager();
			this.initDocStatTask();
		} catch (Exception e) {
			log.error("Failed to init LuceneIndexManager.", e);
			throw new RuntimeException(e);
		}
		this.addShutdownHook();
	}

	// For init
	public void init() {
		// Do nothing
	}

	public void addDocument(IndexDoc indexDoc, DocType type) throws IOException {
		this.indexWriter.addDocument(toDocument(indexDoc, type));
		this.indexWriter.commit();
	}

	public void updateDocument(IndexDoc doc, DocType type) throws IOException {
		// 先删除再新增
		this.deleteDocument(doc, type);
		this.addDocument(doc, type);
	}

	public void deleteDocument(IndexDoc doc, DocType type) throws IOException {
		this.indexWriter.deleteDocuments(new Term(DocField.ID, doc.getId()), new Term(DocField.TYPE, type.getType()));
	}

	public List<IndexDoc> search(@Nullable DocType type, String q, int size) throws IOException, ParseException {
		return this.search(type, q, size, null, null);
	}

	public List<IndexDoc> search(@Nullable DocType type, String q, int size, String sortField, String sortOrder)
			throws IOException, ParseException {
		List<IndexDoc> indexDocs = Lists.newArrayList();
		this.searcherManager.maybeRefresh();
		IndexSearcher indexSearcher = searcherManager.acquire();
		TopDocs topDocs = this.doSearch(indexSearcher, type, q, size, sortField, sortOrder);
		ScoreDoc[] arr = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : arr) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			IndexDoc indexDoc = new IndexDoc();
			indexDoc.setId(document.get(DocField.ID));
			indexDoc.setType(document.get(DocField.TYPE));
			indexDoc.setContent(document.get(DocField.CONTENT));
			indexDocs.add(indexDoc);
		}
		return indexDocs;
	}

	public void destroy() throws IOException {
		log.info("Start to destroy lucene index manager.");
		if (this.docStatsTask != null) {
			this.docStatsTask.cancel(true);
		}
		if (this.docStatsExecutor != null) {
			this.docStatsExecutor.shutdownNow();
		}
		if (this.indexWriter != null) {
			this.indexWriter.close();
		}
		if (this.directory != null) {
			this.directory.close();
		}
		if (StringUtils.isNotEmpty(indexDir)) {
			FileUtils.deleteQuietly(new File(indexDir));
			log.info("Delete index dir finished: {}", indexDir);
		}
		log.info("Destroy lucene index manager finished.");
	}

	private String getLuceneIndexDir() {
		String dir = LUCENE_INDEX_DIR;
		if (StringUtils.isEmpty(dir)) {
			dir = System.getProperty("java.io.tmpdir");
		}
		dir = dir + File.separator + "lucene/sg_index";
		return dir;
	}

	private Document toDocument(IndexDoc indexDoc, DocType type) {
		Document document = new Document();
		document.add(new StringField(DocField.ID, indexDoc.getId(), Field.Store.YES));
		document.add(new StringField(DocField.TYPE, type.getType(), Field.Store.YES));
		document.add(new TextField(DocField.CONTENT, indexDoc.getContent(), Field.Store.YES));
		document.add(new LongPoint(DocField.UPDATE_TIME, indexDoc.getUpdateTime()));
		document.add(new LongPoint(DocField.CLICK_CNT, indexDoc.getClickCnt()));
		document.add(new LongPoint(DocField.JOIN_CNT, indexDoc.getJoinCnt()));
		return document;
	}

	private void initSearcherManager() throws IOException {
		this.searcherManager = new SearcherManager(indexWriter, false, false, new SearcherFactory());
		ControlledRealTimeReopenThread<?> thread = new ControlledRealTimeReopenThread<>(indexWriter, searcherManager,
				5.0, 0.025);
		thread.setDaemon(true);
		thread.setName("update-index-reader");
		thread.start();
	}

	private void initDocStatTask() {
		this.docStatsExecutor = Executors.newScheduledThreadPool(1,
				new ThreadFactoryBuilder().setNamePrefix("lucene-doc-stats").build());
		this.docStatsTask = (RunnableScheduledFuture<?>) this.docStatsExecutor.scheduleWithFixedDelay(() -> {
			IndexWriter.DocStats docStats = this.indexWriter.getDocStats();
			int currentDocNum = docStats.numDocs;
			if (currentDocNum != docNum.get()) {
				docNum.set(currentDocNum);
				log.info("Lucene index doc stats, numDocs: {}", currentDocNum);
			}
		}, 3, LUCENE_INDEX_DOC_STATS_DELAY_SECOND, TimeUnit.SECONDS);
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				destroy();
			} catch (Exception e) {
				log.error("Failed to destroy lucene index manager", e);
			}
		}));
	}

	private Query parseQ(DocType type, String q) throws ParseException {
		Query query;
		QueryParser queryParser = new QueryParser(DocField.CONTENT, this.analyzer);
		if (type != null) {
			BooleanClause typeTerm = new BooleanClause(new TermQuery(new Term(DocField.TYPE, type.getType())),
					BooleanClause.Occur.MUST);
			BooleanClause queryTerm = new BooleanClause(queryParser.parse(q), BooleanClause.Occur.MUST);
			query = new BooleanQuery.Builder().add(typeTerm).add(queryTerm).build();
		} else {
			query = queryParser.parse(q);
		}
		return query;
	}

	private TopDocs doSearch(IndexSearcher indexSearcher, @Nullable DocType type, String q, int size, String sortField,
			String sortOrder) throws ParseException, IOException {
		TopDocs topDocs;
		if (sortField != null && sortOrder != null) {
			this.validSortParams(sortField, sortOrder);
			boolean reverse = "desc".equals(sortOrder);
			Sort sort = new Sort(new SortField(sortField, SortField.Type.LONG, reverse));
			topDocs = indexSearcher.search(parseQ(type, q), size, sort);
		} else {
			topDocs = indexSearcher.search(parseQ(type, q), size);
		}
		return topDocs;
	}

	private void validSortParams(String sortField, String sortOrder) {
		if (!VALID_SORT_FIELDS.contains(sortField)) {
			throw new IllegalArgumentException("Invalid sortField: " + sortField);
		}
		if (!PageConstant.VALID_SORT_ORDER.contains(sortOrder)) {
			throw new IllegalArgumentException("Invalid sortOrder: " + sortOrder);
		}
	}
}
