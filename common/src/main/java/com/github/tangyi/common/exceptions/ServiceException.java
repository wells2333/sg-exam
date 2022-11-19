package com.github.tangyi.common.exceptions;

/**
 *
 * 服务异常
 *
 * @author tangyi
 * @date 2019-10-08 12:56
 */
public class ServiceException extends CommonException {

	private static final long serialVersionUID = -7285211528095468156L;

	public ServiceException() {
	}

	public ServiceException(String msg) {
		super(msg);
	}
}
