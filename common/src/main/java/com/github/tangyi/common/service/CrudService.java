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

public abstract class CrudService<D extends CrudMapper<T>, T extends BaseEntity<T>> extends BaseService {

	@Autowired
	protected D dao;

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return T
	 */
	public T get(Long id) {
		return dao.selectByPrimaryKey(id);
	}

	/**
	 * 查询列表
	 *
	 * @param entity entity
	 * @return List
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * 查询列表
	 *
	 * @param entity entity
	 * @return List
	 */
	public List<T> findAllList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * 查询列表
	 *
	 * @param ids ids
	 * @return List
	 */
	public List<T> findListById(Long[] ids) {
		if (ids == null || ids.length == 0)
			return new ArrayList<>();
		return dao.findListById(ids);
	}

	public void commonPageParam(Map<String, Object> params, int pageNum, int pageSize) {
		tenantParams(params);
		PageUtil.pageInfo(params);
		PageMethod.startPage(pageNum, pageSize);
	}

	public void tenantParams(Map<String, Object> params) {
		params.put(CommonConstant.TENANT_CODE, SysUtil.getTenantCode());
	}

	/**
	 * 查询分页
	 *
	 * @param params   params
	 * @param pageNum pageNum
	 * @return PageInfo
	 */
	public PageInfo<T> findPage(Map<String, Object> params, int pageNum, int pageSize) {
		commonPageParam(params, pageNum, pageSize);
		return new PageInfo<>(dao.findPage(params));
	}

	/**
	 * 插入或更新
	 *
	 * @param entity entity
	 * @return int
	 */
	@Transactional
	public int save(T entity) {
		if (entity.isNewRecord()) {
			return dao.insert(entity);
		} else {
			return dao.updateByPrimaryKey(entity);
		}
	}

	/**
	 * 删除
	 *
	 * @param entity entity
	 * @return int
	 */
	@Transactional
	public int delete(T entity) {
		return dao.delete(entity);
	}

	/**
	 * 批量删除
	 *
	 * @param ids ids
	 * @return int
	 */
	@Transactional
	public int deleteAll(Long[] ids) {
		return this.dao.deleteAll(ids);
	}

	/**
	 * 插入数据
	 *
	 * @param entity entity
	 * @return int
	 */
	@Transactional
	public int insert(T entity) {
		return dao.insertSelective(entity);
	}

	/**
	 * 更新数据
	 *
	 * @param entity entity
	 * @return int
	 */
	@Transactional
	public int update(T entity) {
		return dao.updateByPrimaryKeySelective(entity);
	}
}

