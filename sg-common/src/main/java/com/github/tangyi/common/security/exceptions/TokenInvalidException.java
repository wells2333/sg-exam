package com.github.tangyi.common.security.exceptions;

import com.github.tangyi.common.exceptions.CommonException;

public class TokenInvalidException extends CommonException {

	public TokenInvalidException() {

	}

	public TokenInvalidException(String msg) {
		super(msg);
	}

	public TokenInvalidException(Throwable throwable) {
		super(throwable);
	}

	public TokenInvalidException(Throwable throwable, String msg) {
		super(throwable);
	}
}
