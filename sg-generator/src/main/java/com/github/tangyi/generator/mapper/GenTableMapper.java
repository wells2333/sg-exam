package com.github.tangyi.generator.mapper;

import com.github.tangyi.common.base.CrudMapper;
import com.github.tangyi.generator.model.GenTable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author tangyi
 * @date 2022/7/18 3:57 下午
 */
@Repository
public interface GenTableMapper extends CrudMapper<GenTable> {

	/**
	 * 查询业务列表
	 *
	 * @param genTable 业务信息
	 * @return 业务集合
	 */
	List<GenTable> selectGenTableList(GenTable genTable);

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
	 * 查询表ID业务信息
	 *
	 * @param id 业务ID
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
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	int deleteGenTableByIds(Long[] ids);
}
