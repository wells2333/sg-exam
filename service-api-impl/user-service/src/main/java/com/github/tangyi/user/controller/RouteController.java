package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.model.Route;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.user.service.RouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 路由controller
 *
 * @author tangyi
 * @date 2019/4/2 15:03
 */
@Slf4j
@AllArgsConstructor
@Api("网关路由信息管理")
@RestController
@RequestMapping("/v1/route")
public class RouteController extends BaseController {

    private final RouteService routeService;

    private final AmqpTemplate amqpTemplate;


    /**
     * 根据id获取路由
     *
     * @param id id
     * @return Route
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取路由信息", notes = "根据路由id获取路由详细信息")
    @ApiImplicitParam(name = "id", value = "路由ID", required = true, dataType = "String", paramType = "path")
    public Route get(@PathVariable String id) {
        try {
            return routeService.get(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Route();
    }

    /**
     * 路由分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param route    route
     * @return PageInfo
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @RequestMapping("routeList")
    @ApiOperation(value = "获取路由列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "route", value = "路由信息", dataType = "Route")
    })
    public PageInfo<Route> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                    Route route) {
        return routeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), route);
    }

    /**
     * 修改路由
     *
     * @param route route
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:route:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新路由信息", notes = "根据路由id更新路由的基本信息")
    @ApiImplicitParam(name = "route", value = "路由实体route", required = true, dataType = "Route")
    @Log("修改路由")
    public ResponseBean<Boolean> updateRoute(@RequestBody Route route) {
        route.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        // 更新路由
        if (routeService.update(route) > 0) {
            // 发送消息
            if (Integer.parseInt(route.getStatus()) == CommonConstant.DEL_FLAG_DEL) {
                amqpTemplate.convertAndSend(MqConstant.DEL_GATEWAY_ROUTE_QUEUE, route);
            } else {
                amqpTemplate.convertAndSend(MqConstant.EDIT_GATEWAY_ROUTE_QUEUE, route);
            }
        }
        return new ResponseBean<>(Boolean.TRUE);
    }

    /**
     * 创建路由
     *
     * @param route route
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:route:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "创建路由", notes = "创建路由")
    @ApiImplicitParam(name = "route", value = "路由实体route", required = true, dataType = "Route")
    @Log("新增路由")
    public ResponseBean<Boolean> add(@RequestBody Route route) {
        route.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        if (routeService.insert(route) > 0 && Integer.parseInt(route.getStatus()) == CommonConstant.DEL_FLAG_NORMAL) {
            // 发送消息
            amqpTemplate.convertAndSend(MqConstant.EDIT_GATEWAY_ROUTE_QUEUE, route);
        }
        return new ResponseBean<>(Boolean.TRUE);
    }

    /**
     * 根据id删除路由
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:route:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除路由", notes = "根据ID删除路由")
    @ApiImplicitParam(name = "id", value = "路由ID", required = true, paramType = "path")
    @Log("删除路由")
    public ResponseBean<Boolean> delete(@PathVariable String id) {
        Route route = new Route();
        route.setId(id);
        route = routeService.get(route);
        route.setNewRecord(false);
        route.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        if (routeService.delete(route) > 0) {
            // 发送消息
            amqpTemplate.convertAndSend(MqConstant.DEL_GATEWAY_ROUTE_QUEUE, Collections.singletonList(route));
        }
        return new ResponseBean<>(Boolean.TRUE);
    }

    /**
     * 批量删除
     *
     * @param route route
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @PostMapping("deleteAll")
    @PreAuthorize("hasAuthority('sys:route:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "批量删除路由", notes = "根据路由id批量删除路由")
    @ApiImplicitParam(name = "route", value = "路由信息", dataType = "Route")
    @Log("批量删除路由")
    public ResponseBean<Boolean> deleteAll(@RequestBody Route route) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(route.getIdString())) {
                // 先获取路由列表
                List<Route> routeList = routeService.findListById(route);
                // 删除
                success = routeService.deleteAll(route.getIdString().split(",")) > 0;
                if (success && CollectionUtils.isNotEmpty(routeList)) {
                    // 发送消息
                    amqpTemplate.convertAndSend(MqConstant.DEL_GATEWAY_ROUTE_QUEUE, route);
                }
            }
        } catch (Exception e) {
            log.error("删除路由失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
