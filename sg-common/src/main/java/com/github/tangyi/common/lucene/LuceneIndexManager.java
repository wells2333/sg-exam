package com.github.tangyi.common.lucene;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LuceneIndexManager {

	private static final String LUCENE_INDEX_DIR = EnvUtils.getValue("LUCENE_INDEX_DIR");

	private static final int LUCENE_INDEX_SEGMENT_NUM = EnvUtils.getInt("LUCENE_INDEX_SEGMENT_NUM", 1);

	private static final int LUCENE_INDEX_DOC_STATS_DELAY_SECOND = EnvUtils.getInt(
			"LUCENE_INDEX_DOC_STATS_DELAY_SECOND", 30);

	private String indexDir;

	private Directory directory;

	private IndexWriter indexWriter;

	private DirectoryReader directoryReader;

	private Analyzer analyzer;

	private ScheduledExecutorService docStatsExecutor;

	private RunnableScheduledFuture<?> docStatsTask;

	private static final class LuceneIndexManagerInstance {
		private static final LuceneIndexManager instance = new LuceneIndexManager();
	}

	private static final class DocField {
		static String ID = "id";
		static String TYPE = "type";
		static String CONTENT = "content";
	}

	public static LuceneIndexManager getInstance() {
		return LuceneIndexManagerInstance.instance;
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
		return document;
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

	public LuceneIndexManager() {
		try {
			this.indexDir = getLuceneIndexDir();
			log.info("Lucene index dir: {}", indexDir);
			this.directory = FSDirectory.open(new File(indexDir).toPath());
			// 使用 IK 分词器
			this.analyzer = new IKAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
			this.indexWriter = new IndexWriter(directory, config);
			this.commitAndForceMerge();
			this.directoryReader = DirectoryReader.open(directory);
			// 文档监控
			this.docStatsExecutor = Executors.newScheduledThreadPool(1,
					new ThreadFactoryBuilder().setNamePrefix("lucene-doc-stats").build());
			this.docStatsTask = (RunnableScheduledFuture<?>) this.docStatsExecutor.scheduleWithFixedDelay(() -> {
				IndexWriter.DocStats docStats = this.indexWriter.getDocStats();
				log.info("Lucene index doc stats, numDocs: {}", docStats.numDocs);
			}, 3, LUCENE_INDEX_DOC_STATS_DELAY_SECOND, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Failed to init LuceneIndexManager.", e);
		}
		addShutdownHook();
	}

	public void addDocument(IndexDoc indexDoc, DocType type) throws IOException {
		this.indexWriter.addDocument(toDocument(indexDoc, type));
		this.flushAndCommit();
	}

	public void updateDocument(IndexDoc doc, DocType type) throws IOException {
		// 先删除再新增
		this.deleteDocument(doc, type);
		this.addDocument(doc, type);
	}

	public void deleteDocument(IndexDoc doc, DocType type) throws IOException {
		this.indexWriter.deleteDocuments(new Term(DocField.ID, doc.getId()), new Term(DocField.TYPE, type.getType()));
	}

	public void flushAndCommit() throws IOException {
		this.indexWriter.flush();
		this.indexWriter.commit();
	}

	public void commitAndForceMerge() throws IOException {
		this.flushAndCommit();
		this.forceMerge();
	}

	public void forceMerge() throws IOException {
		int segment = LUCENE_INDEX_SEGMENT_NUM;
		log.info("Start to forceMerge, segment: {}", segment);
		this.indexWriter.forceMerge(segment);
		log.info("ForceMerge done.");
	}

	public List<IndexDoc> search(DocType type, String query, int size) throws IOException, ParseException {
		QueryParser queryParser = new QueryParser(DocField.CONTENT, this.analyzer);
		BooleanClause typeTerm = new BooleanClause(new TermQuery(new Term(DocField.TYPE, type.getType())),
				BooleanClause.Occur.MUST);
		BooleanClause queryTerm = new BooleanClause(queryParser.parse(query), BooleanClause.Occur.MUST);
		BooleanQuery booleanQuery = new BooleanQuery.Builder().add(typeTerm).add(queryTerm).build();
		DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(this.directoryReader);
		List<IndexDoc> indexDocs = Lists.newArrayList();
		IndexSearcher indexSearcher = new IndexSearcher(newDirectoryReader);
		TopDocs topDocs = indexSearcher.search(booleanQuery, size);
		ScoreDoc[] arr = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : arr) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			indexDocs.add(
					IndexDoc.builder().id(document.get(DocField.ID)).content(document.get(DocField.CONTENT)).build());
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
}
