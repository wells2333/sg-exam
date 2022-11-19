package com.github.tangyi.exam.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.github.tangyi.common.excel.annotation.ExcelModel;
import com.github.tangyi.exam.excel.converter.SubjectLevelConverter;
import com.github.tangyi.exam.excel.converter.SubjectTypeConverter;
import lombok.Data;

/**
 * 题目信息
 * @author tangyi
 * @date 2019/12/10 18:03
 */
@Data
@ExcelModel("题目信息")
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SubjectExcelModel {

	@ExcelProperty(value = "题目ID", converter = LongStringConverter.class)
	private Long id;

	@ExcelProperty(value = "考试ID", converter = LongStringConverter.class)
	private Long examinationId;

	@ExcelProperty(value = "分类ID", converter = LongStringConverter.class)
	private Long categoryId;

	@ExcelProperty("题目名称")
	private String subjectName;

	@ExcelProperty(value = "题目类型", converter = SubjectTypeConverter.class)
	private Integer type;

	@ExcelProperty(value = "选择题类型", converter = SubjectTypeConverter.class)
	private Integer choicesType;

	@ExcelProperty("分值")
	@NumberFormat("#.##")
	private Double score;

	@ExcelProperty("答案")
	private String answer;

	@ExcelProperty("解析")
	private String analysis;

	@ExcelProperty(value = "难度等级", converter = SubjectLevelConverter.class)
	private Integer level;

	@ExcelProperty("选项")
	private String options;
}
