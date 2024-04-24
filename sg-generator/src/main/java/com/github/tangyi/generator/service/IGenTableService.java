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

import com.github.tangyi.generator.model.GenTable;

import java.util.List;
import java.util.Map;

public interface IGenTableService {

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
	 * 查询业务信息
	 *
	 * @param id 业务 ID
	 * @return 业务信息
	 */
	GenTable selectGenTableById(Long id);

	/**
	 * 修改业务
	 *
	 * @param genTable 业务信息
	 */
	void updateGenTable(GenTable genTable, String options);

	void deleteGenTableById(Long tableId);

	/**
	 * 导入表结构
	 *
	 * @param tableName 导入表列表
	 * @param comment 注释
	 */
	void importGenTable(String tableName, String comment, String packageName, String tenantCode);

	/**
	 * 预览代码
	 *
	 * @param tableId 表编号
	 * @return 预览数据列表
	 */
	Map<String, String> previewCode(Long tableId);

	/**
	 * 生成代码（下载方式）
	 *
	 * @param tableName 表名称
	 * @return 数据
	 */
	byte[] downloadCode(String tableName);

	/**
	 * 生成代码（自定义路径）
	 *
	 * @param tableName 表名称
	 */
	void generatorCode(String tableName);

	/**
	 * 同步数据库
	 *
	 * @param tableName 表名称
	 */
	void synchDb(String tableName);

	/**
	 * 批量生成代码（下载方式）
	 *
	 * @param tableNames 表数组
	 * @return 数据
	 */
	byte[] downloadCode(String[] tableNames);

	/**
	 * 修改保存参数校验
	 *
	 * @param genTable 业务信息
	 */
	void validateEdit(GenTable genTable, String options);
}
