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
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.github.tangyi.common.excel.annotation.ExcelModel;
import lombok.Data;

@Data
@ExcelModel("题目信息")
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SubjectExcelModel {

	@ExcelProperty("题目名称")
	@ColumnWidth(50)
	private String subjectName;

	@ExcelProperty(value = "题目类型", converter = Converters.TypeConverter.class)
	private Integer type;

	@ExcelProperty(value = "选择题类型", converter = Converters.TypeConverter.class)
	private Integer choicesType;

	@ExcelProperty("分值")
	@NumberFormat("#.##")
	private Double score;

	@ExcelProperty(value = "难度等级", converter = Converters.LevelConverter.class)
	private Integer level;

	@ExcelProperty("选项 A")
	private String optionA;

	@ExcelProperty("选项 B")
	private String optionB;

	@ExcelProperty("选项 C")
	private String optionC;

	@ExcelProperty("选项 D")
	private String optionD;

	@ExcelProperty("选项 E")
	private String optionE;

	@ExcelProperty("选项 F")
	private String optionF;

	@ExcelProperty("选项 G")
	private String optionG;

	@ExcelProperty("选项 H")
	private String optionH;

	@ExcelProperty("选项 I")
	private String optionI;

	@ExcelProperty("答案")
	private String answer;

	@ExcelProperty("解析")
	private String analysis;
}
