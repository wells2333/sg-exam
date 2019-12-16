package com.github.tangyi.exam.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.github.tangyi.exam.enums.SubmitStatusEnum;

/**
 * 提交状态
 * @author tangyi
 * @date 2019/12/10 17:49
 */
public class SubmitStatusConverter implements Converter<Integer> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return Integer.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		return SubmitStatusEnum.matchByName(cellData.getStringValue()).getValue();
	}

	@Override
	public CellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		return new CellData<>(SubmitStatusEnum.matchByValue(value).getName());
	}
}

