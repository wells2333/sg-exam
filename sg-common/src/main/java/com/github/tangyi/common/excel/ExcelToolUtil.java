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

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.tangyi.common.excel.annotation.ExcelModel;
import com.github.tangyi.common.excel.exception.ExcelException;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.Servlets;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExcelToolUtil {

	private static final String DEFAULT_SHEET_NAME = "sheet1";

	private ExcelToolUtil() {
	}

	public static <T> void writeExcel(HttpServletRequest req, HttpServletResponse res, List<T> dataList,
			Class<T> clazz) {
		String fileName = DateUtils.localDateMillisToString(LocalDateTime.now());
		writeExcel(req, res, dataList, clazz, fileName);
	}

	public static <T> void writeExcel(HttpServletRequest req, HttpServletResponse res, List<T> dataList, Class<T> clazz,
			String fileName) {
		ExcelModel excelModel = clazz.getDeclaredAnnotation(ExcelModel.class);
		String sheetName = DEFAULT_SHEET_NAME;
		if (excelModel != null) {
			fileName = excelModel.value() + fileName;
			sheetName = excelModel.sheets()[0];
		}
		writeExcel(req, res, dataList, fileName, sheetName, clazz);
	}

	public static <T> void writeExcel(HttpServletRequest req, HttpServletResponse res, List<T> dataList,
			String fileName, String sheetName, Class<T> clazz) {
		ExcelWriter writer = null;
		try {
			writer = EasyExcelFactory.write(getOutputStream(fileName, req, res, ExcelTypeEnum.XLSX), clazz).build();
			writer.write(dataList, EasyExcelFactory.writerSheet(sheetName).build());
		} finally {
			if (writer != null) {
				writer.finish();
			}
		}
	}

	private static OutputStream getOutputStream(String fileName, HttpServletRequest req, HttpServletResponse res,
			ExcelTypeEnum type) {
		try {
			res.addHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(req, fileName + type.getValue()));
			return res.getOutputStream();
		} catch (IOException e) {
			throw new ExcelException("get OutputStream error!", e);
		}
	}

	public static <T> Boolean readExcel(InputStream inputStream, Class<T> clazz, AnalysisEventListener<T> listener) {
		Boolean success = Boolean.TRUE;
		ExcelReader reader = null;
		try {
			reader = EasyExcelFactory.read(inputStream, clazz, listener).build();
			reader.read(EasyExcelFactory.readSheet(0).build());
		} catch (Exception e) {
			log.error("failed to read excel", e);
			success = Boolean.FALSE;
		} finally {
			if (reader != null) {
				reader.finish();
			}
		}
		return success;
	}
}
