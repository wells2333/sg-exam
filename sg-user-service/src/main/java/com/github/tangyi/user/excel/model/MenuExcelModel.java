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
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.github.tangyi.common.excel.annotation.ExcelModel;
import lombok.Data;

@Data
@ExcelModel("菜单信息")
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(25)
public class MenuExcelModel {

	@ExcelProperty(value = "菜单 ID", converter = LongStringConverter.class)
	private Long id;

	@ExcelProperty("菜单名称")
	private String name;

	@ExcelProperty("权限标识")
	private String permission;

	@ExcelProperty("url")
	private String url;

	@ExcelProperty("重定向 url")
	private String redirect;

	@ExcelProperty(value = "父菜单 ID", converter = LongStringConverter.class)
	private Long parentId;

	@ExcelProperty("图标")
	private String icon;

	@ExcelProperty("排序号")
	private String sort;

	@ExcelProperty("类型")
	private String type;

	@ExcelProperty("模块")
	private String component;

	@ExcelProperty("路径")
	private String path;

	@ExcelProperty("备注")
	private String remark;

	@ExcelProperty("租户标识")
	private String tenantCode;

}
