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
