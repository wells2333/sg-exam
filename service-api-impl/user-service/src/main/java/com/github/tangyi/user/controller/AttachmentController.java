package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.Servlets;
import com.github.tangyi.common.core.vo.AttachmentVo;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.user.api.module.Attachment;
import com.github.tangyi.user.service.AttachmentService;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
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
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/{id}")
    public ResponseBean<Attachment> attachment(@PathVariable String id) {
        Attachment attachment = new Attachment();
        if (StringUtils.isNotBlank(id)) {
            attachment.setId(id);
            attachment = attachmentService.get(attachment);
        }
        return new ResponseBean<>(attachment);
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
    @RequestMapping("attachmentList")
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
    @RequestMapping("upload")
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
    @RequestMapping("download")
    @ApiOperation(value = "下载附件", notes = "根据ID下载附件")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "String")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(id))
            throw new IllegalArgumentException("附件ID不能为空！");
        Attachment attachment = new Attachment();
        attachment.setId(id);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = attachmentService.download(attachment);
            outputStream = response.getOutputStream();  // 输出流
            response.setContentType("application/zip");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=10");
            // IE之外的浏览器使用编码输出名称
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, attachment.getAttachName()));
            response.setContentLength(inputStream.available());
            FileCopyUtils.copy(inputStream, outputStream);  // 下载文件
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
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
    public ResponseBean<Boolean> delete(@PathVariable String id) {
        if (StringUtils.isBlank(id))
            throw new IllegalArgumentException("附件ID不能为空！");
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
     * @param attachment attachment
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/4 10:01
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除附件", notes = "根据附件id批量删除附件")
    @ApiImplicitParam(name = "attachment", value = "附件信息", dataType = "Attachment")
    @Log("批量删除附件")
    public ResponseBean<Boolean> deleteAllAttachments(@RequestBody Attachment attachment) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(attachment.getIdString()))
                success = attachmentService.deleteAll(attachment.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除附件失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据附件ID批量查询
     *
     * @param attachmentVo attachmentVo
     * @return ResponseBean
     * @author tangyi
     * @date 2019/01/01 22:16
     */
    @RequestMapping(value = "findById", method = RequestMethod.POST)
    @ApiOperation(value = "批量查询附件信息", notes = "根据附件ID批量查询附件信息")
    @ApiImplicitParam(name = "attachmentVo", value = "附件信息", dataType = "AttachmentVo")
    public ResponseBean<List<AttachmentVo>> findById(@RequestBody AttachmentVo attachmentVo) {
        ResponseBean<List<AttachmentVo>> returnT = null;
        Attachment attachment = new Attachment();
        attachment.setIds(attachmentVo.getIds());
        List<Attachment> attachmentList = attachmentService.findListById(attachment);
        if (CollectionUtils.isNotEmpty(attachmentList)) {
            // 流处理转换成AttachmentVo
            List<AttachmentVo> attachmentVoList = attachmentList.stream().map(tempAttachment -> {
                AttachmentVo tempAttachmentVo = new AttachmentVo();
                BeanUtils.copyProperties(tempAttachment, tempAttachmentVo);
                return tempAttachmentVo;
            }).collect(Collectors.toList());
            returnT = new ResponseBean<>(attachmentVoList);
        }
        return returnT;
    }
}
