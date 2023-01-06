package com.github.tangyi.user.controller;

import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.user.service.UserService;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Properties;

@Slf4j
@AllArgsConstructor
@Tag(name = "生成验证码")
@RestController
@RequestMapping(value = "/v1/code")
public class ValidateCodeController extends BaseController {

	private final Map<String, Producer> producerMap = Maps.newHashMap();

	private final UserService userService;

	/**
	 * 生成验证码
	 */
	@Operation(summary = "生成验证码", description = "生成验证码")
	@GetMapping("/{random}")
	public void produceCode(@PathVariable String random,
			@RequestParam(required = false, defaultValue = "130") String width,
			@RequestParam(required = false, defaultValue = "50") String height, HttpServletResponse response)
			throws Exception {
		response.setHeader("Cache-Control", "no-store, no-com.github.tangyi.common.basic.cache");
		response.setContentType("image/jpeg");
		Producer producer = getProducer(width, height);
		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		userService.saveImageCode(random, text);
		try (ServletOutputStream out = response.getOutputStream()) {
			ImageIO.write(image, "JPEG", out);
		}
	}

	public Producer getProducer(String width, String height) {
		String key = width + height;
		if (!producerMap.containsKey(key)) {
			synchronized (producerMap) {
				if (!producerMap.containsKey(key)) {
					producerMap.put(key, createProducer(width, height));
				}
			}
		}
		return producerMap.get(key);
	}

	public Producer createProducer(String width, String height) {
		Properties properties = new Properties();
		properties.put("kaptcha.border", "no");
		// 验证码字体颜色
		properties.put("kaptcha.textproducer.font.color", "black");
		// 验证码字体大小
		properties.put("kaptcha.textproducer.font.size", "40");
		// 验证码文本字符间距
		properties.put("kaptcha.textproducer.char.space", "6");
		// 验证码宽度
		properties.put("kaptcha.image.width", width);
		// 验证码高度
		properties.put("kaptcha.image.height", height);
		properties.put("kaptcha.textproducer.char.length", "4");
		Config config = new Config(properties);
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		defaultKaptcha.setConfig(config);
		log.info("createProducer success, width: {}, height: {}", width, height);
		return defaultKaptcha;
	}
}
