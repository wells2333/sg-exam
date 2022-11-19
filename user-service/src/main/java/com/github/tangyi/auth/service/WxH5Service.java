package com.github.tangyi.auth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.auth.properties.WxH5Properties;
import com.github.tangyi.auth.security.core.user.CustomUserDetailsService;
import com.github.tangyi.auth.security.wx.WxUser;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.TenantContextHolder;
import com.github.tangyi.common.utils.okhttp.OkHttpUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author tangyi
 * @date 2022/10/30 4:04 下午
 */
@Slf4j
@Service
public class WxH5Service {

	public static final String WX_API_TICKET_URL = EnvUtils.getValue("WX_API_TICKET_URL",
			"https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=");

	public static final String WX_API_TOKEN_URL = EnvUtils.getValue("WX_API_TOKEN_URL",
			"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=");

	public static final String WX_API_EXPIRE_SECONDS = EnvUtils.getValue("WX_API_EXPIRE_SECONDS", "600");

	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	public static final String WX_H5_SCENE = "wx_h5_scene:";

	private final WxH5Properties wxH5Properties;

	private final UserService userService;

	private final CustomUserDetailsService userDetailsService;

	private final UserTokenService userTokenService;

	private final RedisTemplate redisTemplate;

	public WxH5Service(WxH5Properties wxH5Properties, UserService userService,
			CustomUserDetailsService userDetailsService, UserTokenService userTokenService,
			RedisTemplate redisTemplate) {
		this.wxH5Properties = wxH5Properties;
		this.userService = userService;
		this.userDetailsService = userDetailsService;
		this.userTokenService = userTokenService;
		this.redisTemplate = redisTemplate;
	}

	public String getAccessToken() {
		String url = WX_API_TOKEN_URL + wxH5Properties.getAppId() + "&secret=" + wxH5Properties.getAppSecret();
		String res = OkHttpUtil.getInstance().postJson(url, Maps.newHashMap(), Maps.newHashMap());
		JSONObject resJson = JSON.parseObject(res);
		return resJson == null ? "" : resJson.getString("access_token");
	}

	public JSONObject getUserInfo(String fromUser) throws Exception {
		// 查询用户信息
		String url =
				"https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAccessToken() + "&openid=" + fromUser
						+ "&lang=zh_CN";
		String result = OkHttpUtil.getInstance().get(url, Maps.newHashMap());
		return JSONObject.parseObject(result);
	}

	/**
	 * 获取二维码的ticket
	 * @return Map
	 */
	public Map<String, Object> getTicket() {
		String accessToken = getAccessToken();
		String url = WX_API_TICKET_URL + accessToken;
		Map<String, Object> data = Maps.newHashMap();
		Map<String, Object> actionInfo = Maps.newHashMap();
		Map<String, Object> scene = Maps.newHashMap();
		// 自定义参数，8位随机数
		String sceneStr = getRandomString(8);
		scene.put("scene_str", sceneStr);
		actionInfo.put("scene", scene);

		data.put("expire_seconds", WX_API_EXPIRE_SECONDS);
		data.put("action_name", "QR_STR_SCENE");
		data.put("action_info", actionInfo);
		String res = OkHttpUtil.getInstance().postJson(url, Maps.newHashMap(), data);
		JSONObject resJson = JSON.parseObject(res);
		Map<String, Object> map = Maps.newHashMap();
		map.put("ticket", resJson.get("ticket").toString());
		map.put("expire_seconds", resJson.get("expire_seconds").toString());
		map.put("url", resJson.get("url").toString());
		map.put("scene_str", sceneStr);
		return map;
	}

	public R<Object> checkSign(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echoStr = req.getParameter("echostr");
		String[] params = new String[]{timestamp, nonce, wxH5Properties.getToken()};
		Arrays.sort(params);
		String paramStr = params[0] + params[1] + params[2];
		// 加密
		byte[] digestResult = MessageDigest.getInstance("SHA-1").digest(paramStr.getBytes(StandardCharsets.UTF_8));
		String mySignature = bytes2HexString(digestResult).toLowerCase(Locale.ROOT);
		if (mySignature.equals(signature) && echoStr != null) {
			// 不正确就直接返回失败提示
			return R.error(echoStr);
		} else {
			return callback(req, res);
		}
	}

	/**
	 * 回调方法
	 * @param req req
	 * @return JSONObject
	 */
	public R<Object> callback(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WxMpXmlMessage message = WxMpXmlMessage.fromXml(req.getInputStream());
		String messageType = message.getMsgType();
		String messageEvent = message.getEvent();
		// openid，发送者帐号
		String fromUser = message.getFromUser();
		// 开发者微信号
		String toUser = message.getToUser();
		String text = message.getContent();
		// 生成二维码时穿过的特殊参数
		String sceneStr = message.getEventKey();
		log.info("消息类型: {}, 消息事件: {}, 发送者账号: {}, 接收者微信: {}, 文本消息: {}, 二维码参数: {}", messageType, messageEvent, fromUser,
				toUser, text, sceneStr);
		if (messageType.equals("event")) {
			// 设置租户code
			String tenantCode = SecurityConstant.DEFAULT_TENANT_CODE;
			TenantContextHolder.setTenantCode(tenantCode);
			// 先根据openid从查询出用户信息
			UserVo userVo = userService.findUserByIdentifier(IdentityType.WE_CHAT.getValue(), fromUser, tenantCode);
			// 没有该用户
			if (userVo == null) {
				JSONObject userInfo = getUserInfo(fromUser);
				WxUser wxUser = new WxUser();
				// 自动注册用户
				userVo = userDetailsService.registerUser(wxUser, fromUser, tenantCode);
			}
			// 扫码成功，存入缓存
			if (userVo != null) {
				CustomUserDetails details = userDetailsService.toCustomUserDetails(userVo);
				Map<String, Object> map = userTokenService.generateAndSaveToken(req, res, details, false);
				if (map != null) {
					saveSceneStr(sceneStr, map);
				}
				return R.success("success");
			}
		}
		return R.error("failed");
	}

	public Object getOpenId(String sceneStr) {
		if (StringUtils.isEmpty(sceneStr)) {
			return R.success(null);
		}
		Object valueObj = getSceneStr(sceneStr);
		return valueObj == null ? R.success(null) : valueObj;
	}

	public void saveSceneStr(String sceneStr, Map<String, Object> map) {
		redisTemplate.opsForValue()
				.set(WX_H5_SCENE + sceneStr, JSON.toJSONString(map), Integer.parseInt(WX_API_EXPIRE_SECONDS));
	}

	public Object getSceneStr(String sceneStr) {
		Object valueObj = redisTemplate.opsForValue().get(WX_H5_SCENE + sceneStr);
		return valueObj == null ? null : JSON.parseObject(valueObj.toString());
	}

	/**
	 * 从字节数组到十六进制字符串转换
	 */
	public String bytes2HexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	public String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		ThreadLocalRandom random = ThreadLocalRandom.current();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			builder.append(str.charAt(number));
		}
		return builder.toString();
	}
}
