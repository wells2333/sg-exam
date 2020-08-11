package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.user.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 日志controller
 *
 * @author tangyi
 * @date 2018/10/31 20:48
 */
@Slf4j
@AllArgsConstructor
@Api("日志信息管理")
@RestController
@RequestMapping("/v1/log")
public class LogController extends BaseController {

    private final LogService logService;

    /**
     * 根据id获取日志
     *
     * @param id id
     * @return Log
     * @author tangyi
     * @date 2018/9/14 18:20
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取日志信息", notes = "根据日志id获取日志详细信息")
    @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "Long", paramType = "path")
    public Log log(@PathVariable Long id) {
        try {
            return logService.get(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Log();
    }

    /**
     * 日志分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param log      log
     * @return PageInfo
     * @author tangyi
     * @date 2018/10/24 0024 22:13
     */
    @GetMapping("logList")
    @ApiOperation(value = "获取日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "log", value = "日志信息", dataType = "Log")
    })
    public PageInfo<Log> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                  @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                  @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                  @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                  Log log) {
        log.setTenantCode(SysUtil.getTenantCode());
        return logService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), log);
    }

    /**
     * 新增日志
     *
     * @param log log
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/27 23:14
     */
    @PostMapping
    @ApiOperation(value = "新增日志", notes = "新增日志")
    @ApiImplicitParam(name = "log", value = "日志实体Log", required = true, dataType = "Log")
    public ResponseBean<Boolean> addLog(@RequestBody @Valid Log log) {
        // 保存日志
        return new ResponseBean<>(logService.insert(log) > 0);
    }

    /**
     * 删除日志
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/10/31 21:27
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除日志", notes = "根据ID删除日志")
    @ApiImplicitParam(name = "id", value = "日志ID", required = true, paramType = "path")
    public ResponseBean<Boolean> delete(@PathVariable Long id) {
        Log log = new Log();
        log.setId(id);
        return new ResponseBean<>(logService.delete(log) > 0);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/4 10:12
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除日志", notes = "根据日志ids批量删除日志")
    @ApiImplicitParam(name = "ids", value = "日志ID", dataType = "Long")
    public ResponseBean<Boolean> deleteAllLog(@RequestBody Long[] ids) {
        boolean success = false;
        try {
            if (ArrayUtils.isNotEmpty(ids))
                success = logService.deleteAll(ids) > 0;
        } catch (Exception e) {
            log.error("Delete attachment failed", e);
        }
        return new ResponseBean<>(success);
    }
}
