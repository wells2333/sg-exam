package com.github.tangyi.common.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CrudMapper<T> extends BaseCrudMapper<T> {

	/**
	 * 获取列表数据
	 *
	 * @param entity entity
	 * @return List
	 */
	List<T> findList(T entity);

	/**
	 * 获取列表数据
	 * @param params params
	 * @return List
	 */
	List<T> findPage(@Param("params") Map<String, Object> params);

	/**
	 * 根据id获取列表数据
	 *
	 * @param ids ids
	 * @return List
	 */
	List<T> findListById(@Param("ids") Long[] ids);

	/**
	 * 删除单条数据
	 *
	 * @param entity entity
	 * @return int
	 */
	int delete(T entity);

	/**
	 * 批量删除
	 *
	 * @param ids ids
	 * @return int
	 */
	int deleteAll(@Param("ids") Long[] ids);
}

