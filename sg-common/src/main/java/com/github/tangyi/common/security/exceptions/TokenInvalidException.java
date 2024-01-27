package com.github.tangyi.common.security.exceptions;

import com.github.tangyi.common.exceptions.CommonException;

public class TokenInvalidException extends CommonException {

	public TokenInvalidException(Throwable throwable, String msg) {
		super(throwable, msg);
	}
}
