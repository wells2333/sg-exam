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

package com.github.tangyi.exam.controller.subject;

import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.JsonMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Tag(name = "选项信息管理")
@RestController
@RequestMapping("/v1/option")
public class OptionController {

	private static final String DEFAULT_OPTIONS = EnvUtils.getValue("DEFAULT_OPTIONS",
			"[{\"labelName\":\"A\"},{\"labelName\":\"B\"},{\"labelName\":\"C\"},{\"labelName\":\"D\"}]");

	@SuppressWarnings("unchecked")
	private static final List<LinkedHashMap<String, String>> OPTIONS = JsonMapper.getInstance()
			.fromJson(DEFAULT_OPTIONS, List.class);

	@GetMapping("defaultOptions")
	public R<List<LinkedHashMap<String, String>>> defaultOptions() {
		return R.success(OPTIONS);
	}
}
