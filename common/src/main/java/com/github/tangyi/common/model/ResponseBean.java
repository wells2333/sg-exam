package com.github.tangyi.common.model;

import com.github.tangyi.common.constant.ApiMsg;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装返回数据
 *
 * @author tangyi
 * @date 2019/3/17 12:08
 */
@Data
public class ResponseBean<T> implements Serializable {

	public static final long serialVersionUID = 42L;

	private String msg = ApiMsg.msg(ApiMsg.KEY_SUCCESS);

	private int code = ApiMsg.KEY_SUCCESS;

	private T data;

	public ResponseBean() {
		super();
	}

	public ResponseBean(T data) {
		super();
		this.data = data;
	}

	public ResponseBean(T data, int keyCode, int msgCode) {
		super();
		this.data = data;
		this.code = Integer.parseInt(keyCode + "" + msgCode);
		this.msg = ApiMsg.code2Msg(keyCode, msgCode);
	}

	public ResponseBean(T data, String msg) {
		super();
		this.data = data;
		this.msg = msg;
	}
}
