package com.github.tangyi.common.utils;

import com.github.tangyi.common.base.TreeEntity;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

	private TreeUtil() {
	}

	@SuppressWarnings(value = "unchecked")
	public static <T> List<T> buildTree(List<? extends TreeEntity<T>> entities, Object root) {
		List<TreeEntity<T>> result = new ArrayList<>();
		entities.forEach(e -> {
			if (e.getParentId().equals(root)) {
				result.add(e);
			}
			entities.forEach(c -> {
				if (c.getParentId().equals(e.getId())) {
					if (e.getChildren() == null) {
						e.setChildren(new ArrayList<>());
					}
					e.add(c);
				}
			});
		});
		return (List<T>) result;
	}
}
