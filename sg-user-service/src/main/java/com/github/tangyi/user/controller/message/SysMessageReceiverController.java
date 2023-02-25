package com.github.tangyi.user.controller.message;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.message.SysMessageReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "消息接收管理")
@RequestMapping("/v1/receiver")
public class SysMessageReceiverController extends BaseController {

	private final SysMessageReceiverService sysMessageReceiverService;

	@GetMapping("/list")
	@Operation(summary = "查询消息接收列表")
	public R<PageInfo<SysMessageReceiver>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(sysMessageReceiverService.findPage(condition, pageNum, pageSize));
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
