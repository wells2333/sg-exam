package com.github.tangyi.common.exceptions;

import java.io.Serial;

public class InvalidValidateCodeException extends CommonException {

    @Serial
	private static final long serialVersionUID = -7285211528095468156L;

    public InvalidValidateCodeException() {
    }

    public InvalidValidateCodeException(String msg) {
        super(msg);
    }
}
