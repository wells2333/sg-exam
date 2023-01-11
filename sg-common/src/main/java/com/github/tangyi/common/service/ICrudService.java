package com.github.tangyi.common.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.base.BaseEntity;

import java.util.List;
import java.util.Map;

public interface ICrudService<T extends BaseEntity<T>> {

	T get(Long id);

	List<T> findList(T entity);

	List<T> findAllList(T entity);

	List<T> findListById(Long[] ids);

	PageInfo<T> findPage(Map<String, Object> params, int pageNum, int pageSize);

	int save(T entity);

	int insert(T entity);

	int update(T entity);

	int delete(T entity);

	int deleteAll(Long[] ids);
}
