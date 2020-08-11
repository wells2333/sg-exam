package com.github.tangyi.exam.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.github.tangyi.common.excel.annotation.ExcelModel;
import com.github.tangyi.exam.excel.converter.SubmitStatusConverter;
import lombok.Data;

import java.util.Date;

/**
 * 考试记录
 * @author tangyi
 * @date 2019/12/10 17:41
 */
@Data
@ExcelModel("考试记录")
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(25)
public class ExamRecordExcelModel {

	@ExcelProperty(value = "考生ID", converter = LongStringConverter.class)
	private Long userId;

	@ExcelProperty(value = "考试ID", converter = LongStringConverter.class)
	private Long examinationId;

	@ExcelProperty("开始时间")
	@DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
	private Date startTime;

	@ExcelProperty("结束时间")
	@DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
	private Date endTime;

	@ExcelProperty("成绩")
	@NumberFormat("#.##")
	private Double score;

	@ExcelProperty("错题数")
	private Integer inCorrectNumber;

	@ExcelProperty("正确题数")
	private Integer correctNumber;

	@ExcelProperty(value = "提交状态", converter = SubmitStatusConverter.class)
	private Integer submitStatus;
}
