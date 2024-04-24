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

package com.github.tangyi.generator.service;

import com.github.tangyi.generator.model.GenTableColumn;

import java.util.List;

public interface IGenTableColumnService {

	/**
	 * 查询业务字段列表
	 *
	 * @param tableId 业务字段编号
	 * @return 业务字段集合
	 */
	List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId);

	/**
	 * 新增业务字段
	 *
	 * @param genTableColumn 业务字段信息
	 * @return 结果
	 */
	int insertGenTableColumn(GenTableColumn genTableColumn);

	/**
	 * 修改业务字段
	 *
	 * @param genTableColumn 业务字段信息
	 * @return 结果
	 */
	int updateGenTableColumn(GenTableColumn genTableColumn);

	/**
	 * 删除业务字段信息
	 *
	 * @param ids 需要删除的数据 ID
	 * @return 结果
	 */
	int deleteGenTableColumnByIds(String ids);
}
