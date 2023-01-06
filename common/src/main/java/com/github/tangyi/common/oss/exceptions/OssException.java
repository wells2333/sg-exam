package com.github.tangyi.common.oss.exceptions;

import com.github.tangyi.common.exceptions.CommonException;

public class OssException extends CommonException {

	public OssException(String msg) {
		super(msg);
	}

	public OssException(Throwable throwable, String msg) {
		super(throwable, msg);
	}
}
