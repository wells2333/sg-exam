package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.model.Tenant;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.log.constants.OperationType;
import com.github.tangyi.user.service.TenantService;
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

/**
 * 租户管理Controller
 *
 * @author tangyi
 * @date 2019/5/22 22:52
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "租户信息管理")
@RestController
@RequestMapping("/v1/tenant")
public class TenantController extends BaseController {

	private final TenantService tenantService;

	@Operation(summary = "获取租户信息", description = "根据租户id获取租户详细信息")
	@GetMapping("/{id}")
	public R<Tenant> get(@PathVariable Long id) {
		return R.success(tenantService.get(id));
	}

	/**
	 * 根据租户标识获取
	 */
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
		tenant.setStatus(TenantConstant.PENDING_AUDIT);
		return R.success(tenantService.add(tenant) > 0);
	}

	@PutMapping("/{id}")
	@Operation(summary = "更新租户信息", description = "根据租户id更新租户的基本信息")
	@SgLog(value = "修改租户", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid Tenant tenant) {
		tenant.setId(id);
		tenant.setCommonValue(SysUtil.getUser(), tenant.getTenantCode());
		return R.success(tenantService.update(tenant) > 0);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除租户", description = "根据ID删除租户")
	@SgLog(value = "删除租户", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		Tenant tenant = tenantService.get(id);
		tenant.setCommonValue();
		return R.success(tenantService.delete(tenant) > 0);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除租户", description = "根据租户id批量删除租户")
	@SgLog(value = "批量删除租户", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(ArrayUtils.isNotEmpty(ids) ? tenantService.deleteAll(ids) > 0 : Boolean.FALSE);
	}

	@RequestMapping(value = "findById", method = RequestMethod.POST)
	@Operation(summary = "根据ID查询租户")
	public R<List<Tenant>> findById(@RequestBody Long[] ids) {
		List<Tenant> tenantList = tenantService.findListById(ids);
		return Optional.ofNullable(tenantList).isPresent() ? R.success(tenantList) : null;
	}

	@PostMapping("init/{id}")
	@Operation(summary = "租户初始化")
	@SgLog(value = "租户初始化", operationType = OperationType.UPDATE)
	public R<Boolean> init(@PathVariable Long id) {
		Tenant tenant = tenantService.get(id);
		SgPreconditions.checkNull(tenant, "tenant not found");
		if (TenantConstant.NOT_INIT.equals(tenant.getInitStatus())) {
			tenantService.asyncInit(tenant);
		}
		return R.success(Boolean.TRUE);
	}
}
