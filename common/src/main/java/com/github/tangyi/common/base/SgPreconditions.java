package com.github.tangyi.common.base;

import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.exceptions.ServiceException;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.RUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class SgPreconditions {

	public static void checkNull(Object obj, String msg) {
		if (obj == null) {
			throw new CommonException(msg);
		}
	}

	public static void checkBlank(String obj, String msg) {
		if (StringUtils.isBlank(obj)) {
			throw new CommonException(msg);
		}
	}

	public static void checkCollectionEmpty(Collection<?> obj, String msg) {
		if (CollectionUtils.isEmpty(obj)) {
			throw new CommonException(msg);
		}
	}

	public static void checkBoolean(boolean condition, String msg) {
		if (condition) {
			throw new CommonException(msg);
		}
	}
}
