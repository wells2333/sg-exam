package com.github.tangyi.common.exceptions;

import java.io.Serial;

public class TenantNotFoundException extends CommonException {

	@Serial
    private static final long serialVersionUID = -7285211528095468156L;

    public TenantNotFoundException() {
    }

    public TenantNotFoundException(String msg) {
        super(msg);
    }
}
