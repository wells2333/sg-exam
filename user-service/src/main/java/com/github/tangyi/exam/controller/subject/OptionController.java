package com.github.tangyi.exam.controller.subject;

import com.beust.jcommander.internal.Lists;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.JsonMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/3/31 1:38 下午
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "选项信息管理")
@RestController
@RequestMapping("/v1/option")
public class OptionController {

	public static final String DEFAULT_OPTIONS = EnvUtils.getValue("DEFAULT_OPTIONS",
			"[{\"labelName\":\"A\"},{\"labelName\":\"B\"},{\"labelName\":\"C\"},{\"labelName\":\"D\"}]");

	private final List<LinkedHashMap<String, String>> defaultOptionsList = Lists.newArrayList();

	@PostConstruct
	public void init() {
		try {
			List<LinkedHashMap<String, String>> list = JsonMapper.getInstance().fromJson(DEFAULT_OPTIONS, List.class);
			if (CollectionUtils.isNotEmpty(list)) {
				defaultOptionsList.addAll(list);
			}
		} catch (Exception e) {
			log.error("init default option failed", e);
		}
	}

	/**
	 * 获取默认的选项名称
	 * @return R
	 */
	@GetMapping("defaultOptions")
	public R<List<LinkedHashMap<String, String>>> defaultOptions() {
		return R.success(defaultOptionsList);
	}
}
