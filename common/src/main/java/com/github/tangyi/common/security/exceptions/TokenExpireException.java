package com.github.tangyi.common.security.exceptions;

import com.github.tangyi.common.exceptions.CommonException;

/**
 * token 失效
 * @author tangyi
 * @date 2022/5/2 10:42 上午
 */
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
