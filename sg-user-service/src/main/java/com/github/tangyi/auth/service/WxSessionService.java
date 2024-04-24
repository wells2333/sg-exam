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

package com.github.tangyi.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.github.tangyi.api.other.model.WxSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class WxSessionService {

	private final WxMaService wxMaService;

	public WxSession code2Session(String code) {
		WxSession session = null;
		try {
			WxMaJscode2SessionResult result = wxMaService.jsCode2SessionInfo(code);
			session = new WxSession(result.getOpenid(), result.getSessionKey());
			log.info("Get wx session success, openId: {}, sessionKey: {}", session.getOpenId(), session.getSessionKey());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return session;
	}
}
