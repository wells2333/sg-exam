package com.github.tangyi.gateway.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.gateway.module.Route;
import com.github.tangyi.gateway.service.RouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 路由controller
 * TODO：增加security认证
 *
 * @author tangyi
 * @date 2019/4/2 15:03
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/route/v1/route")
public class RouteController extends BaseController {

    private final RouteService routeService;

    /**
     * 根据id获取路由
     * @param id id
     * @return Route
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @GetMapping("/{id}")
    public Route get(@PathVariable Long id) {
        try {
            return routeService.get(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 路由分页查询
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param route    route
     * @return PageInfo
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @GetMapping("routeList")
    public PageInfo<Route> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                    Route route) {
        return routeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), route);
    }

    /**
     * 修改路由
     * @param route route
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @PutMapping
    public ResponseBean<Boolean> updateRoute(@RequestBody @Valid Route route) {
        try {
            route.setCommonValue("", "", "");
			return new ResponseBean<>(routeService.update(route) > 0);
		} catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 创建路由
     * @param route route
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @PostMapping
    public ResponseBean<Boolean> add(@RequestBody @Valid Route route) {
        try {
            return new ResponseBean<>(routeService.insert(route) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 根据id删除路由
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @DeleteMapping("/{id}")
    public ResponseBean<Boolean> delete(@PathVariable Long id) {
        try {
			return new ResponseBean<>(routeService.delete(id) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 批量删除
     * @param ids ids
     * @return ResponseBean
     * @author tangyi
     * @date 2019/4/2 15:09
     */
    @PostMapping("deleteAll")
    public ResponseBean<Boolean> deleteAll(@RequestBody Long[] ids) {
		try {
			return new ResponseBean<>( routeService.deleteAll(ids) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }


    /**
     * 刷新路由
     *
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/07 12:32
     */
    @GetMapping("refresh")
    public ResponseBean<Boolean> refresh() {
        try {
            routeService.refresh();
            return new ResponseBean<>(true);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonException(e.getMessage());
        }
    }
}
