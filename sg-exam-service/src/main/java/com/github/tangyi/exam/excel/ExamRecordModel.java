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

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.github.tangyi.common.excel.annotation.ExcelModel;
import lombok.Data;

import java.util.Date;

@Data
@ExcelModel("考试记录")
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(25)
public class ExamRecordModel {

	@ExcelProperty(value = "考生ID", converter = LongStringConverter.class)
	private Long userId;

	@ExcelProperty("考生姓名")
	private String userName;

	@ExcelProperty("部门名称")
	private String deptName;

	@ExcelProperty(value = "考试ID", converter = LongStringConverter.class)
	private Long examinationId;

	@ExcelProperty("开始时间")
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@ExcelProperty("结束时间")
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	@ExcelProperty("成绩")
	private Double score;

	@ExcelProperty("错题数")
	private Integer inCorrectNumber;

	@ExcelProperty("正确题数")
	private Integer correctNumber;

	@ExcelProperty(value = "提交状态", converter = Converters.SubmitConverter.class)
	private Integer submitStatus;
}
