package com.github.tangyi.common.exceptions;

/**
 * 租户不存在异常
 *
 * @author tangyi
 * @date 2019/5/26 10:14
 */
public class TenantNotFoundException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public TenantNotFoundException() {
    }

    public TenantNotFoundException(String msg) {
        super(msg);
    }
}
