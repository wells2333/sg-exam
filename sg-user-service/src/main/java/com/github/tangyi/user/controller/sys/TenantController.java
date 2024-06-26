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
import com.github.tangyi.api.user.model.Tenant;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.service.sys.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Tag(name = "租户信息管理")
@RestController
@RequestMapping("/v1/tenant")
public class TenantController extends BaseController {

	private final TenantService tenantService;

	@Operation(summary = "获取租户信息", description = "根据租户 ID 获取租户详细信息")
	@GetMapping("/{id}")
	public R<Tenant> get(@PathVariable Long id) {
		return R.success(tenantService.get(id));
	}

	@GetMapping("anonymousUser/findTenantByTenantCode/{tenantCode}")
	public R<Tenant> findTenantByTenantCode(@PathVariable String tenantCode) {
		return R.success(tenantService.getByTenantCode(tenantCode));
	}

	@GetMapping("tenantList")
	@Operation(summary = "获取租户列表")
	public R<PageInfo<Tenant>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(tenantService.findPage(condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "创建租户", description = "创建租户")
	@SgLog(value = "新增租户", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid Tenant tenant) {
		tenant.setCommonValue(SysUtil.getUser(), tenant.getTenantCode());
		if (tenant.getStatus() == null) {
			tenant.setStatus(TenantConstant.PENDING_AUDIT);
		}
		return R.success(tenantService.add(tenant) > 0);
	}

	@PutMapping("/{id}")
	@Operation(summary = "更新租户信息", description = "根据租户 id 更新租户的基本信息")
	@SgLog(value = "修改租户", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Tenant tenant) {
		tenant.setId(id);
		tenant.setCommonValue(SysUtil.getUser(), tenant.getTenantCode());
		return R.success(tenantService.update(tenant) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除租户", description = "根据 ID 删除租户")
	@SgLog(value = "删除租户", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		Tenant tenant = tenantService.get(id);
		return R.success(tenantService.delete(tenant) > 0);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除租户", description = "根据租户 ID 批量删除租户")
	@SgLog(value = "批量删除租户", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(ArrayUtils.isNotEmpty(ids) ? tenantService.deleteAll(ids) > 0 : Boolean.FALSE);
	}

	@RequestMapping(value = "findById", method = RequestMethod.POST)
	@Operation(summary = "根据 ID 查询租户")
	public R<List<Tenant>> findById(@RequestBody Long[] ids) {
		List<Tenant> tenantList = tenantService.findListById(ids);
		return Optional.ofNullable(tenantList).isPresent() ? R.success(tenantList) : null;
	}
}
