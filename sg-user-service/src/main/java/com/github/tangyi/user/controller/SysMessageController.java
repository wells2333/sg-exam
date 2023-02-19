package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.SysMessageUserDto;
import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.service.SysMessageService;
import com.github.tangyi.user.service.UserService;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "消息管理")
@RequestMapping("/v1/message")
public class SysMessageController extends BaseController {

	private final SysMessageService sysMessageService;

	private final UserService userService;

	@GetMapping("/list")
	@Operation(summary = "查询消息列表")
	public R<PageInfo<SysMessage>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysMessageService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取消息详细信息")
	public R<SysMessage> get(@PathVariable("id") Long id) {
		return R.success(sysMessageService.get(id));
	}

	@GetMapping("selectUsers")
	@Operation(summary = "获取用户列表")
	public R<PageInfo<SysMessageUserDto>> selectUsers(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		PageInfo<SysMessageUserDto> dtoPage = new PageInfo<>();
		List<SysMessageUserDto> dtoList = Lists.newArrayList();
		PageInfo<User> userPage = userService.findPage(condition, pageNum, pageSize);
		List<User> users = userPage.getList();
		if (CollectionUtils.isNotEmpty(users)) {
			for (User user : users) {
				SysMessageUserDto dto = new SysMessageUserDto();
				dto.setId(user.getId());
				dto.setName(user.getName());
				dtoList.add(dto);
			}
		}
		dtoPage.setList(dtoList);
		return R.success(dtoPage);
	}

	@PostMapping
	@Operation(summary = "新增消息")
	@SgLog(value = "消息", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SysMessage sysMessage) {
		return R.success(sysMessageService.addMessage(sysMessage) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改消息")
	@SgLog(value = "消息", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysMessage sysMessage) {
		sysMessage.setId(id);
		sysMessage.setCommonValue(SysUtil.getUser(), SysUtil.getTenantCode());
		return R.success(sysMessageService.update(sysMessage) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除消息")
	@SgLog(value = "消息", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		SysMessage sysMessage = sysMessageService.get(id);
		sysMessage.setCommonValue();
		return R.success(sysMessageService.delete(sysMessage) > 0);
	}
}
