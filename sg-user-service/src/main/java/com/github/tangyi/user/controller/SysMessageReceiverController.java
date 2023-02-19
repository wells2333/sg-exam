package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.service.SysMessageReadService;
import com.github.tangyi.user.service.SysMessageReceiverService;
import com.github.tangyi.user.service.SysMessageService;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "消息接收管理")
@RequestMapping("/v1/receiver")
public class SysMessageReceiverController extends BaseController {

	private final SysMessageReceiverService sysMessageReceiverService;

	private final SysMessageService messageService;

	private final SysMessageReadService messageReadService;

	@GetMapping("/list")
	@Operation(summary = "查询消息接收列表")
	public R<PageInfo<SysMessageReceiver>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysMessageReceiverService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping("/userMessageList")
	@Operation(summary = "查询用户的消息接收列表")
	public R<PageInfo<SysMessage>> userMessageList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		Long userId = SysUtil.getUserId();
		PageInfo<SysMessage> page = new PageInfo<>();
		List<SysMessage> list = Lists.newArrayListWithExpectedSize(pageSize);
		PageInfo<SysMessageReceiver> receiverPage = sysMessageReceiverService.getPublishedMessage(condition, pageNum,
				pageSize);
		if (CollectionUtils.isNotEmpty(receiverPage.getList())) {
			for (SysMessageReceiver receiver : receiverPage.getList()) {
				SysMessage message = messageService.get(receiver.getMessageId());
				if (message != null) {
					if (messageReadService.findByMessageIdAndReceiverId(message.getId(), userId) != null) {
						message.setHasRead(true);
					}
					list.add(message);
				}
			}
		}
		BeanUtils.copyProperties(receiverPage, page);
		page.setList(list);
		return R.success(page);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取消息接收详细信息")
	public R<SysMessageReceiver> get(@PathVariable("id") Long id) {
		return R.success(sysMessageReceiverService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增消息接收")
	@SgLog(value = "新增消息接收", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SysMessageReceiver sysMessageReceiver) {
		sysMessageReceiver.setCommonValue();
		return R.success(sysMessageReceiverService.insert(sysMessageReceiver) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改消息接收")
	@SgLog(value = "修改消息接收", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid SysMessageReceiver sysMessageReceiver) {
		sysMessageReceiver.setId(id);
		return R.success(sysMessageReceiverService.update(sysMessageReceiver) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除消息接收")
	@SgLog(value = "删除消息接收", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		SysMessageReceiver sysMessageReceiver = sysMessageReceiverService.get(id);
		sysMessageReceiver.setCommonValue();
		return R.success(sysMessageReceiverService.delete(sysMessageReceiver) > 0);
	}
}
