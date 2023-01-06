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
		SubjectCategory subjectCategory = new SubjectCategory();
		subjectCategory.setTenantCode(SysUtil.getTenantCode());
		Object categoryName = condition.get("categoryName");
		if (categoryName != null) {
			subjectCategory.setCategoryName(categoryName.toString());
		}
		List<SubjectCategory> categories = findList(subjectCategory);
		if (CollectionUtils.isNotEmpty(categories)) {
			List<SubjectCategoryDto> list = categories.stream().map(SubjectCategoryDto::new).distinct()
					.collect(Collectors.toList());
			return TreeUtil.buildTree(CollUtil.sort(list, Comparator.comparingInt(SubjectCategoryDto::getSort)),
					CommonConstant.ROOT);
		}
		return Lists.newArrayList();
	}
}
