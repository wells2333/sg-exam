package com.github.tangyi.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.github.tangyi.api.auth.module.WxSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信Service
 *
 * @author tangyi
 * @date 2019/07/05 20:33
 */
@Slf4j
@AllArgsConstructor
@Service
public class WxSessionService {

    private final WxMaService wxMaService;

    /**
     * 根据code获取WxSession
     *
     * @param code code
     * @return WxSession
     * @author tangyi
     * @date 2019/07/05 20:37:02
     */
    public WxSession getSession(String code) {
        WxSession session = null;
        try {
            WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo(code);
            session = new WxSession(result.getOpenid(), result.getSessionKey());
            log.info("Get wx session success，openId: {}, sessionKey: {}", session.getOpenId(), session.getSessionKey());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return session;
    }

    /**
     * 根据code获取WxSession
     *
     * @param code code
     * @return WxSession
     * @author tangyi
     * @date 2019/07/06 14:01:13
     */
    public WxSession code2Session(String code) {
        WxSession session = null;
        try {
            WxMaJscode2SessionResult result = wxMaService.jsCode2SessionInfo(code);
            session = new WxSession(result.getOpenid(), result.getSessionKey());
            log.info("Get wx session success，openId: {}, sessionKey: {}", session.getOpenId(), session.getSessionKey());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return session;
    }
}
