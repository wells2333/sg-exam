package com.github.tangyi.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author tangyi
 * @date 2020/5/16 11:12 上午
 */
@RestController
public class TestController {

	@GetMapping("/user/test")
	public String test() {
		return "test";
	}
}
