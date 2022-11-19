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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目分类service
 *
 * @author tangyi
 * @date 2018/12/4 21:56
 */
@Service
public class SubjectCategoryService extends CrudService<SubjectCategoryMapper, SubjectCategory> {

	/**
	 * 查找题目分类
	 *
	 * @param id id
	 * @return SubjectCategory
	 * @author tangyi
	 * @date 2019/1/3 14:21
	 */
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

	/**
	 * 更新题目分类
	 *
	 * @param subjectCategory subjectCategory
	 * @return int
	 * @author tangyi
	 * @date 2019/1/3 14:21
	 */
	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.SUBJECT_CATE, ExamCacheName.SUBJECT_CATE_TREE}, allEntries = true)
	public int update(SubjectCategory subjectCategory) {
		return super.update(subjectCategory);
	}

	/**
	 * 删除题目分类
	 *
	 * @param subjectCategory subjectCategory
	 * @return int
	 * @author tangyi
	 * @date 2019/1/3 14:21
	 */
	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.SUBJECT_CATE, ExamCacheName.SUBJECT_CATE_TREE}, allEntries = true)
	public int delete(SubjectCategory subjectCategory) {
		return super.delete(subjectCategory);
	}

	/**
	 * 批量删除题目分类
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019/1/3 14:23
	 */
	@Override
	@Transactional
	@CacheEvict(value = {ExamCacheName.SUBJECT_CATE, ExamCacheName.SUBJECT_CATE_TREE}, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	/**
	 * 返回树形分类集合
	 *
	 * @return List
	 * @author tangyi
	 * @date 2018/12/04 22:03
	 */
	@Cacheable(value = ExamCacheName.SUBJECT_CATE_TREE, key = "'tree'")
	public List<SubjectCategoryDto> categoryTree() {
		SubjectCategory subjectCategory = new SubjectCategory();
		subjectCategory.setTenantCode(SysUtil.getTenantCode());
		List<SubjectCategory> subjectCategoryList = findList(subjectCategory);
		if (CollectionUtils.isNotEmpty(subjectCategoryList)) {
			List<SubjectCategoryDto> subjectCategorySetTreeList = subjectCategoryList.stream()
					.map(SubjectCategoryDto::new).distinct().collect(Collectors.toList());
			return TreeUtil.buildTree(
					CollUtil.sort(subjectCategorySetTreeList, Comparator.comparingInt(SubjectCategoryDto::getSort)),
					CommonConstant.ROOT);
		}
		return new ArrayList<>();
	}
}
