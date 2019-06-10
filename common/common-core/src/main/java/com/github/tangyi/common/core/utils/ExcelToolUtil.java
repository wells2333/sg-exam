package com.github.tangyi.common.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author tangyi
 * @date 2018/11/26 22:20
 */
public class ExcelToolUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 根据数据和标题封装excel数据输出流
     *
     * @param input          输入流
     * @param out            响应输出流
     * @param list           待导出数据
     * @param keys2titlesMap 标题
     * @date 2018/11/26 22:20
     */
    public static void exportExcel(InputStream input, OutputStream out,
                                   List<Map<String, Object>> list, LinkedHashMap<String, String> keys2titlesMap) throws Exception {
        // 创建SXSSFWorkbook
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet("data");
        // 获取样式
        Map<String, CellStyle> styles = createStyles(wb);
        // 创建首行标题，设置高度
        Row rowHeader = sh.createRow(0);
        rowHeader.setHeightInPoints(16);
        Iterator<String> keys = keys2titlesMap.keySet().iterator();
        int i = 0;
        List<String> keyList = new ArrayList<String>();
        while (keys.hasNext()) {
            // 遍历标题，设置标题样式
            String key = keys.next();
            keyList.add(key);
            String title = keys2titlesMap.get(key);
            Cell cellHeader = rowHeader.createCell(i++);
            cellHeader.setCellStyle(styles.get("header"));
            cellHeader.setCellValue(title);
        }
        // 设置列宽
        for (int t = 0; t < keyList.size(); t++) {
            int colWidth = sh.getColumnWidth(t) * 2;
            sh.setColumnWidth(t, colWidth < 3000 ? 3000 : colWidth);
        }

        for (int rownum = 1; rownum <= list.size(); rownum++) {
            // 遍历数据，逐一插入excel
            Row row = sh.createRow(rownum);
            Map<String, Object> dataMap = list.get(rownum - 1);
            i = 0;

            for (String key : keyList) {
                // 根据标题，设置excel数据
                Cell cell = row.createCell(i++);
                if (dataMap.get(key) != null) {
                    // 根据值的类型，设置表格的数据格式
                    if (dataMap.get(key) instanceof String) {
                        cell.setCellValue(MapUtil.getString(dataMap, key));
                    } else if (dataMap.get(key) instanceof Date) {
                        Date date = MapUtil.getDate(dataMap, key);
                        cell.setCellValue(date == null ? "" : FORMATTER.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())));
                    } else if (dataMap.get(key) instanceof Number) {
                        cell.setCellValue(MapUtil.getDouble(dataMap, key));
                    } else if (dataMap.get(key) instanceof Boolean) {
                        Boolean value = MapUtil.getBooleanValue(dataMap, key);
                        cell.setCellValue(value == null ? Boolean.FALSE : value);
                    }
                } else if (key.contains(".")) {
                    // 标题的名称对应的可以是对象
                    String[] temp = StringUtils.split(key, ".");
                    Object obj = dataMap.get(temp[0]);
                    if (obj != null) {
                        //映射对象，取得对应的字段值
                        obj = ReflectionUtil.getFieldValue(obj, temp[1]);
                        if (obj != null) {
                            cell.setCellValue(obj.toString());
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }
            if (rownum % 100 == 0) {
                ((SXSSFSheet) sh).flushRows();
            }
        }
        wb.write(out);
        out.close();
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        // 设置标题样式
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        styles.put("title", style);
        // 设置数据行的默认样式
        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);
        // 设置数据行的默认样式1，位置左对齐
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_LEFT);
        styles.put("data1", style);
        // 设置数据行的默认样式2，位置居中
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("data2", style);
        // 设置数据行的默认样式3，位置右对齐
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("data3", style);
        // 设置表头的样式
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);
        return styles;
    }

    /**
     * 导入数据
     *
     * @param input          input
     * @param keys2titlesMap keys2titlesMap
     * @return List
     * @author tangyi
     * @date 2018/11/28 12:41
     */
    public static List<Map<String, Object>> importExcel(InputStream input,
                                                        LinkedHashMap<String, String> keys2titlesMap) throws Exception {
        String[] keys = keys2titlesMap.keySet().toArray(new String[]{});
        return importExcel(input, keys);
    }

    /**
     * 导入数据
     *
     * @param input input
     * @param keys  keys
     * @return List
     * @author tangyi
     * @date 2018/11/28 12:42
     */
    private static List<Map<String, Object>> importExcel(InputStream input, String[] keys) throws Exception {
        Workbook wb = WorkbookFactory.create(input);
        Sheet sheet = wb.getSheetAt(0);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
            // 从第二行开始解析数据
            Row row = sheet.getRow(rownum);
            if (row == null) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
                // 从第1列开始将单元格中的值写入map
                Cell cell = row.getCell(cellnum);
                if (cell == null)
                    continue;
                int valType = cell.getCellType();
                if (valType == Cell.CELL_TYPE_STRING) {
                    map.put(keys[cellnum], cell.getStringCellValue());
                } else if (valType == Cell.CELL_TYPE_BOOLEAN) {
                    map.put(keys[cellnum], cell.getBooleanCellValue());
                } else if (valType == Cell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 用于转化为日期格式
                        Date d = cell.getDateCellValue();
                        map.put(keys[cellnum], FORMATTER_DATE.format(LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault())));
                    } else {
                        map.put(keys[cellnum], numToStringFormat(String.valueOf(cell.getNumericCellValue())));
                    }
                }
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 判断这个数字是否是科学计数法 如果是 则转换成普通模式
     *
     * @param num num
     * @return String
     */
    private static String numToStringFormat(String num) {
        if (num.contains("E10")) {
            return new BigDecimal(num).toPlainString();
        } else if (num.contains(".0")) {
            return num.substring(0, num.length() - 2);
        } else
            return num;
    }
}
