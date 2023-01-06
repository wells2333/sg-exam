package com.github.tangyi.common.constant;

import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PageConstant {

	String CONDITION = "condition";

	String PAGE = "page";

	String PAGE_SIZE = "pageSize";

	String PAGE_DEFAULT = "1";

	String PAGE_SIZE_DEFAULT = "10";

	String IS_DELETED = "isDeleted";

	String SORT_FIELD = "sortField";

	String SORT_ORDER = "sortOrder";

	String COLUMNS_FOR_ORDER_BY = EnvUtils.getValue("COLUMNS_FOR_ORDER_BY",
			"id,createTime,updateTime,sortNo,lastLoginTime");
	Set<String> VALID_COLUMNS_FOR_ORDER_BY = Stream.of(COLUMNS_FOR_ORDER_BY.split(","))
			.collect(Collectors.toCollection(HashSet::new));

	Set<String> VALID_SORT_ORDER = Sets.newHashSet("asc", "desc");
}
