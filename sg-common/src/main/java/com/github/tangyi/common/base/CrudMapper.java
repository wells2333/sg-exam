package com.github.tangyi.common.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CrudMapper<T> extends BaseCrudMapper<T> {

	List<T> findList(T entity);

	List<T> findPage(@Param("params") Map<String, Object> params);

	List<T> findListById(@Param("ids") Long[] ids);

	int delete(T entity);

	int deleteAll(@Param("ids") Long[] ids);
}

