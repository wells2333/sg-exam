package com.github.tangyi.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装常用的业务错误码和提示信息
 */
public class ApiMsg {

	private ApiMsg() {
	}

	/**
	 * 失败
	 */
	public static final int FAILED = 2;

	/**
	 * NULL
	 */
	public static final int NULL = 3;

	/**
	 * 找不到
	 */
	public static final int NOT_FOUND = 4;

	/**
	 * 不可用
	 */
	public static final int UNAVAILABLE = 5;

	/**
	 * 超时
	 */
	public static final int EXPIRED = 6;

	/**
	 * 非法
	 */
	public static final int INVALID = 7;

	/**
	 * 拒绝
	 */
	public static final int DENIED = 8;

	// =============== 业务key ================

	/**
	 * 成功
	 */
	public static final int KEY_SUCCESS = 0;

	/**
	 * 错误
	 */
	public static final int KEY_ERROR = 1;

	/**
	 * 未知内容
	 */
	public static final int KEY_UNKNOWN = 400;


	public static final int KEY_TOKEN = 401;

	/**
	 * 验证码
	 */
	public static final int KEY_VALIDATE_CODE = 402;

	public static final int KEY_SERVICE = 403;

	/**
	 * 访问
	 */
	public static final int KEY_ACCESS = 404;

	/**
	 * 认证
	 */
	public static final int KEY_AUTHENTICATION = 405;

	/**
	 * 参数校验
	 */
	public static final int KEY_PARAM_VALIDATE = 406;

	/**
	 * code和提示内容的对应关系
	 */
	private static final Map<Integer, String> CODE_MAP = new HashMap<>();

	/**
	 * code和提示内容的对应关系
	 */
	private static final Map<Integer, String> KEY_MAP = new HashMap<>();

	static {
		CODE_MAP.put(KEY_SUCCESS, "SUCCESS");
		CODE_MAP.put(KEY_ERROR, "ERROR");
		CODE_MAP.put(FAILED, "FAILED");
		CODE_MAP.put(NULL, "NULL");
		CODE_MAP.put(NOT_FOUND, "NOT_FOUND");
		CODE_MAP.put(UNAVAILABLE, "UNAVAILABLE");
		CODE_MAP.put(EXPIRED, "EXPIRED");
		CODE_MAP.put(INVALID, "INVALID");
		CODE_MAP.put(DENIED, "DENIED");
	}

	static {
		KEY_MAP.put(KEY_ERROR, "");
		KEY_MAP.put(KEY_UNKNOWN, "UNKNOWN");
		KEY_MAP.put(KEY_SERVICE, "SERVICE");
		KEY_MAP.put(KEY_VALIDATE_CODE, "VALIDATE CODE");
		KEY_MAP.put(KEY_TOKEN, "TOKEN");
		KEY_MAP.put(KEY_ACCESS, "ACCESS");
		KEY_MAP.put(KEY_PARAM_VALIDATE, "PARAM_VALIDATE");
	}

	public static String code2Msg(int codeKey, int msgKey) {
		return KEY_MAP.get(codeKey) + " " + CODE_MAP.get(msgKey);
	}

	public static String msg(int code) {
		return CODE_MAP.get(code);
	}
}
