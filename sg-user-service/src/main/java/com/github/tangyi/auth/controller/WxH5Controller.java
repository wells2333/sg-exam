/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.auth.controller;

import com.github.tangyi.auth.service.WxH5Service;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * wx h5 扫码登录逻辑
 */
@Slf4j
@RestController
@RequestMapping("/v1/wx")
@AllArgsConstructor
public class WxH5Controller extends BaseController {

	private final WxH5Service wxH5Service;

	@GetMapping("getTicket")
	public R<Map<String, Object>> getTicket() {
		return R.success(wxH5Service.getTicket());
	}

	@PostMapping("checkSign")
	public R<Object> checkSign(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return wxH5Service.checkSign(req, res);
	}

	/**
	 *  根据二维码标识获取用户 openId => 获取用户信息
	 */
	@GetMapping("getOpenId")
	public R<Object> getOpenId(String sceneStr) {
		return R.success(wxH5Service.getOpenId(sceneStr));
	}
}
