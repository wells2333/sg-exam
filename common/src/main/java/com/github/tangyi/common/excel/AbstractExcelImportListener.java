package com.github.tangyi.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装简单数据导入的逻辑，解析3000条刷一次数据库
 * @author tangyi
 * @date 2019/12/10 16:48
 */
public abstract class AbstractExcelImportListener<T> extends AnalysisEventListener<T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 每隔3000条存储数据库
	 */
	private static final int BATCH_COUNT = 3000;

	/**
	 * 需要导入的数据
	 */
	private List<T> dataList = new ArrayList<>();

	@Override
	public void invoke(T dataModel, AnalysisContext context) {
		dataList.add(dataModel);
		// 达到BATCH_COUNT则保存进数据库，防止数据几万条数据在内存，容易OOM
		if (dataList.size() >= BATCH_COUNT) {
			saveData(dataList);
			// 存储完成清理list
			dataList.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		// 最后一次保存
		saveData(dataList);
		logger.info("All data is parsed!");
	}

	/**
	 * 保存数据，子类实现
	 */
	public abstract void saveData(List<T> dataList);
}
