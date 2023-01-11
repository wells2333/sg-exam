package com.github.tangyi.common.utils;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.constant.PageConstant;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class PageUtil implements PageConstant {

	public static void copyProperties(PageInfo<?> source, PageInfo<?> target) {
		target.setPageNum(source.getPageNum());
		target.setPageSize(source.getPageSize());
		target.setSize(source.getSize());
		target.setStartRow(source.getStartRow());
		target.setEndRow(source.getEndRow());
		target.setPages(source.getPages());
		target.setPrePage(source.getPrePage());
		target.setNextPage(source.getNextPage());
		target.setIsFirstPage(source.isIsFirstPage());
		target.setIsLastPage(source.isIsLastPage());
		target.setHasPreviousPage(source.isHasPreviousPage());
		target.setHasNextPage(source.isHasNextPage());
		target.setNavigatePages(source.getNavigatePages());
		target.setNavigatepageNums(source.getNavigatepageNums());
		target.setNavigateFirstPage(source.getNavigateFirstPage());
		target.setNavigateLastPage(source.getNavigateLastPage());
		target.setTotal(source.getTotal());
	}

	public static void pageInfo(Map<String, Object> params) {
		if (params.containsKey(SORT_FIELD)) {
			String sortField = String.valueOf(params.get(SORT_FIELD));
			if (isValidSortField(sortField)) {
				params.put(SORT_FIELD, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortField));
			}
		}
		if (params.containsKey(SORT_ORDER)) {
			String sortOrder = String.valueOf(params.get(SORT_ORDER));
			if (isValidSortOrder(sortOrder)) {
				params.put(SORT_ORDER, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortOrder));
			}
		}
		// (0：启用；1：禁用) 如果是非0非1，则移除isDeleted，从而查询出全部状态数据
		Object isDelete = params.get(IS_DELETED);
		if (isDelete != null && !StringUtils.isEmpty(isDelete.toString())) {
			String isDeleted = String.valueOf(params.get(IS_DELETED));
			if (isDeleted != null && !"0".equals(isDeleted) && !"1".equals(isDeleted)) {
				params.remove(IS_DELETED);
			}
		}
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value instanceof String) {
				params.put(key, ((String) value).trim());
			}
		}
		params.put("tenant_code", SysUtil.getTenantCode());
	}

	public static <T> PageInfo<T> newPageInfo(PageInfo<?> pageInfo, List<T> list) {
		PageInfo<T> newPageInfo = new PageInfo<>();
		copyProperties(pageInfo, newPageInfo);
		newPageInfo.setList(list);
		return newPageInfo;
	}

	public static boolean isValidSortField(String sortField) {
		return VALID_COLUMNS_FOR_ORDER_BY.contains(sortField);
	}

	public static boolean isValidSortOrder(String sortOrder) {
		return VALID_SORT_ORDER.contains(sortOrder);
	}
}
