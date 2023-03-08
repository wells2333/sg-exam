package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.DeptDto;
import com.github.tangyi.api.common.IdNameDto;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.SelectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "下拉选择")
@RequestMapping("/v1/select")
public class SelectController extends BaseController {

	private final SelectService selectService;

	@GetMapping("userList")
	@Operation(summary = "获取用户列表")
	public R<PageInfo<IdNameDto>> userList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(selectService.userList(condition, pageNum, pageSize));
	}

	@GetMapping(value = "deptList")
	@Operation(summary = "获取部门列表")
	public R<List<DeptDto>> deptList() {
		return R.success(selectService.deptList());
	}
}
