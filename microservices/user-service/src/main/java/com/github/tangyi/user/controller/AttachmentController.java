package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.module.Attachment;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.FileUtil;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.Servlets;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.AttachmentVo;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.user.service.AttachmentService;
import com.github.tangyi.user.uploader.UploadInvoker;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;
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

    private final SysProperties sysProperties;

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
    public ResponseBean<Attachment> upload(@ApiParam(value = "要上传的文件", required = true) @RequestParam("file") MultipartFile file,
                                           Attachment attachment) {
        if (!file.isEmpty()) {
            try {
                attachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                attachment.setAttachType(FileUtil.getFileNameEx(file.getOriginalFilename()));
                attachment.setAttachSize(String.valueOf(file.getSize()));
                attachment.setAttachName(file.getOriginalFilename());
                attachment.setBusiId(attachment.getId().toString());
                attachment = UploadInvoker.getInstance().upload(attachment, file.getBytes());
            } catch (Exception e) {
                log.error("upload attachment error: {}", e.getMessage(), e);
            }
        }
        return new ResponseBean<>(attachment);
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
    public void download(HttpServletRequest request, HttpServletResponse response, @NotBlank Long id) {
        try {
			Attachment attachment = new Attachment();
			attachment.setId(id);
			attachment = attachmentService.get(attachment);
			if (attachment == null)
				throw new CommonException("Attachment does not exist");
			InputStream inputStream = UploadInvoker.getInstance().download(attachment);
			if (inputStream == null) {
			    log.info("attachment is not exists");
			    return;
            }
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=10");
            // IE之外的浏览器使用编码输出名称
            String contentDisposition = "";
            String httpUserAgent = request.getHeader("User-Agent");
            if (StringUtils.isNotEmpty(httpUserAgent)) {
                httpUserAgent = httpUserAgent.toLowerCase();
                String fileName = attachment.getAttachName();
                contentDisposition = httpUserAgent.contains("wps") ? "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") : Servlets.getDownName(request, fileName);
            }
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            response.setContentLength(inputStream.available());
            FileCopyUtils.copy(inputStream, outputStream);
            log.info("download {} success", attachment.getAttachName());
		} catch (Exception e) {
        	log.error("Download attachment failed: {}", e.getMessage(), e);
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
    public ResponseBean<Boolean> delete(@PathVariable Long id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        attachment = attachmentService.get(attachment);
        boolean success = false;
        if (attachment != null)
            success = UploadInvoker.getInstance().delete(attachment);
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
    public ResponseBean<Boolean> deleteAllAttachments(@RequestBody Long[] ids) {
        boolean success = false;
        try {
            if (ArrayUtils.isNotEmpty(ids))
                success = UploadInvoker.getInstance().deleteAll(ids);
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
     * 是否支持预览
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/19 15:47
     */
    @GetMapping("/{id}/canPreview")
    @ApiOperation(value = "判断附件是否支持预览", notes = "根据附件ID判断附件是否支持预览")
    @ApiImplicitParam(name = "id", value = "附件id", required = true, dataType = "Long", paramType = "path")
    public ResponseBean<Boolean> canPreview(@PathVariable Long id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        attachment = attachmentService.get(attachment);
        return new ResponseBean<>(attachment != null && ArrayUtils.contains(sysProperties.getCanPreview().split(","), attachment.getAttachType()));
    }

    /**
     * 预览附件
     *
     * @param response response
     * @param id id
     * @author tangyi
     * @date 2019/06/19 15:47
     */
    @GetMapping("/preview")
    @ApiOperation(value = "预览附件", notes = "根据附件ID预览附件")
    @ApiImplicitParam(name = "id", value = "附件id", required = true, dataType = "Long")
    public void preview(HttpServletResponse response, @RequestParam Long id) throws Exception {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        attachment = attachmentService.get(attachment);
        FileInputStream stream = new FileInputStream(new File(attachment.getFastFileId() + File.separator + attachment.getAttachName()));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while ((n = stream.read(b)) != -1) {
            out.write(b, 0, n);
        }
        response.setHeader("Content-Type", "image/png");
        response.getOutputStream().write(out.toByteArray());
        response.getOutputStream().flush();
        out.close();
        stream.close();
    }
}
