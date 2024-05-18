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

package com.github.tangyi.user.controller;

import com.github.tangyi.api.exam.dto.SearchResDto;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.lucene.DocType;
import com.github.tangyi.common.lucene.IndexDoc;
import com.github.tangyi.common.lucene.LuceneIndexManager;
import com.github.tangyi.common.model.R;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "搜索")
@RequestMapping("/v1/search")
public class SearchController extends BaseController {

	private final ICourseService courseService;
	private final IExaminationService examinationService;
	private final AttachmentManager attachmentManager;
	private final Map<String, AtomicInteger> rankWordMap = Maps.newConcurrentMap();

	@GetMapping("search")
	@Operation(summary = "根据关键词搜索，只返回 ID")
	public R<SearchResDto> search(@RequestParam String q, @RequestParam(required = false) String itemType,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(value = SORT_FIELD, required = false) String sortField,
			@RequestParam(value = SORT_ORDER, required = false) String sortOrder) {
		q = this.rewriteIfNecessary(q);
		SearchResDto res = null;
		try {
			res = this.doSearch(q, itemType, pageSize, sortField, sortOrder);
		} catch (Exception e) {
			log.error("Failed to search, itemType: {}, query: {}", itemType, q, e);
		} finally {
			int resSize = 0;
			long tookMs = 0;
			if (res != null) {
				resSize = CollectionUtils.size(res.getItems());
				tookMs = res.getTookMs();
			}
			log.info("[search-access] itemType: {}, query: {}, res size: {}, tookMs: {}", itemType, q, resSize, tookMs);
		}
		return R.success(res);
	}

	@GetMapping("searchDetail")
	@Operation(summary = "根据关键词搜索详细信息")
	public R<SearchResDto.SearchDetailResItemList> searchDetail(@RequestParam String q,
			@RequestParam(required = false) String itemType,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(value = SORT_FIELD, required = false) String sortField,
			@RequestParam(value = SORT_ORDER, required = false) String sortOrder) {
		q = this.rewriteIfNecessary(q);
		SearchResDto.SearchDetailResItemList res = null;
		try {
			SearchResDto dto = this.doSearch(q, itemType, pageSize, sortField, sortOrder);
			res = this.parseItems(dto);
		} catch (Exception e) {
			log.error("Failed to search, itemType: {}, query: {}", itemType, q, e);
		} finally {
			int resSize = 0;
			long tookMs = 0;
			if (res != null) {
				resSize = CollectionUtils.size(res.getItems());
				tookMs = res.getTookMs();
			}
			log.info("[searchDetail-access] itemType: {}, query: {}, res size: {}, tookMs: {}", itemType, q, resSize,
					tookMs);
		}
		return R.success(res);
	}

	@GetMapping("searchRank")
	@Operation(summary = "查询热门搜索词")
	public R<SearchResDto> searchRank() {
		SearchResDto dto = new SearchResDto();
		int limit = 10;
		rankWordMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.comparingInt(AtomicInteger::get).reversed())).limit(limit)
				.forEach(e -> dto.getItems().add(SearchResDto.SearchResItem.builder().title(e.getKey()).build()));
		return R.success(dto);
	}

	private String rewriteIfNecessary(String q) {
		String newQ = q;
		if (StringUtils.isEmpty(q)) {
			if (MapUtils.isNotEmpty(this.rankWordMap)) {
				newQ = CollectionUtils.get(this.rankWordMap, 0).getKey();
			}
		}
		if (StringUtils.isEmpty(newQ)) {
			newQ = "热门";
		}
		return newQ;
	}

	private SearchResDto doSearch(String q, String itemType, int pageSize, String sortField, String sortOrder)
			throws IOException, ParseException {
		long startNs = System.nanoTime();
		SearchResDto dto = new SearchResDto();
		DocType docType = DocType.matchByType(itemType);
		List<IndexDoc> indexDocs = LuceneIndexManager.getInstance().search(docType, q, pageSize, sortField, sortOrder);
		if (CollectionUtils.isNotEmpty(indexDocs)) {
			for (IndexDoc indexDoc : indexDocs) {
				dto.getItems().add(SearchResDto.SearchResItem.builder().id(indexDoc.getId()).type(indexDoc.getType())
						.build());
			}
		}
		dto.setTookMs(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs));
		rankWordMap.computeIfAbsent(q, s -> new AtomicInteger()).incrementAndGet();
		return dto;
	}

	private SearchResDto.SearchDetailResItemList parseItems(SearchResDto itemList) {
		List<SearchResDto.SearchResItem> items = itemList.getItems();
		if (CollectionUtils.isEmpty(items)) {
			return null;
		}
		SearchResDto.SearchDetailResItemList resItemList = new SearchResDto.SearchDetailResItemList();
		resItemList.setTotal(itemList.getTotal());
		resItemList.setTookMs(itemList.getTookMs());
		Map<String, List<SearchResDto.SearchResItem>> map = items.stream()
				.collect(Collectors.groupingBy(SearchResDto.SearchResItem::getType));
		for (Map.Entry<String, List<SearchResDto.SearchResItem>> entry : map.entrySet()) {
			List<SearchResDto.SearchResItem> resItems = entry.getValue();
			if (entry.getKey().equals(DocType.COURSE.getType())) {
				this.parseCourseDetail(resItems, resItemList);
			} else if (entry.getKey().equals(DocType.EXAM.getType())) {
				this.parseExamDetail(resItems, resItemList);
			} else if (entry.getKey().equals(DocType.OTHER.getType())) {
				this.parseOtherDetail(resItems, resItemList);
			}
		}
		return resItemList;
	}

	private void parseCourseDetail(List<SearchResDto.SearchResItem> items,
			SearchResDto.SearchDetailResItemList detailResItemList) {
		for (SearchResDto.SearchResItem item : items) {
			Course course = this.courseService.get(Long.valueOf(item.getId()));
			if (course != null) {
				SearchResDto.SearchResItem resItem = SearchResDto.SearchResItem.builder()//
						.id(item.getId())//
						.type(item.getType())//
						.title(course.getCourseName())//
						.desc(course.getSimpleDesc())//
						.build();
				this.initImageUrl(course.getImageId(), resItem);
				detailResItemList.addItem(resItem);
			}
		}
	}

	private void parseExamDetail(List<SearchResDto.SearchResItem> items,
			SearchResDto.SearchDetailResItemList detailResItemList) {
		for (SearchResDto.SearchResItem item : items) {
			Examination examination = this.examinationService.get(Long.valueOf(item.getId()));
			if (examination != null) {
				SearchResDto.SearchResItem resItem = SearchResDto.SearchResItem.builder()//
						.id(item.getId())//
						.type(item.getType())//
						.title(examination.getExaminationName())//
						.desc(examination.getRemark())//
						.build();
				this.initImageUrl(examination.getImageId(), resItem);
				detailResItemList.addItem(resItem);
			}
		}
	}

	private void parseOtherDetail(List<SearchResDto.SearchResItem> items,
			SearchResDto.SearchDetailResItemList detailResItemList) {
		for (SearchResDto.SearchResItem item : items) {
			SearchResDto.SearchResItem resItem = SearchResDto.SearchResItem.builder()//
					.id(item.getId())//
					.type(item.getType())//
					.build();
			detailResItemList.addItem(resItem);
		}
	}

	private void initImageUrl(Long imageId, SearchResDto.SearchResItem resItem) {
		if (imageId == null) {
			return;
		}
		resItem.setImageUrl(attachmentManager.getPreviewUrlIgnoreException(imageId));
	}
}
