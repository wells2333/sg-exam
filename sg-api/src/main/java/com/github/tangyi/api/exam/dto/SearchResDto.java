package com.github.tangyi.api.exam.dto;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchResDto extends ItemList {

	private List<SearchResItem> items = Lists.newArrayList();

	@Data
	@Builder
	public static final class SearchResItem {
		private String id;
		private String type;
		private String title;
		private String desc;
		private String imageUrl;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static final class SearchDetailResItemList extends ItemList {
		private List<SearchResItem> items = Lists.newArrayList();

		public void addItem(SearchResItem item) {
			this.items.add(item);
		}
	}
}

@Data
class ItemList {
	private int total;
	private long tookMs;
}
