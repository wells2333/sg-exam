package com.github.tangyi.common.utils;

import com.github.tangyi.common.constant.ApiMsg;
import com.github.tangyi.common.model.ResponseBean;

/**
 * @author tangyi
 * @date 2019-10-08 12:03
 */
public class ResponseUtil {

	private ResponseUtil() {
	}

	/**
	 * 是否成功
	 * @param responseBean responseBean
	 * @return boolean
	 */
	public static boolean isSuccess(ResponseBean<?> responseBean) {
		return responseBean != null && responseBean.getCode() == ApiMsg.KEY_SUCCESS;
	}
}
