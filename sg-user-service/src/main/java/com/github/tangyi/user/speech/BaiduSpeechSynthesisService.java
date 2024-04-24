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

package com.github.tangyi.user.speech;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class BaiduSpeechSynthesisService {

	private static final String BAIDU_APP_ID = EnvUtils.getValue("BAIDU_APP_ID", "26722003");
	private static final String BAIDU_API_KEY = EnvUtils.getValue("BAIDU_API_KEY", "Of2iwZNelH4mWa6XikrkhUFp");
	private static final String BAIDU_SECRET_KEY = EnvUtils.getValue("BAIDU_SECRET_KEY",
			"XOEszPwGNN4IKLzj9n7PT1YB1l3Vd67c");

	// 普通发音人选择：度小美=0(默认)，度小宇=1，，度逍遥（基础）=3，度丫丫=4
	// 精品发音人选择：度逍遥（精品）=5003，度小鹿=5118，度博文=106，度小童=110，度小萌=111，度米朵=103，度小娇=5
	private static final String BAIDU_PER = EnvUtils.getValue("BAIDU_PER", "4");

	private final AipSpeech client;
	private final AttachmentManager attachmentManager;

	public BaiduSpeechSynthesisService(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
		this.client = new AipSpeech(BAIDU_APP_ID, BAIDU_API_KEY, BAIDU_SECRET_KEY);
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
	}

	/**
	 * 语音合成
	 */
	public byte[] synthesis(String text) {
		HashMap<String, Object> options = Maps.newHashMap();
		// 语速，取值 0-9，默认为 5 中语速
		options.put("spd", "5");
		// 音调，取值 0-9，默认为 5 中语调
		options.put("pit", "5");
		options.put("per", BAIDU_PER);
		// 调用接口
		TtsResponse res = client.synthesis(text, "zh", 1, options);
		return res.getResult() == null ? res.getData() : null;
	}

	public void synthesisAndUpLoad(String text, SynthesisHandlerContext context) {
		byte[] bytes = synthesis(text);
		String fileName = context.getFileName();
		BytesUploadContext uploadContext = new BytesUploadContext();
		uploadContext.setGroup(AttachGroup.of(AttachTypeEnum.SPEECH));
		uploadContext.setUser(context.getUser());
		uploadContext.setTenantCode(context.getTenantCode());
		uploadContext.setFileName(fileName);
		uploadContext.setOriginalFilename(fileName);
		uploadContext.setBytes(bytes);
		context.setAttachment(attachmentManager.upload(uploadContext));
	}
}
