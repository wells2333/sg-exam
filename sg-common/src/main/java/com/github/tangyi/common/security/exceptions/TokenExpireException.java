package com.github.tangyi.common.security.exceptions;

import com.github.tangyi.common.exceptions.CommonException;

public class TokenExpireException extends CommonException {

	public TokenExpireException() {

	}

	public TokenExpireException(String msg) {
		super(msg);
	}

	public TokenExpireException(Throwable throwable) {
		super(throwable);
	}

	public TokenExpireException(Throwable throwable, String msg) {
		super(throwable);
	}
}
