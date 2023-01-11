package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.RoleMenu;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.user.service.RoleMenuService;
import com.github.tangyi.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "角色信息管理")
@RestController
@RequestMapping("/v1/role")
public class RoleController extends BaseController {

	private final RoleService roleService;

	private final RoleMenuService roleMenuService;

	@GetMapping("/{id}")
	@Operation(summary = "获取角色信息", description = "根据角色id获取角色详细信息")
	public Role get(@PathVariable Long id) {
		return roleService.get(id);
	}

	@GetMapping("roleList")
	@Operation(summary = "获取角色列表")
	public R<PageInfo<Role>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(roleService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping("allRoles")
	@Operation(summary = "获取全部角色列表")
	public R<List<Role>> allRoles(Role role) {
		role.setTenantCode(SysUtil.getTenantCode());
		return R.success(roleService.findAllList(role));
	}

	@PutMapping("{id}")
	@Operation(summary = "更新角色信息", description = "根据角色id更新角色的基本信息")
	@SgLog(value = "修改角色", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Role role) {
		boolean success = false;
		role.setCommonValue();
		role.setId(id);
		if (roleService.update(role) > 0) {
			success = roleMenuService.saveRoleMenus(role.getId(), role.getMenuIds()) > 0;
		}
		return R.success(success);
	}

	@GetMapping("roleMenus/{id}")
	@Operation(summary = "查询角色菜单信息", description = "查询角色菜单信息")
	public R<List<RoleMenu>> roleMenus(@PathVariable Long id) {
		return R.success(roleMenuService.getByRoleId(id));
	}

	@PutMapping("roleMenuUpdate")
	@Operation(summary = "更新角色菜单信息", description = "更新角色菜单信息")
	@SgLog(value = "更新角色菜单", operationType = OperationType.UPDATE)
	public R<Boolean> updateRoleMenu(@RequestBody Role role) {
		boolean success = false;
		if (role.getId() != null) {
			role = roleService.get(role.getId());
			if (role != null) {
				success = roleMenuService.saveRoleMenus(role.getId(), role.getMenuIds()) > 0;
			}
		}
		return R.success(success);
	}

	@PostMapping("setRoleStatus/{id}")
	@Operation(summary = "更新角色状态", description = "更新角色状态")
	@SgLog(value = "更新角色状态", operationType = OperationType.UPDATE)
	public R<Boolean> setRoleStatus(@PathVariable Long id, @RequestBody Role role) {
		role.setId(id);
		role.setCommonValue();
		return R.success(roleService.update(role) > 0);
	}

	@PostMapping
	@Operation(summary = "创建角色", description = "创建角色")
	@SgLog(value = "新增角色", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid Role role) {
		role.setCommonValue();
		return R.success(roleService.insert(role) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除角色", description = "根据ID删除角色")
	@SgLog(value = "删除角色", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		Role role = new Role();
		role.setId(id);
		role.setCommonValue();
		return R.success(roleService.delete(role) > 0);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除角色", description = "根据角色id批量删除角色")
	@SgLog(value = "批量删除角色", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(ArrayUtils.isNotEmpty(ids) ? roleService.deleteAll(ids) > 0 : Boolean.FALSE);
	}
}
