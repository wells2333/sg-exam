package com.github.tangyi.common.exceptions;

import java.io.Serial;

public class ServiceException extends CommonException {

	@Serial
	private static final long serialVersionUID = -7285211528095468156L;

	public ServiceException() {
	}

	public ServiceException(String msg) {
		super(msg);
	}
}
