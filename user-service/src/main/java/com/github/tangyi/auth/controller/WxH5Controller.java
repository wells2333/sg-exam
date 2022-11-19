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
 * wx h5扫码登录逻辑
 * @author tangyi
 * @date 2022/10/28 11:10 下午
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
	 *  根据二维码标识获取用户openId => 获取用户信息
	 */
	@GetMapping("getOpenId")
	public R<Object> getOpenId(String sceneStr) {
		return R.success(wxH5Service.getOpenId(sceneStr));
	}
}
