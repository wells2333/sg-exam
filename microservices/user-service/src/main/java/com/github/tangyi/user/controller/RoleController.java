package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.module.Role;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.user.service.RoleMenuService;
import com.github.tangyi.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 角色controller
 *
 * @author tangyi
 * @date 2018/8/26 22:50
 */
@Slf4j
@AllArgsConstructor
@Api("角色信息管理")
@RestController
@RequestMapping("/v1/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    private final RoleMenuService roleMenuService;

    /**
     * 根据id获取角色
     *
     * @param id id
     * @return RoleVo
     * @author tangyi
     * @date 2018/9/14 18:20
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取角色信息", notes = "根据角色id获取角色详细信息")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long", paramType = "path")
    public Role role(@PathVariable Long id) {
       return roleService.get(id);
    }

    /**
     * 角色分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param role     role
     * @return PageInfo
     * @author tangyi
     * @date 2018/10/24 22:13
     */
    @GetMapping("roleList")
    @ApiOperation(value = "获取角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "role", value = "角色信息", dataType = "RoleVo")
    })
    public PageInfo<Role> roleList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                   Role role) {
        role.setTenantCode(SysUtil.getTenantCode());
        return roleService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), role);
    }

    /**
     * 查询所有角色
     *
     * @param role role
     * @return ResponseBean
     * @author tangyi
     * @date 2019/05/15 23:29
     */
    @GetMapping("allRoles")
    @ApiOperation(value = "获取全部角色列表")
    @ApiImplicitParam(name = "role", value = "角色信息", dataType = "RoleVo")
    public ResponseBean<List<Role>> allRoles(Role role) {
        role.setApplicationCode(SysUtil.getSysCode());
        role.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(roleService.findAllList(role));
    }

    /**
     * 修改角色
     *
     * @param role role
     * @return ResponseBean
     * @author tangyi
     * @date 2018/9/14 18:22
     */
    @PutMapping
    @ApiOperation(value = "更新角色信息", notes = "根据角色id更新角色的基本信息")
    @ApiImplicitParam(name = "role", value = "角色实体role", required = true, dataType = "RoleVo")
	@Log("修改角色")
    public ResponseBean<Boolean> updateRole(@RequestBody @Valid Role role) {
        role.setCommonValue();
        return new ResponseBean<>(roleService.update(role) > 0);
    }

    /**
     * 更新角色菜单
     *
     * @param role role
     * @return ResponseBean
     * @author tangyi
     * @date 2018/10/28 14:20
     */
    @PutMapping("roleMenuUpdate")
    @ApiOperation(value = "更新角色菜单信息", notes = "更新角色菜单信息")
    @ApiImplicitParam(name = "role", value = "角色实体role", required = true, dataType = "RoleVo")
	@Log("更新角色菜单")
    public ResponseBean<Boolean> updateRoleMenu(@RequestBody Role role) {
        boolean success = false;
        String menuIds = role.getMenuIds();
        if (role.getId() != null) {
            role = roleService.get(role);
            // 保存角色菜单关系
            if (role != null && StringUtils.isNotBlank(menuIds))
                success = roleMenuService.saveRoleMenus(role.getId(), Stream.of(menuIds.split(",")).map(Long::parseLong).collect(Collectors.toList())) > 0;
        }
        return new ResponseBean<>(success);
    }

    /**
     * 创建角色
     *
     * @param role role
     * @return ResponseBean
     * @author tangyi
     * @date 2018/9/14 18:23
     */
    @PostMapping
    @ApiOperation(value = "创建角色", notes = "创建角色")
    @ApiImplicitParam(name = "role", value = "角色实体role", required = true, dataType = "RoleVo")
	@Log("新增角色")
    public ResponseBean<Boolean> role(@RequestBody @Valid Role role) {
        role.setCommonValue();
        return new ResponseBean<>(roleService.insert(role) > 0);
    }

    /**
     * 根据id删除角色
     *
     * @param id id
     * @return RoleVo
     * @author tangyi
     * @date 2018/9/14 18:24
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除角色", notes = "根据ID删除角色")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, paramType = "path")
	@Log("删除角色")
    public ResponseBean<Boolean> deleteRole(@PathVariable Long id) {
        Role role = new Role();
        role.setId(id);
        role.setNewRecord(false);
        role.setCommonValue();
        return new ResponseBean<>(roleService.delete(roleService.get(role)) > 0);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/4 10:00
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除角色", notes = "根据角色id批量删除角色")
    @ApiImplicitParam(name = "ids", value = "角色ID", dataType = "Long")
	@Log("批量删除角色")
    public ResponseBean<Boolean> deleteAllRoles(@RequestBody Long[] ids) {
        boolean success = false;
        try {
            if (ArrayUtils.isNotEmpty(ids))
                success = roleService.deleteAll(ids) > 0;
        } catch (Exception e) {
            log.error("Delete role failed", e);
        }
        return new ResponseBean<>(success);
    }
}
