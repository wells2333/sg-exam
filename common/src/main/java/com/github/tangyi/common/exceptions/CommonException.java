package com.github.tangyi.common.exceptions;

/**
 * 公共异常
 *
 * @author tangyi
 * @date 2019/3/16 20:28
 */
public class CommonException extends RuntimeException {

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
