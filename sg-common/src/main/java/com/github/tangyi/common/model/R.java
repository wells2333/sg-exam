package com.github.tangyi.common.model;

import com.github.tangyi.common.constant.ApiMsg;
import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {

	public static final long serialVersionUID = 42L;

	private String message = ApiMsg.msg(ApiMsg.KEY_SUCCESS);

	private int code = ApiMsg.KEY_SUCCESS;

	private T result;

	public R() {
		super();
	}

	public R(T result) {
		super();
		this.result = result;
	}

	public R(T result, int code, String message) {
		super();
		this.result = result;
		this.code = code;
		this.message = message;
	}

	public R(T result, String msg) {
		super();
		this.result = result;
		this.message = msg;
	}

	public static R<Boolean> success() {
		return new R<>(Boolean.TRUE);
	}

	public static <T> R<T> success(T data) {
		return new R<>(data);
	}

	public static <T> R<T> error(String message) {
		return error(null, ApiMsg.KEY_ERROR, message);
	}

	public static <T> R<T> error(T data, String message) {
		return error(data, ApiMsg.KEY_ERROR, message);
	}

	public static <T> R<T> error(T data, int code, String message) {
		return new R<>(data, code, message);
	}
}
