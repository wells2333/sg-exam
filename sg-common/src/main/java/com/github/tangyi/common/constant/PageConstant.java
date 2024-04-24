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

package com.github.tangyi.common.constant;

import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PageConstant {

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
