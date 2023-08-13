package com.github.tangyi.user.controller;

import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.lucene.DocType;
import com.github.tangyi.common.lucene.IndexDoc;
import com.github.tangyi.common.lucene.LuceneIndexManager;
import com.github.tangyi.common.model.R;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "搜索")
@RequestMapping("/v1/search")
public class SearchController extends BaseController {

	@Data
	private static class SearchResItemList {
		private List<SearchResItem> items = Lists.newArrayList();
		private int total;
		private long tookMs;
	}

	@Data
	@Builder
	private static class SearchResItem {
		private String id;
		private String type;
	}

	@GetMapping("searchByType")
	@Operation(summary = "根据关键词搜索")
	public R<SearchResItemList> search(@RequestParam String itemType, @RequestParam String query,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(query), "query must not be empty");
		SearchResItemList res = new SearchResItemList();
		long startNs = System.nanoTime();
		try {
			DocType docType = DocType.matchByType(itemType);
			if (docType == null) {
				throw new IllegalArgumentException("docType not found");
			}
			List<IndexDoc> indexDocs = LuceneIndexManager.getInstance().search(docType, query, pageSize);
			if (CollectionUtils.isNotEmpty(indexDocs)) {
				for (IndexDoc indexDoc : indexDocs) {
					res.getItems().add(SearchResItem.builder().id(indexDoc.getId()).type(itemType).build());
				}
			}
			res.setTookMs(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs));
		} catch (Exception e) {
			log.error("Failed to search, itemType: {}, query: {}", itemType, query, e);
		} finally {
			log.info("[search-access] itemType: {}, query: {}, res size: {}, tookMs: {}", itemType, query,
					res.getItems().size(), res.getTookMs());
		}
		return R.success(res);
	}
}
