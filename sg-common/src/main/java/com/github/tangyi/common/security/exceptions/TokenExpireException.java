package com.github.tangyi.common.security.exceptions;

import com.github.tangyi.common.exceptions.CommonException;

public class TokenExpireException extends CommonException {

	public TokenExpireException(String msg) {
		super(msg);
	}
}
