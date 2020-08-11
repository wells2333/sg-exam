package com.github.tangyi.common.exceptions;

/**
 * 验证码错误异常
 *
 * @author tangyi
 * @date 2019/3/18 16:46
 */
public class InvalidValidateCodeException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public InvalidValidateCodeException() {
    }

    public InvalidValidateCodeException(String msg) {
        super(msg);
    }
}
