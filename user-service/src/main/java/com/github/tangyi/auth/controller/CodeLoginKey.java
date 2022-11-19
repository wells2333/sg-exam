package com.github.tangyi.auth.controller;

import lombok.Data;

/**
 *
 *
 * @author tangyi
 * @date 2022/10/29 11:29 上午
 */
@Data
public class CodeLoginKey {
	private Long id;
	private String eventKey;
	private String openId;

	public CodeLoginKey(String eventKey, String openId) {
		this.eventKey = eventKey;
		this.openId = openId;
	}
}
