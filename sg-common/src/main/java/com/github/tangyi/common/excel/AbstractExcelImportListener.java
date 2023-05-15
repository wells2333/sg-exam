package com.github.tangyi.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装简单数据导入的逻辑，解析 3000 条刷一次数据库
 */
public abstract class AbstractExcelImportListener<T> extends AnalysisEventListener<T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 每隔 3000 条存储数据库
	 */
	private static final int BATCH_COUNT = 3000;

	/**
	 * 需要导入的数据
	 */
	private final List<T> dataList = new ArrayList<>();

	@Override
	public void invoke(T dataModel, AnalysisContext context) {
		dataList.add(dataModel);
		// 达到 BATCH_COUNT 则保存进数据库，防止数据几万条数据在内存，容易OOM
		if (dataList.size() >= BATCH_COUNT) {
			saveData(dataList);
			// 存储完成清理
			dataList.clear();
		}
		if (CollectionUtils.isNotEmpty(dataList)) {
			saveData(dataList);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		saveData(dataList);
		logger.info("All data is parsed!");
	}

	public abstract void saveData(List<T> dataList);
}
