package com.github.tangyi.common.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.persistence.BaseEntity;
import com.github.tangyi.common.persistence.CrudMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangyi
 * @date 2018-08-25 17:22
 */
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
        return dao.getById(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity entity
     * @return T
     */
    public T get(T entity) {
        return dao.get(entity);
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

    /**
     * 查询分页
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<T> findPage(PageInfo<T> page, T entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(dao.findList(entity));
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
            return dao.update(entity);
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
        return dao.insert(entity);
    }

    /**
     * 更新数据
     *
     * @param entity entity
     * @return int
     */
    @Transactional
    public int update(T entity) {
        return dao.update(entity);
    }
}

