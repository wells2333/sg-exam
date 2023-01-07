package com.github.tangyi.common.exceptions;

import java.io.Serial;

public class ValidateCodeExpiredException extends CommonException {

	@Serial
    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeExpiredException() {
    }

    public ValidateCodeExpiredException(String msg) {
        super(msg);
    }
}
