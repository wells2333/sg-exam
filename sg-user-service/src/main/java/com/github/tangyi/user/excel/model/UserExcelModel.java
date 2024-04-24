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

package com.github.tangyi.user.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.github.tangyi.common.excel.annotation.ExcelModel;
import com.github.tangyi.common.excel.converter.GenderConverter;
import com.github.tangyi.common.excel.converter.StatusConverter;
import com.github.tangyi.user.excel.IdentityTypeConverter;
import lombok.Data;

import java.util.Date;

@Data
@ExcelModel("用户信息")
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(15)
public class UserExcelModel {

	@ExcelProperty(value = "用户 ID", converter = LongStringConverter.class)
	@ColumnWidth(20)
	private Long id;

	@ExcelProperty("账号")
	private String identifier;

	@ExcelProperty(value = "账号类型", converter = IdentityTypeConverter.class)
	private Integer identityType;

	@ExcelProperty("姓名")
	private String name;

	@ExcelProperty(value = "性别", converter = GenderConverter.class)
	private Integer gender;

	@ExcelProperty("联系电话")
	private String phone;

	@ExcelProperty("邮箱")
	@ColumnWidth(20)
	private String email;

	@ExcelProperty("生日")
	@DateTimeFormat("yyyy 年 MM 月 dd 日")
	private Date born;

	@ExcelProperty("备注")
	private String remark;

	@ExcelProperty(value = "状态", converter = StatusConverter.class)
	private Integer status;

	@ExcelProperty(value = "部门 ID", converter = LongStringConverter.class)
	@ColumnWidth(20)
	private Long deptId;

	@ExcelProperty("租户标识")
	private String tenantCode;
}
