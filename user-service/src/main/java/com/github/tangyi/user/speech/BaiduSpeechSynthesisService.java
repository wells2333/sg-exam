package com.github.tangyi.user.speech;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.user.service.QiNiuService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
public class BaiduSpeechSynthesisService {

	public static final String BAIDU_APP_ID = EnvUtils.getValue("BAIDU_APP_ID", "26722003");

	public static final String BAIDU_API_KEY = EnvUtils.getValue("BAIDU_API_KEY", "Of2iwZNelH4mWa6XikrkhUFp");

	public static final String BAIDU_SECRET_KEY = EnvUtils.getValue("BAIDU_SECRET_KEY",
			"XOEszPwGNN4IKLzj9n7PT1YB1l3Vd67c");

	// 普通发音人选择：度小美=0(默认)，度小宇=1，，度逍遥（基础）=3，度丫丫=4
	// 精品发音人选择：度逍遥（精品）=5003，度小鹿=5118，度博文=106，度小童=110，度小萌=111，度米朵=103，度小娇=5
	public static final String BAIDU_PER = EnvUtils.getValue("BAIDU_PER", "4");

	private final AipSpeech client;

	private final QiNiuService qiNiuService;

	public BaiduSpeechSynthesisService(QiNiuService qiNiuService) {
		this.qiNiuService = qiNiuService;
		this.client = new AipSpeech(BAIDU_APP_ID, BAIDU_API_KEY, BAIDU_SECRET_KEY);
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
	}

	/**
	 * 语音合成
	 */
	public byte[] synthesis(String text) {
		HashMap<String, Object> options = Maps.newHashMap();
		// 语速，取值0-9，默认为5中语速
		options.put("spd", "5");
		// 音调，取值0-9，默认为5中语调
		options.put("pit", "5");
		options.put("per", BAIDU_PER);
		// 调用接口
		TtsResponse res = client.synthesis(text, "zh", 1, options);
		return res.getResult() == null ? res.getData(): null;
	}

	public void synthesisAndUpLoad(String text, SynthesisHandlerContext context) {
		byte[] bytes = synthesis(text);
		String fileName = context.getFileName();
		Attachment attachment = qiNiuService.upload(context.getGroupCode(), fileName, fileName, bytes,
				context.getUser(), context.getTenantCode());
		context.setAttachment(attachment);
	}
}
