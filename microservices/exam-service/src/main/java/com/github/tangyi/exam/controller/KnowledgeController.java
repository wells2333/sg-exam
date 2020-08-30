package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.KnowledgeDto;
import com.github.tangyi.api.exam.module.Knowledge;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.AttachmentVo;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.exam.service.KnowledgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ApiImplicitParam(name = "id", value = "知识ID", required = true, dataType = "Long", paramType = "path")
    public ResponseBean<Knowledge> knowledge(@PathVariable Long id) {
        return new ResponseBean<>(knowledgeService.get(id));
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
    @GetMapping("knowledgeList")
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
        knowledge.setTenantCode(SysUtil.getTenantCode());
        // 查询知识
        PageInfo<Knowledge> knowledgePageInfo = knowledgeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), knowledge);
        PageInfo<KnowledgeDto> knowledgeDtoPageInfo = new PageInfo<>();
        List<KnowledgeDto> knowledgeDtoList = new ArrayList<>();
        // 查询附件
        Set<Long> attachmentIdSet = new HashSet<>();
        knowledgePageInfo.getList().forEach(tempKnowledge -> attachmentIdSet.add(tempKnowledge.getAttachmentId()));
        // 根据附件ID查询附件
        //ResponseBean<List<AttachmentVo>> returnT = userServiceClient.findAttachmentById(attachmentIdSet.toArray(new Long[0]));
        ResponseBean<List<AttachmentVo>> returnT = null;
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
    public ResponseBean<Boolean> addKnowledge(@RequestBody @Valid Knowledge knowledge) {
        knowledge.setCommonValue();
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
    public ResponseBean<Boolean> updateKnowledge(@RequestBody @Valid Knowledge knowledge) {
        knowledge.setCommonValue();
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
    public ResponseBean<Boolean> deleteKnowledge(@PathVariable Long id) {
        boolean success = false;
		Knowledge knowledge = knowledgeService.get(id);
		if (knowledge != null) {
			knowledge.setCommonValue();
			success = knowledgeService.delete(knowledge) > 0;
		}
		// 删除附件
		//if (knowledge != null && knowledge.getAttachmentId() != null)
			//success = userServiceClient.deleteAttachment(knowledge.getAttachmentId()).getData();
        return new ResponseBean<>(success);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return ResponseBean
     * @author tangyi
     * @date 2019/1/1 15:15
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除知识", notes = "根据知识id批量删除知识")
    @ApiImplicitParam(name = "ids", value = "知识ID", dataType = "Long")
    public ResponseBean<Boolean> deleteAllKnowledge(@RequestBody Long[] ids) {
        boolean success = false;
		if (ArrayUtils.isNotEmpty(ids))
			success = knowledgeService.deleteAll(ids) > 0;
        return new ResponseBean<>(success);
    }
}
