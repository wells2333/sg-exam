package com.github.tangyi.api.user.constant;

import com.github.tangyi.common.utils.EnvUtils;

public class AttachmentConstant {

    public static final Integer QI_NIU = 1;

    public static final Integer MINIO = 2;

    // URL 默认的失效时间，一百年
    public static final long DEFAULT_EXPIRE_SECOND = EnvUtils.getLong("ATTACH_URL_DEFAULT_EXPIRE_SECOND", 33233472000L);
}
