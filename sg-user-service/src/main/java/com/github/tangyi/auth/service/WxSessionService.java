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
			log.info("get wx session success，openId: {}, sessionKey: {}", session.getOpenId(), session.getSessionKey());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return session;
	}
}
