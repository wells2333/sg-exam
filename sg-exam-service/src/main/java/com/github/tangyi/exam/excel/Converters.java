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

package com.github.tangyi.exam.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.exam.enums.SubjectLevel;
import com.github.tangyi.exam.enums.SubjectType;

public final class Converters {

	private Converters() {

	}

	public static final class TypeConverter implements Converter<Integer> {

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
			return SubjectType.matchByName(cellData.getStringValue()).getValue();
		}

		@Override
		public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) {
			return new WriteCellData<>(SubjectType.matchByValue(value).getName());
		}
	}

	public static final class SubmitConverter implements Converter<Integer> {

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
			return SubmitStatusEnum.matchByName(cellData.getStringValue(), SubmitStatusEnum.UNKNOWN).getValue();
		}

		@Override
		public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) {
			return new WriteCellData<>(SubmitStatusEnum.match(value, SubmitStatusEnum.UNKNOWN).getName());
		}
	}

	/**
	 * 题目难度级别
	 */
	public static final class LevelConverter implements Converter<Integer> {

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
			return SubjectLevel.matchByName(cellData.getStringValue()).getValue();
		}

		@Override
		public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) {
			return new WriteCellData<>(SubjectLevel.matchByValue(value).getName());
		}
	}
}
