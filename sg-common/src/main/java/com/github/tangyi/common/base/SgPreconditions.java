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

package com.github.tangyi.common.base;

import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.exceptions.ServiceException;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.RUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class SgPreconditions {

	private SgPreconditions() {

	}

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
