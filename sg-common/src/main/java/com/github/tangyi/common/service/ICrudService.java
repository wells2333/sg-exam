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
