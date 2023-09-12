package com.github.tangyi.exam.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.github.tangyi.exam.enums.SubmitStatus;

/**
 * 提交状态
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
	public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return SubmitStatus.matchByName(cellData.getStringValue()).getValue();
	}

	@Override
	public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return new WriteCellData<>(SubmitStatus.matchByValue(value).getName());
	}
}

