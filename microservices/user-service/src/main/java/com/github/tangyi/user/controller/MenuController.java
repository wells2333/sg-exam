package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.MenuDto;
import com.github.tangyi.api.user.module.Menu;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.user.excel.listener.MenuImportListener;
import com.github.tangyi.user.excel.model.MenuExcelModel;
import com.github.tangyi.user.service.MenuService;
import com.github.tangyi.user.utils.MenuUtil;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜单controller
 *
 * @author tangyi
 * @date 2018/8/26 22:48
 */
@Slf4j
@AllArgsConstructor
@Api("菜单信息管理")
@RestController
@RequestMapping("/v1/menu")
public class MenuController extends BaseController {

    private final MenuService menuService;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @return 当前用户的树形菜单
     */
    @GetMapping(value = "userMenu")
    @ApiOperation(value = "获取当前用户的菜单列表")
    public List<MenuDto> userMenu() {
        return menuService.findUserMenu();
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单集合
     */
    @GetMapping(value = "menus")
    @ApiOperation(value = "获取树形菜单列表")
    public List<MenuDto> menus() {
        return menuService.menus();
    }

    /**
     * 新增菜单
     *
     * @param menu menu
     * @return ResponseBean
     * @author tangyi
     * @date 2018/8/27 16:12
     */
    @PostMapping
    @ApiOperation(value = "创建菜单", notes = "创建菜单")
    @ApiImplicitParam(name = "menu", value = "角色实体menu", required = true, dataType = "Menu")
	@Log("新增菜单")
    public ResponseBean<Boolean> addMenu(@RequestBody @Valid Menu menu) {
        menu.setCommonValue();
        return new ResponseBean<>(menuService.insert(menu) > 0);
    }

    /**
     * 更新菜单
     *
     * @param menu menu
     * @return ResponseBean
     * @author tangyi
     * @date 2018/10/24 16:34
     */
    @PutMapping
    @ApiOperation(value = "更新菜单信息", notes = "根据菜单id更新菜单的基本信息")
    @ApiImplicitParam(name = "menu", value = "角色实体menu", required = true, dataType = "Menu")
	@Log("更新菜单")
    public ResponseBean<Boolean> updateMenu(@RequestBody @Valid Menu menu) {
        menu.setCommonValue();
        return new ResponseBean<>(menuService.update(menu) > 0);
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/8/27 16:19
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除菜单", notes = "根据ID删除菜单")
    @ApiImplicitParam(name = "id", value = "菜单ID", required = true, paramType = "path")
	@Log("删除菜单")
    public ResponseBean<Boolean> deleteMenu(@PathVariable Long id) {
        Menu menu = new Menu();
        menu.setId(id);
        return new ResponseBean<>(menuService.delete(menu) > 0);
    }

    /**
     * 根据id查询菜单
     *
     * @param id
     * @return Menu
     * @author tangyi
     * @date 2018/8/27 16:11
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取菜单信息", notes = "根据菜单id获取菜单详细信息")
    @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "Long", paramType = "path")
    public Menu menu(@PathVariable Long id) {
        return menuService.get(id);
    }

    /**
     * 获取菜单分页列表
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param menu     menu
     * @return PageInfo
     * @author tangyi
     * @date 2018/8/26 23:17
     */
    @GetMapping("menuList")
    @ApiOperation(value = "获取菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "Menu", value = "菜单信息", dataType = "Menu")})
    public PageInfo<Menu> menuList(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
            Menu menu) {
        // 租户标识过滤条件
        menu.setTenantCode(SysUtil.getTenantCode());
        return menuService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), menu);
    }

    /**
     * 根据角色查找菜单
     *
     * @param role       角色标识
     * @param tenantCode 租户标识
     * @return ResponseBean
     * @author tangyi
     * @date 2018/8/27 15:58
     */
    @GetMapping("anonymousUser/findMenuByRole/{role}")
    @ApiOperation(value = "根据角色查找菜单", notes = "根据角色id获取角色菜单")
    @ApiImplicitParam(name = "role", value = "角色名称", required = true, dataType = "String", paramType = "path")
    public ResponseBean<List<Menu>> findMenuByRole(@PathVariable String role, @RequestParam @NotBlank String tenantCode) {
        return new ResponseBean<>(menuService.findMenuByRole(role, tenantCode));
    }

    /**
     * 查询所有菜单
     *
     * @param tenantCode 租户标识
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/26 11:50
     */
    @GetMapping("anonymousUser/findAllMenu")
    @ApiOperation(value = "查询所有菜单", notes = "查询所有菜单")
    public ResponseBean<List<Menu>> findAllMenu(@RequestParam @NotBlank String tenantCode) {
        Menu menu = new Menu();
        menu.setTenantCode(tenantCode);
        menu.setApplicationCode(SysUtil.getSysCode());
        return new ResponseBean<>(menuService.findAllList(menu));
    }

    /**
     * 根据角色查找菜单
     *
     * @param roleCode 角色code
     * @return 属性集合
     */
    @GetMapping("roleTree/{roleCode}")
    @ApiOperation(value = "根据角色查找菜单", notes = "根据角色code获取角色菜单")
    @ApiImplicitParam(name = "roleCode", value = "角色code", required = true, dataType = "String", paramType = "path")
    public List<String> roleTree(@PathVariable String roleCode) {
        // 根据角色查找菜单
        Stream<Menu> menuStream = menuService.findMenuByRole(roleCode, SysUtil.getTenantCode()).stream();
        // 获取菜单ID
        if (Optional.ofNullable(menuStream).isPresent())
            return menuStream.map(menu -> menu.getId().toString()).collect(Collectors.toList());
        return new ArrayList<>();
    }

    /**
     * 导出菜单
     *
     * @param ids ids
     * @author tangyi
     * @date 2018/11/28 12:46
     */
    @PostMapping("export")
    @ApiOperation(value = "导出菜单", notes = "根据菜单id导出菜单")
    @ApiImplicitParam(name = "ids", value = "菜单ID", required = true, dataType = "Long")
	@Log("导出菜单")
    public void exportMenu(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
        String tenantCode = SysUtil.getTenantCode();
        try {
            List<Menu> menus;
            // 导出当前租户下的所有菜单
            if (ArrayUtils.isEmpty(ids)) {
                Menu menu = new Menu();
                menu.setTenantCode(tenantCode);
                menus = menuService.findList(menu);
            } else {    // 导出选中
                menus = menuService.findListById(ids);
            }
			ExcelToolUtil.writeExcel(request, response, MenuUtil.convertToExcelModel(menus), MenuExcelModel.class);
        } catch (Exception e) {
            log.error("Export menu data failed", e);
        }
    }

    /**
     * 导入数据
     *
     * @param file file
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/28 12:51
     */
    @PostMapping("import")
    @ApiOperation(value = "导入菜单", notes = "导入菜单")
	@Log("导入菜单")
    public ResponseBean<Boolean> importMenu(@ApiParam(value = "要上传的文件", required = true) MultipartFile file) {
        try {
            log.debug("Start import menu data");
            return new ResponseBean<>(ExcelToolUtil.readExcel(file.getInputStream(), MenuExcelModel.class, new MenuImportListener(menuService)));
        } catch (Exception e) {
            log.error("Import menu data failed", e);
        }
        return new ResponseBean<>(Boolean.FALSE);
    }
}
