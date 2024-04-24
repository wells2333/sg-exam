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

package com.github.tangyi.common.base.id;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class IdFetcher implements IIdFetcher {

	protected static final String ID = "id";

	@Override
	public List<Long> fetchAll(long tableMinId, Map<String, Object> params) {
		log.info("Start to fetch ids, minId: {}", tableMinId);
		long startNs = System.nanoTime();
		List<Long> ids = Lists.newArrayList();
		long minId = tableMinId;
		int times = 0;
		while (true) {
			times++;
			List<Long> list = this.fetchPage(minId, params);
			if (CollectionUtils.isEmpty(list)) {
				log.info("Fetch the last page.");
				break;
			}
			ids.addAll(list);
			minId = list.get(list.size() - 1) + 1;
		}
		long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
		log.info("Fetch ids finished, size: {}, times: {}, took: {}ms", ids.size(), times, took);
		return ids;
	}
}
