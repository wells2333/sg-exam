package com.github.tangyi.api.user.service;

import com.github.tangyi.common.model.R;

public interface IMobileService {

	R<Boolean> sendVerificationCode(String mobile);
}
