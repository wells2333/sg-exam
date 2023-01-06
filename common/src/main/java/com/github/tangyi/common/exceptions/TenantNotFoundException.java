package com.github.tangyi.common.exceptions;

public class TenantNotFoundException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public TenantNotFoundException() {
    }

    public TenantNotFoundException(String msg) {
        super(msg);
    }
}
