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

package com.github.tangyi.exam.service.subject;

import cn.hutool.core.collection.CollUtil;
import com.github.tangyi.api.exam.dto.SubjectCategoryDto;
import com.github.tangyi.api.exam.model.SubjectCategory;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TreeUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectCategoryMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubjectCategoryService extends CrudService<SubjectCategoryMapper, SubjectCategory> {

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_CATE, key = "#id")
	public SubjectCategory get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.SUBJECT_CATE, ExamCacheName.SUBJECT_CATE_TREE}, allEntries = true)
	public int insert(SubjectCategory entity) {
		return super.insert(entity);
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.SUBJECT_CATE, ExamCacheName.SUBJECT_CATE_TREE}, allEntries = true)
	public int update(SubjectCategory subjectCategory) {
		return super.update(subjectCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.SUBJECT_CATE, ExamCacheName.SUBJECT_CATE_TREE}, allEntries = true)
	public int delete(SubjectCategory subjectCategory) {
		return super.delete(subjectCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.SUBJECT_CATE, ExamCacheName.SUBJECT_CATE_TREE}, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Cacheable(value = ExamCacheName.SUBJECT_CATE_TREE, key = "'tree'", condition = "#condition == null || #condition.isEmpty()")
	public List<SubjectCategoryDto> categoryTree(Map<String, Object> condition) {
		SubjectCategory category = new SubjectCategory();
		category.setTenantCode(SysUtil.getTenantCode());
		Object categoryName = condition.get("categoryName");
		if (categoryName != null) {
			category.setCategoryName(categoryName.toString());
		}
		Object status = condition.get("status");
		if (status != null) {
			category.setStatus(Integer.valueOf(status.toString()));
		}
		return findAndBuild(category, CommonConstant.ROOT);
	}

	public List<SubjectCategoryDto> getCategoryByParentId(Long parentId) {
		SubjectCategory category = new SubjectCategory();
		category.setTenantCode(SysUtil.getTenantCode());
		category.setParentId(parentId);
		return findAndBuild(category, parentId);
	}

	private List<SubjectCategoryDto> findAndBuild(SubjectCategory subjectCategory, Long rootId) {
		List<SubjectCategory> categories = findList(subjectCategory);
		if (CollectionUtils.isNotEmpty(categories)) {
			return buildTree(categories, rootId);
		}
		return Lists.newArrayList();
	}

	private List<SubjectCategoryDto> buildTree(List<SubjectCategory> categories, Long rootId) {
		List<SubjectCategoryDto> list = categories.stream().map(SubjectCategoryDto::new).distinct()
				.collect(Collectors.toList());
		return TreeUtil.buildTree(CollUtil.sort(list, Comparator.comparingInt(SubjectCategoryDto::getSort)), rootId);
	}
}
