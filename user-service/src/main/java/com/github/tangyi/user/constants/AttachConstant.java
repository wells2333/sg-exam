package com.github.tangyi.user.constants;

import com.github.tangyi.common.utils.EnvUtils;

public interface AttachConstant {

	// URL默认的失效时间
	int DEFAULT_EXPIRE = EnvUtils.getInt("QI_NIU_DEFAULT_EXPIRE", Integer.MAX_VALUE);

}
