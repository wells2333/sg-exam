package com.github.tangyi.user.controller.attach;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.user.service.attach.AttachGroupService;
import com.qiniu.common.QiniuException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "附件分组管理")
@RestController
@RequestMapping("/v1/attachGroup")
public class AttachGroupController extends BaseController {

	private final AttachGroupService groupService;

	@Operation(summary = "获取分组信息")
	@GetMapping("/{id}")
	public R<AttachGroup> get(@PathVariable Long id) {
		return R.success(groupService.get(id));
	}

	@GetMapping("groupList")
	@Operation(summary = "获取分组列表")
	public R<PageInfo<AttachGroup>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(groupService.findPage(condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "创建分组")
	@SgLog(value = "创建分组", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid AttachGroup group) {
		return R.success(groupService.insert(group) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新分组信息")
	@SgLog(value = "更新分组", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid AttachGroup group) {
		group.setId(id);
		return R.success(groupService.update(group) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除分组")
	@SgLog(value = "删除分组", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		AttachGroup group = groupService.get(id);
		SgPreconditions.checkNull(group, "group does not exist");
		return R.success(groupService.delete(group) > 0);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除分组")
	@SgLog(value = "批量删除分组", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) throws QiniuException {
		return R.success(groupService.deleteAll(ids) > 0);
	}
}
