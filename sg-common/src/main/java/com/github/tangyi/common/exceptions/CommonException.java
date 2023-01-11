package com.github.tangyi.common.exceptions;

import java.io.Serial;

public class CommonException extends RuntimeException {

	@Serial
    private static final long serialVersionUID = 1L;

    public CommonException() {

    }

    public CommonException(String msg) {
        super(msg);
    }

    public CommonException(Throwable throwable) {
        super(throwable);
    }

    public CommonException(Throwable throwable, String msg) {
        super(throwable);
    }
}
