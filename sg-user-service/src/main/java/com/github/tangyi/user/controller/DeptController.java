package com.github.tangyi.user.controller;

import com.github.tangyi.api.user.dto.DeptDto;
import com.github.tangyi.api.user.model.Dept;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.user.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Tag(name = "部门管理")
@RestController
@RequestMapping("/v1/dept")
public class DeptController extends BaseController {

	private final DeptService deptService;

	/**
	 * 查询树形部门集合
	 */
	@GetMapping(value = "deptList")
	@Operation(summary = "获取部门列表")
	public R<List<DeptDto>> list() {
		return R.success(deptService.deptList());
	}

	@GetMapping("/{id}")
	@Operation(summary = "获取部门信息", description = "根据部门id获取部门详细信息")
	public Dept get(@PathVariable Long id) {
		return deptService.get(id);
	}

	@PostMapping
	@Operation(summary = "创建部门", description = "创建部门")
	@SgLog(value = "新增部门", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid Dept dept) {
		dept.setCommonValue();
		if (dept.getParentId() == null) {
			dept.setParentId(-1L);
		}
		return R.success(deptService.insert(dept) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除部门", description = "根据ID删除部门")
	@SgLog(value = "删除部门", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		Dept dept = new Dept();
		dept.setId(id);
		dept.setCommonValue();
		return R.success(deptService.delete(dept) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新部门信息", description = "根据部门id更新部门的基本信息")
	@SgLog(value = "更新部门", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Dept dept) {
		dept.setCommonValue();
		dept.setId(id);
		return R.success(deptService.update(dept) > 0);
	}

	@RequestMapping(value = "findById", method = RequestMethod.POST)
	@Operation(summary = "批量查询部门信息", description = "根据Ids批量查询信息")
	public R<List<DeptVo>> findById(@RequestBody Long[] ids) {
		return R.success(deptService.findById(ids));
	}
}
