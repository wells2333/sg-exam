package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.vo.AttachmentVo;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.dto.KnowledgeDto;
import com.github.tangyi.exam.api.module.Knowledge;
import com.github.tangyi.exam.service.KnowledgeService;
import com.github.tangyi.user.api.feign.UserServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 知识库controller
 *
 * @author tangyi
 * @date 2019/1/1 15:11
 */
@Slf4j
@AllArgsConstructor
@Api("知识库信息管理")
@RestController
@RequestMapping("/v1/knowledge")
public class KnowledgeController extends BaseController {

    private final KnowledgeService knowledgeService;

    private final UserServiceClient userServiceClient;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/1/1 15:15
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取知识信息", notes = "根据知识id获取知识详细信息")
    @ApiImplicitParam(name = "id", value = "知识ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Knowledge> knowledge(@PathVariable String id) {
        Knowledge knowledge = new Knowledge();
        if (StringUtils.isNotBlank(id)) {
            knowledge.setId(id);
            knowledge = knowledgeService.get(knowledge);
        }
        return new ResponseBean<>(knowledge);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param sort      sort
     * @param order     order
     * @param knowledge knowledge
     * @return PageInfo
     * @author tangyi
     * @date 2019/1/1 15:15
     */
    @RequestMapping("knowledgeList")
    @ApiOperation(value = "获取知识列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "knowledge", value = "知识信息", dataType = "Knowledge")
    })
    public PageInfo<KnowledgeDto> knowledgeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                Knowledge knowledge) {
        // 查询知识
        PageInfo<Knowledge> knowledgePageInfo = knowledgeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), knowledge);
        PageInfo<KnowledgeDto> knowledgeDtoPageInfo = new PageInfo<>();
        List<KnowledgeDto> knowledgeDtoList = new ArrayList<>();
        // 查询附件
        Set<String> attachmentIdSet = new HashSet<>();
        knowledgePageInfo.getList().forEach(tempKnowledge -> {
            attachmentIdSet.add(tempKnowledge.getAttachmentId());
        });
        AttachmentVo attachmentVo = new AttachmentVo();
        attachmentVo.setIds(attachmentIdSet.toArray(new String[0]));
        // 根据附件ID查询附件
        ResponseBean<List<AttachmentVo>> returnT = userServiceClient.findAttachmentById(attachmentVo);
        knowledgePageInfo.getList().stream()
                // 转成Dto
                .map(tempKnowledge -> {
                    KnowledgeDto knowledgeDto = new KnowledgeDto();
                    BeanUtils.copyProperties(tempKnowledge, knowledgeDto);
                    return knowledgeDto;
                })
                // 遍历
                .forEach(tempKnowledgeDto -> {
                    if (returnT != null && CollectionUtils.isNotEmpty(returnT.getData())) {
                        AttachmentVo tempKnowledgeDtoAttachmentVo = returnT.getData().stream()
                                // 根据ID过滤
                                .filter(tempAttachmentVo -> tempAttachmentVo.getId().equals(tempKnowledgeDto.getAttachmentId()))
                                // 匹配第一个
                                .findFirst().orElse(null);
                        // 设置附件名称、附件大小
                        if (tempKnowledgeDtoAttachmentVo != null) {
                            tempKnowledgeDto.setAttachName(tempKnowledgeDtoAttachmentVo.getAttachName());
                            tempKnowledgeDto.setAttachSize(tempKnowledgeDtoAttachmentVo.getAttachSize());
                        }
                    }
                    knowledgeDtoList.add(tempKnowledgeDto);
                });
        knowledgeDtoPageInfo.setList(knowledgeDtoList);
        knowledgeDtoPageInfo.setTotal(knowledgePageInfo.getTotal());
        knowledgeDtoPageInfo.setPageNum(knowledgePageInfo.getPageNum());
        knowledgeDtoPageInfo.setPageSize(knowledgePageInfo.getPageSize());
        return knowledgeDtoPageInfo;
    }

    /**
     * 创建
     *
     * @param knowledge knowledge
     * @return ResponseBean
     * @author tangyi
     * @date 2019/1/1 15:15
     */
    @PostMapping
    @ApiOperation(value = "创建知识", notes = "创建知识")
    @ApiImplicitParam(name = "knowledge", value = "知识实体knowledge", required = true, dataType = "Knowledge")
    @Log("新增知识")
    public ResponseBean<Boolean> addKnowledge(@RequestBody Knowledge knowledge) {
        knowledge.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(knowledgeService.insert(knowledge) > 0);
    }

    /**
     * 更新
     *
     * @param knowledge knowledge
     * @return ResponseBean
     * @author tangyi
     * @date 2019/1/1 15:15
     */
    @PutMapping
    @ApiOperation(value = "更新知识信息", notes = "根据知识id更新知识的基本信息")
    @ApiImplicitParam(name = "knowledge", value = "知识实体knowledge", required = true, dataType = "Knowledge")
    @Log("更新知识")
    public ResponseBean<Boolean> updateKnowledge(@RequestBody Knowledge knowledge) {
        knowledge.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(knowledgeService.update(knowledge) > 0);
    }

    /**
     * 删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/1/1 15:15
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除知识", notes = "根据ID删除知识")
    @ApiImplicitParam(name = "id", value = "知识ID", required = true, paramType = "path")
    @Log("删除知识")
    public ResponseBean<Boolean> deleteKnowledge(@PathVariable String id) {
        boolean success = false;
        try {
            Knowledge knowledge = new Knowledge();
            knowledge.setId(id);
            knowledge = knowledgeService.get(knowledge);
            if (knowledge != null) {
                knowledge.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                success = knowledgeService.delete(knowledge) > 0;
            }
            // 删除附件
            if (knowledge != null && StringUtils.isNotBlank(knowledge.getAttachmentId()))
                success = userServiceClient.deleteAttachment(knowledge.getAttachmentId()).getData();
        } catch (Exception e) {
            log.error("删除知识失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量删除
     *
     * @param knowledge knowledge
     * @return ResponseBean
     * @author tangyi
     * @date 2019/1/1 15:15
     */
    @PostMapping("/deleteAll")
    @ApiOperation(value = "批量删除知识", notes = "根据知识id批量删除知识")
    @ApiImplicitParam(name = "knowledge", value = "知识信息", dataType = "Knowledge")
    @Log("批量删除知识")
    public ResponseBean<Boolean> deleteAllKnowledge(@RequestBody Knowledge knowledge) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(knowledge.getIdString()))
                success = knowledgeService.deleteAll(knowledge.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除知识失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
