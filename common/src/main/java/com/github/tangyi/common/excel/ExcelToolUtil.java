package com.github.tangyi.common.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
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

/**
 * excel导入导出工具类
 * @author tangyi
 * @date 2018/11/26 22:20
 */
@Slf4j
public class ExcelToolUtil {

	private static final String DEFAULT_SHEET_NAME = "sheet1";

	/**
	 * 私有构造方法，禁止实例化
	 */
	private ExcelToolUtil() {
	}

	/**
	 * 导出Excel
	 * @param request request
	 * @param response response
	 * @param dataList 数据list
	 * @param clazz clazz
	 */
	public static <T> void writeExcel(HttpServletRequest request, HttpServletResponse response, List<T> dataList,
			Class<T> clazz) {
		// 获取fileName和sheetName
		ExcelModel excelModel = clazz.getDeclaredAnnotation(ExcelModel.class);
		String fileName = DateUtils.localDateMillisToString(LocalDateTime.now());
		String sheetName = DEFAULT_SHEET_NAME;
		if (excelModel != null) {
			fileName = excelModel.value() + fileName;
			sheetName = excelModel.sheets()[0];
		}
		// 导出
		writeExcel(request, response, dataList, fileName, sheetName, clazz);
	}

	/**
	 * 导出Excel
	 * @param request request
	 * @param response response
	 * @param dataList 数据list
	 * @param fileName 文件名
	 * @param sheetName sheet 名
	 * @param clazz clazz
	 */
	public static <T> void writeExcel(HttpServletRequest request, HttpServletResponse response, List<T> dataList,
			String fileName, String sheetName, Class<T> clazz) {
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcelFactory
					.write(getOutputStream(fileName, request, response, ExcelTypeEnum.XLSX), clazz).build();
			WriteSheet writeSheet = EasyExcelFactory.writerSheet(sheetName).build();
			excelWriter.write(dataList, writeSheet);
		} finally {
			if (excelWriter != null)
				excelWriter.finish();
		}
	}

	/**
	 * 获取OutputStream
	 * @param fileName fileName
	 * @param request request
	 * @param response response
	 * @param excelTypeEnum excelTypeEnum
	 * @return OutputStream
	 */
	private static OutputStream getOutputStream(String fileName, HttpServletRequest request,
			HttpServletResponse response, ExcelTypeEnum excelTypeEnum) {
		try {
			// 设置响应头，处理浏览器间的中文乱码问题
			response.addHeader(HttpHeaders.CONTENT_DISPOSITION,
					Servlets.getDownName(request, fileName + excelTypeEnum.getValue()));
			return response.getOutputStream();
		} catch (IOException e) {
			throw new ExcelException("get OutputStream error!");
		}
	}

	/**
	 * 导入Excel
	 * @param inputStream inputStream
	 * @param clazz clazz
	 * @param listener listener
	 * @return boolean
	 */
	public static <T> Boolean readExcel(InputStream inputStream, Class<T> clazz, AnalysisEventListener<T> listener) {
		Boolean success = Boolean.TRUE;
		ExcelReader excelReader = null;
		try {
			excelReader = EasyExcelFactory.read(inputStream, clazz, listener).build();
			ReadSheet readSheet = EasyExcelFactory.readSheet(0).build();
			excelReader.read(readSheet);
		} catch (Exception e) {
			log.error("Read com.github.tangyi.exam.excel error: {}", e.getMessage(), e);
			success = Boolean.FALSE;
		} finally {
			if (excelReader != null)
				excelReader.finish();
		}
		return success;
	}
}
