package com.github.tangyi.user.controller;

import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.EnvUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "公告管理")
@RequestMapping("/v1/notice")
public class SysNoticeController extends BaseController {

	public static final String NOTICE_VALUE = EnvUtils.getValue("NOTICE_VALUE", "");

	@GetMapping("/getNotice")
	@Operation(summary = "查询公告")
	public R<String> getNotice() {
		return R.success(NOTICE_VALUE);
	}
}
