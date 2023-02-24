package com.github.tangyi.user.controller.message;


import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.message.MobileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Tag(name = "手机管理")
@RestController
@RequestMapping("/v1/mobile")
public class MobileController extends BaseController {

	private final MobileService mobileService;

	@GetMapping("sendSms/{mobile}")
	@Operation(summary = "发送短信", description = "发送短信到指定的手机号")
	@SgLog(value = "发送短信", operationType = OperationType.INSERT)
	public R<Boolean> sendSms(@PathVariable String mobile) {
		return mobileService.sendVerificationCode(mobile);
	}
}
