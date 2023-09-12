package com.github.tangyi.common.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.github.tangyi.common.enums.GenderEnum;

public class GenderConverter implements Converter<Integer> {

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
		return GenderEnum.matchByName(cellData.getStringValue()).getValue();
	}

	@Override
	public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return new WriteCellData<>(GenderEnum.matchByValue(value).getName());
	}
}
