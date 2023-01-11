package com.github.tangyi.common.service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.base.CrudMapper;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CrudService<D extends CrudMapper<T>, T extends BaseEntity<T>> extends BaseService
		implements ICrudService<T> {

	@Autowired
	protected D dao;

	@Override
	public T get(Long id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	@Override
	public List<T> findAllList(T entity) {
		return dao.findList(entity);
	}

	@Override
	public List<T> findListById(Long[] ids) {
		if (ids == null || ids.length == 0)
			return new ArrayList<>();
		return dao.findListById(ids);
	}

	protected void commonPageParam(Map<String, Object> params, int pageNum, int pageSize) {
		tenantParams(params);
		PageUtil.pageInfo(params);
		PageMethod.startPage(pageNum, pageSize);
	}

	protected void tenantParams(Map<String, Object> params) {
		params.put(CommonConstant.TENANT_CODE, SysUtil.getTenantCode());
	}

	@Override
	public PageInfo<T> findPage(Map<String, Object> params, int pageNum, int pageSize) {
		commonPageParam(params, pageNum, pageSize);
		return new PageInfo<>(dao.findPage(params));
	}

	@Override
	@Transactional
	public int save(T entity) {
		if (entity.isNewRecord()) {
			return dao.insert(entity);
		} else {
			return dao.updateByPrimaryKey(entity);
		}
	}

	@Override
	@Transactional
	public int insert(T entity) {
		return dao.insertSelective(entity);
	}

	@Override
	@Transactional
	public int update(T entity) {
		return dao.updateByPrimaryKeySelective(entity);
	}

	@Override
	@Transactional
	public int delete(T entity) {
		return dao.delete(entity);
	}

	@Override
	@Transactional
	public int deleteAll(Long[] ids) {
		return this.dao.deleteAll(ids);
	}
}

