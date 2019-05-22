package com.github.tangyi.auth.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.auth.api.module.OauthClientDetails;
import com.github.tangyi.auth.service.OauthClientDetailsService;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Oauth2客户端信息管理
 *
 * @author tangyi
 * @date 2019/3/30 16:49
 */
@Slf4j
@AllArgsConstructor
@Api("Oauth2客户端信息管理")
@RestController
@RequestMapping("/v1/client")
public class OauthClientDetailsController extends BaseController {

    private final OauthClientDetailsService oauthClientDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/30 16:53
     */
    @ApiOperation(value = "获取客户端信息", notes = "根据客户端id获取客户端详细信息")
    @ApiImplicitParam(name = "id", value = "客户端ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/{id}")
    public ResponseBean<OauthClientDetails> oauthClient(@PathVariable String id) {
        OauthClientDetails oauthClientDetails = new OauthClientDetails();
        if (StringUtils.isNotBlank(id)) {
            oauthClientDetails.setId(id);
            oauthClientDetails = oauthClientDetailsService.get(oauthClientDetails);
        }
        return new ResponseBean<>(oauthClientDetails);
    }

    /**
     * 分页查询
     *
     * @param pageNum            pageNum
     * @param pageSize           pageSize
     * @param sort               sort
     * @param order              order
     * @param oauthClientDetails oauthClientDetails
     * @return PageInfo
     * @author tangyi
     * @date 2019/03/30 16:54
     */
    @RequestMapping("clientList")
    @ApiOperation(value = "获取客户端列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "attachment", value = "客户端信息", dataType = "OauthClient")
    })
    public PageInfo<OauthClientDetails> oauthClientList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                        @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                        @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                        OauthClientDetails oauthClientDetails) {
        return oauthClientDetailsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), oauthClientDetails);
    }

    /**
     * 查询客户端列表
     *
     * @param oauthClientDetails oauthClientDetails
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/30 23:17
     */
    @RequestMapping("clients")
    @ApiOperation(value = "查询客户端列表", notes = "查询客户端列表")
    @ApiImplicitParam(name = "oauthClient", value = "客户端实体oauthClient", required = true, dataType = "OauthClientDetails")
    public ResponseBean<List<OauthClientDetails>> findOauthClientList(@RequestBody OauthClientDetails oauthClientDetails) {
        return new ResponseBean<>(oauthClientDetailsService.findList(oauthClientDetails));
    }

    /**
     * 创建客户端
     *
     * @param oauthClientDetails oauthClientDetails
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/30 16:57
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:client:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "创建客户端", notes = "创建客户端")
    @ApiImplicitParam(name = "oauthClientDetails", value = "客户端实体oauthClientDetails", required = true, dataType = "OauthClientDetails")
    @Log("新增客户端")
    public ResponseBean<Boolean> oauthClient(@RequestBody OauthClientDetails oauthClientDetails) {
        oauthClientDetails.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        // 加密密钥
        oauthClientDetails.setClientSecret(bCryptPasswordEncoder.encode(oauthClientDetails.getClientSecretPlainText()));
        return new ResponseBean<>(oauthClientDetailsService.insert(oauthClientDetails) > 0);
    }

    /**
     * 修改客户端
     *
     * @param oauthClientDetails oauthClientDetails
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/30 16:56
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:client:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新客户端信息", notes = "根据客户端id更新客户端的基本信息")
    @ApiImplicitParam(name = "oauthClientDetails", value = "客户端实体oauthClientDetails", required = true, dataType = "OauthClientDetails")
    @Log("修改客户端")
    public ResponseBean<Boolean> updateOauthClient(@RequestBody OauthClientDetails oauthClientDetails) {
        OauthClientDetails tempOauthClientDetails = oauthClientDetailsService.get(oauthClientDetails);
        // 有调整过明文则重新加密密钥
        if (tempOauthClientDetails != null && !tempOauthClientDetails.getClientSecretPlainText().equals(oauthClientDetails.getClientSecretPlainText()))
            oauthClientDetails.setClientSecret(bCryptPasswordEncoder.encode(oauthClientDetails.getClientSecretPlainText()));
        oauthClientDetails.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(oauthClientDetailsService.update(oauthClientDetails) > 0);
    }

    /**
     * 根据id删除客户端
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/30 16:59
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:client:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除客户端", notes = "根据ID删除客户端")
    @ApiImplicitParam(name = "id", value = "客户端ID", required = true, paramType = "path")
    @Log("删除客户端")
    public ResponseBean<Boolean> deleteOauthClient(@PathVariable String id) {
        OauthClientDetails oauthClientDetails = new OauthClientDetails();
        oauthClientDetails.setId(id);
        oauthClientDetails.setNewRecord(false);
        oauthClientDetails.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(oauthClientDetailsService.delete(oauthClientDetails) > 0);
    }

    /**
     * 批量删除
     *
     * @param oauthClientDetails oauthClientDetails
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/30 17:01
     */
    @PostMapping("/deleteAll")
    @PreAuthorize("hasAuthority('sys:client:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "批量删除客户端", notes = "根据客户端id批量删除客户端")
    @ApiImplicitParam(name = "oauthClientDetails", value = "客户端信息", dataType = "OauthClientDetails")
    @Log("批量删除客户端")
    public ResponseBean<Boolean> deleteAllOauthClient(@RequestBody OauthClientDetails oauthClientDetails) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(oauthClientDetails.getIdString()))
                success = oauthClientDetailsService.deleteAll(oauthClientDetails.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除客户端失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
