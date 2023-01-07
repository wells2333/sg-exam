package com.github.tangyi.common.exceptions;

import java.io.Serial;

public class InvalidAccessTokenException extends CommonException {

	@Serial
    private static final long serialVersionUID = -7285211528095468156L;

    public InvalidAccessTokenException() {
    }

    public InvalidAccessTokenException(String msg) {
        super(msg);
    }
}
