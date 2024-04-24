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

package com.github.tangyi.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 封装简单数据导入的逻辑，解析 3000 条刷一次数据库
 */
public abstract class AbstractExcelImportListener<T> extends AnalysisEventListener<T> {

	private static final int BATCH_COUNT = EnvUtils.getInt("EXCEL_IMPORT_BATCH_SIZE", 1000);
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final List<T> dataList = Lists.newArrayListWithExpectedSize(BATCH_COUNT);

	@Override
	public void invoke(T model, AnalysisContext context) {
		this.dataList.add(model);
		if (this.dataList.size() >= BATCH_COUNT) {
			this.saveData(Lists.newArrayList(dataList));
			this.dataList.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		if (CollectionUtils.isNotEmpty(this.dataList)) {
			this.saveData(Lists.newArrayList(this.dataList));
		}
		logger.info("All data is parsed!");
	}

	public abstract void saveData(List<T> dataList);
}
