package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.basic.vo.AttachmentVo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.utils.SysUtil;
import com.github.tangyi.user.api.module.Attachment;
import com.github.tangyi.user.service.AttachmentService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 附件信息管理
 *
 * @author tangyi
 * @date 2018/10/30 20:45
 */
@Slf4j
@AllArgsConstructor
@Api("附件信息管理")
@RestController
@RequestMapping("/v1/attachment")
public class AttachmentController extends BaseController {

    private final AttachmentService attachmentService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/01/01 19:56
     */
    @ApiOperation(value = "获取附件信息", notes = "根据附件id获取附件详细信息")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/{id}")
    public ResponseBean<Attachment> attachment(@PathVariable Long id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return new ResponseBean<>(attachmentService.get(attachment));
    }

    /**
     * 分页查询
     *
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param sort       sort
     * @param order      order
     * @param attachment attachment
     * @return PageInfo
     * @author tangyi
     * @date 2018/10/30 21:05
     */
    @GetMapping("attachmentList")
    @ApiOperation(value = "获取附件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "attachment", value = "附件信息", dataType = "Attachment")
    })
    public PageInfo<Attachment> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                         @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                         @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                         Attachment attachment) {
        attachment.setTenantCode(SysUtil.getTenantCode());
        return attachmentService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), attachment);
    }

    /**
     * 上传文件
     *
     * @param file       file
     * @param attachment attachment
     * @author tangyi
     * @date 2018/10/30 21:54
     */
    @PostMapping("upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busiType", value = "业务分类", dataType = "String"),
            @ApiImplicitParam(name = "busiId", value = "业务Id", dataType = "String"),
            @ApiImplicitParam(name = "busiModule", value = "业务模块", dataType = "String"),
    })
    @Log("上传文件")
    public ResponseBean<Attachment> upload(@ApiParam(value = "要上传的文件", required = true) @RequestParam("file") MultipartFile file,
                                           Attachment attachment) {
        if (file.isEmpty())
            return new ResponseBean<>(new Attachment());
        return new ResponseBean<>(attachmentService.upload(file, attachment));
    }

    /**
     * 下载文件
     *
     * @param id id
     * @author tangyi
     * @date 2018/10/30 22:26
     */
    @GetMapping("download")
    @ApiOperation(value = "下载附件", notes = "根据ID下载附件")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "Long")
    public ResponseBean<String> download(@NotBlank Long id) {
        String downloadUrl = "";
        try {
			Attachment attachment = new Attachment();
			attachment.setId(id);
			attachment = attachmentService.get(attachment);
			if (attachment == null)
				throw new CommonException("Attachment does not exist");
			downloadUrl = attachmentService.download(attachment);
		} catch (Exception e) {
        	log.error("Download attachment failed: {}", e.getMessage(), e);
		}
		return new ResponseBean<>(downloadUrl);
	}

    /**
     * 删除附件
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/10/30 22:44
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除附件", notes = "根据ID删除附件")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, paramType = "path")
    @Log("删除附件")
    public ResponseBean<Boolean> delete(@PathVariable Long id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        attachment = attachmentService.get(attachment);
        boolean success = false;
        if (attachment != null)
            success = attachmentService.delete(attachment) > 0;
        return new ResponseBean<>(success);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/4 10:01
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除附件", notes = "根据附件id批量删除附件")
    @ApiImplicitParam(name = "ids", value = "附件ID", dataType = "Long")
    @Log("批量删除附件")
    public ResponseBean<Boolean> deleteAllAttachments(@RequestBody Long[] ids) {
        boolean success = false;
        try {
            if (ArrayUtils.isNotEmpty(ids))
                success = attachmentService.deleteAll(ids) > 0;
        } catch (Exception e) {
            log.error("Delete attachment failed", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据附件ID批量查询
     *
     * @param ids ids
     * @return ResponseBean
     * @author tangyi
     * @date 2019/01/01 22:16
     */
    @PostMapping(value = "findById")
    @ApiOperation(value = "批量查询附件信息", notes = "根据附件ID批量查询附件信息")
    @ApiImplicitParam(name = "ids", value = "附件ID", dataType = "Long")
    public ResponseBean<List<AttachmentVo>> findById(@RequestBody Long[] ids) {
        ResponseBean<List<AttachmentVo>> returnT = null;
        List<Attachment> attachmentList = attachmentService.findListById(ids);
        if (CollectionUtils.isNotEmpty(attachmentList)) {
            List<AttachmentVo> attachmentVoList = attachmentList.stream().map(tempAttachment -> {
                AttachmentVo tempAttachmentVo = new AttachmentVo();
                BeanUtils.copyProperties(tempAttachment, tempAttachmentVo);
                return tempAttachmentVo;
            }).collect(Collectors.toList());
            returnT = new ResponseBean<>(attachmentVoList);
        }
        return returnT;
    }

    /**
     * 获取预览地址
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/19 15:47
     */
    @GetMapping("/{id}/preview")
    @ApiOperation(value = "获取预览地址", notes = "根据附件ID获取预览地址")
    @ApiImplicitParam(name = "id", value = "附件id", required = true, dataType = "Long", paramType = "path")
    public ResponseBean<String> getPreviewUrl(@PathVariable Long id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return new ResponseBean<>(attachmentService.getPreviewUrl(attachment));
    }
}
