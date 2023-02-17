package com.github.tangyi.user.constants;

import com.github.tangyi.common.utils.EnvUtils;

public interface AttachConstant {

	// URL默认的失效时间，一百年后
	long DEFAULT_EXPIRE = EnvUtils.getLong("QI_NIU_DEFAULT_EXPIRE", 33233472000L);

}
