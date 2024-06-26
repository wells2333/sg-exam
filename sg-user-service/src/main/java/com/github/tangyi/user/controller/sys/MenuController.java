/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.user.controller.sys;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.dto.MenuDto;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.excel.listener.MenuImportListener;
import com.github.tangyi.user.excel.model.MenuExcelModel;
import com.github.tangyi.user.service.sys.MenuService;
import com.github.tangyi.user.utils.MenuUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Tag(name = "菜单信息管理")
@RestController
@RequestMapping("/v1/menu")
public class MenuController extends BaseController {

	private final MenuService menuService;

	@GetMapping(value = "userMenu")
	@Operation(summary = "获取当前用户的菜单列表")
	public R<List<MenuDto>> userMenu() {
		return R.success(menuService.findUserMenus(SysUtil.getTenantCode(), SysUtil.getUser()));
	}

	@GetMapping(value = "userPermissions")
	@Operation(summary = "获取当前用户的权限编码")
	public R<Set<String>> userPermissions() {
		return R.success(menuService.findUserMenuPermissions(SysUtil.getTenantCode(), SysUtil.getUser()));
	}

	@GetMapping(value = "defaultTenantMenu")
	@Operation(summary = "获取默认租户的菜单列表")
	public R<List<MenuDto>> defaultTenantMenu() {
		return R.success(menuService.findDefaultTenantMenu(TenantConstant.DEFAULT_TENANT_CODE, true));
	}

	@GetMapping(value = "menuTree")
	@Operation(summary = "获取树形菜单列表")
	public R<List<MenuDto>> menuTree() {
		return R.success(menuService.menuTree(SysUtil.getTenantCode()));
	}

	@PostMapping
	@Operation(summary = "创建菜单", description = "创建菜单")
	@SgLog(value = "新增菜单", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid Menu menu) {
		menu.setCommonValue();
		if (menu.getParentId() == null) {
			menu.setParentId(-1L);
		}
		return R.success(menuService.insert(menu) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新菜单信息", description = "根据菜单 ID 更新菜单的基本信息")
	@SgLog(value = "更新菜单", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Menu menu) {
		menu.setCommonValue();
		menu.setId(id);
		return R.success(menuService.update(menu) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除菜单", description = "根据 ID 删除菜单")
	@SgLog(value = "删除菜单", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		Menu menu = new Menu();
		menu.setId(id);
		return R.success(menuService.delete(menu) > 0);
	}

	@GetMapping("/{id}")
	@Operation(summary = "获取菜单信息", description = "根据菜单 ID 获取菜单详细信息")
	public Menu menu(@PathVariable Long id) {
		return menuService.get(id);
	}

	@GetMapping("menuList")
	@Operation(summary = "获取菜单列表")
	public PageInfo<Menu> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return menuService.findPage(condition, pageNum, pageSize);
	}

	@GetMapping("anonymousUser/findMenuByRole/{role}")
	@Operation(summary = "根据角色查找菜单", description = "根据角色 id 获取角色菜单")
	public R<List<Menu>> findMenuByRole(@PathVariable String role, @RequestParam @NotBlank String tenantCode) {
		return R.success(menuService.findMenuByRole(role, tenantCode));
	}

	@GetMapping("anonymousUser/findAllMenu")
	@Operation(summary = "查询所有菜单", description = "查询所有菜单")
	public R<List<Menu>> findAllMenu(@RequestParam @NotBlank String tenantCode) {
		Menu menu = new Menu();
		menu.setTenantCode(tenantCode);
		return R.success(menuService.findAllList(menu));
	}

	@GetMapping("roleTree/{roleCode}")
	@Operation(summary = "根据角色查找菜单", description = "根据角色 code 获取角色菜单")
	public List<String> roleTree(@PathVariable String roleCode) {
		Stream<Menu> menuStream = menuService.findMenuByRole(roleCode, SysUtil.getTenantCode()).stream();
		if (Optional.ofNullable(menuStream).isPresent()) {
			return menuStream.map(menu -> menu.getId().toString()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@GetMapping("roleTreeNoTenant")
	@Operation(summary = "根据当前角色查找菜单,无租户", description = "根据当前角色查找菜单,无租户")
	public R<List<MenuDto>> roleTreeNoTenant() {
		Collection<? extends GrantedAuthority> authorities = SysUtil.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority firstAuthority = null;
		List<String> list = new ArrayList<>();
		if (iterator.hasNext()) {
			firstAuthority = iterator.next();
			String roleCode = firstAuthority.getAuthority();
			list.add(roleCode);
		}
		List<MenuDto> roleNoTeNantCode = menuService.findByRoleNoTeNantCode(list);
		return R.success(roleNoTeNantCode);
	}


	@PostMapping("export")
	@Operation(summary = "导出菜单", description = "根据菜单 ID 导出菜单")
	@SgLog(value = "导出菜单", operationType = OperationType.EXPORT)
	public void exportMenu(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		List<Menu> menus;
		if (ArrayUtils.isEmpty(ids)) {
			Menu menu = new Menu();
			// 导出当前租户下的所有菜单
			menu.setTenantCode(SysUtil.getTenantCode());
			menus = menuService.findList(menu);
		} else {
			// 导出选中
			menus = menuService.findListById(ids);
		}
		ExcelToolUtil.writeExcel(request, response, MenuUtil.convertToExcelModel(menus), MenuExcelModel.class);
	}

	@PostMapping("import")
	@Operation(summary = "导入菜单", description = "导入菜单")
	@SgLog(value = "导入菜单", operationType = OperationType.IMPORT)
	public R<Boolean> importMenu(@Parameter(description = "要上传的文件", required = true) MultipartFile file)
			throws IOException {
		return R.success(ExcelToolUtil.readExcel(file.getInputStream(), MenuExcelModel.class,
				new MenuImportListener(menuService)));
	}
}
