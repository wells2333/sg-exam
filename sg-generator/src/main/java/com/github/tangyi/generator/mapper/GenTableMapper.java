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

package com.github.tangyi.generator.mapper;

import com.github.tangyi.common.base.CrudMapper;
import com.github.tangyi.generator.model.GenTable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenTableMapper extends CrudMapper<GenTable> {

	/**
	 * 查询据库列表
	 *
	 * @param genTable 业务信息
	 * @return 数据库表集合
	 */
	List<GenTable> selectDbTableList(GenTable genTable);

	/**
	 * 查询据库列表
	 *
	 * @param tableNames 表名称组
	 * @return 数据库表集合
	 */
	List<GenTable> selectDbTableListByNames(String[] tableNames);

	/**
	 * 查询所有表信息
	 *
	 * @return 表信息集合
	 */
	List<GenTable> selectGenTableAll();

	/**
	 * 查询表 ID 业务信息
	 *
	 * @param id 业务 ID
	 * @return 业务信息
	 */
	GenTable selectGenTableById(Long id);

	/**
	 * 查询表名称业务信息
	 *
	 * @param tableName 表名称
	 * @return 业务信息
	 */
	GenTable selectGenTableByName(String tableName);

	/**
	 * 新增业务
	 *
	 * @param genTable 业务信息
	 * @return 结果
	 */
	int insertGenTable(GenTable genTable);

	/**
	 * 修改业务
	 *
	 * @param genTable 业务信息
	 * @return 结果
	 */
	int updateGenTable(GenTable genTable);

	/**
	 * 批量删除业务
	 *
	 * @param ids 需要删除的数据 ID
	 * @return 结果
	 */
	int deleteGenTableByIds(Long[] ids);
}
