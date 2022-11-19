package com.github.tangyi.user.constants;

import com.github.tangyi.common.utils.EnvUtils;

/**
 *
 * @author tangyi
 * @date 2022/7/2 8:29 下午
 */
public interface AttachConstant {

	// URL默认的失效时间
	int DEFAULT_EXPIRE = EnvUtils.getInt("QI_NIU_DEFAULT_EXPIRE", Integer.MAX_VALUE);

}
