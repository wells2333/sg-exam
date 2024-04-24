/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.user.controller.attach;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.ChunkUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.AttachmentVo;
import com.github.tangyi.user.service.attach.AttachGroupService;
import com.github.tangyi.user.service.attach.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Tag(name = "附件信息管理")
@RestController
@RequestMapping("/v1/attachment")
public class AttachmentController extends BaseController {

    private final AttachmentService attachmentService;
    private final AttachGroupService groupService;
    private final SysProperties sysProperties;
    private final AttachmentManager attachmentManager;

    @Operation(summary = "获取附件信息")
    @GetMapping("/{id}")
    public R<Attachment> get(@PathVariable Long id) {
        return R.success(attachmentService.get(id));
    }

    @GetMapping("attachmentList")
    @Operation(summary = "获取附件列表")
    public R<PageInfo<Attachment>> list(@RequestParam Map<String, Object> condition,
                                        @RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
                                        @RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
        return R.success(attachmentService.attachmentList(condition, pageNum, pageSize));
    }

    @PostMapping("prepareUploadChunks")
    @Operation(summary = "准备多分片文件上传")
    public R<Attachment> prepareUploadChunks(@RequestParam(required = false) String groupCode,
                                             @RequestBody Attachment attachment) {
        AttachGroup group = getAttachGroup(groupCode);
        return R.success(attachmentManager.prepareUploadChunks(group, attachment));
    }

    @PostMapping("uploadChunk")
    @Operation(summary = "上传文件分片")
    public R<Boolean> uploadChunk(
            @Parameter(description = "要上传的文件", required = true) @RequestParam("file") MultipartFile file,
            @RequestParam("hash") String hash, @RequestParam("index") Integer index,
            @RequestParam(value = "uploadId", required = false) String uploadId) throws IOException {
        ChunkUploadContext context = new ChunkUploadContext();
        context.setMultipartFile(file);
        context.setUploadId(uploadId);
        context.hash(hash).index(index);
        context.setUser(SysUtil.getUser());
        context.setTenantCode(SysUtil.getTenantCode());
        return R.success(attachmentManager.uploadChunk(context));
    }

    @PostMapping("mergeChunks")
    @Operation(summary = "合并文件分片")
    public R<Attachment> mergeChunks(@RequestParam String hash) {
        return R.success(attachmentManager.mergeChunks(hash));
    }

    @PostMapping("upload")
    @Operation(summary = "上传文件")
    @SgLog(value = "上传文件", operationType = OperationType.UPLOAD)
    public R<Attachment> upload(
            @Parameter(description = "要上传的文件", required = true) @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String groupCode) throws IOException {
        AttachGroup group = getAttachGroup(groupCode);
        return R.success(attachmentManager.upload(MultipartFileUploadContext.of(group, file)));
    }

    @GetMapping("getDownloadUrl")
    @Operation(summary = "获取下载链接")
    @SgLog(value = "获取下载链接", operationType = OperationType.PREVIEW)
    public R<String> getDownloadUrl(@NotBlank Long id) {
        return R.success(attachmentService.getNotNullAttachment(id).getUrl());
    }

    @GetMapping("download")
    @Operation(summary = "下载附件", description = "根据 ID 下载附件")
    @SgLog(value = "下载文件", operationType = OperationType.DOWNLOAD)
    public R<String> download(@NotBlank Long id) {
        Attachment attachment = attachmentService.getNotNullAttachment(id);
        AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
        return R.success(attachmentManager.getDownloadUrl(group, attachment.getAttachName(), attachment.getHash()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除附件", description = "根据 ID 删除附件")
    @SgLog(value = "删除附件", operationType = OperationType.DELETE)
    public R<Boolean> delete(@PathVariable Long id) throws IOException {
        Attachment attachment = attachmentService.getNotNullAttachment(id);
        return R.success(attachmentManager.delete(attachment));
    }

    @PostMapping("deleteAll")
    @Operation(summary = "批量删除附件")
    @SgLog(value = "批量删除附件", operationType = OperationType.DELETE)
    public R<Boolean> deleteAll(@RequestBody Long[] ids) throws IOException {
        for (Long id : ids) {
            this.delete(id);
        }
        return R.success(true);
    }

    @PostMapping(value = "findById")
    @Operation(summary = "批量查询附件信息")
    public R<List<AttachmentVo>> findById(@RequestBody Long[] ids) {
        R<List<AttachmentVo>> returnT = null;
        List<Attachment> attachmentList = attachmentService.findListById(ids);
        if (CollectionUtils.isNotEmpty(attachmentList)) {
            returnT = R.success(attachmentList.stream().map(temp -> {
                AttachmentVo vo = new AttachmentVo();
                BeanUtils.copyProperties(temp, vo);
                return vo;
            }).collect(Collectors.toList()));
        }
        return returnT;
    }

    @GetMapping("/{id}/getPreviewUrl")
    @Operation(summary = "查询附件预览")
    public R<String> getPreviewUrl(@PathVariable Long id) {
        return R.success(attachmentManager.getPreviewUrl(id));
    }

    @GetMapping("/{id}/getPreviewAttachment")
    @Operation(summary = "查询附件预览信息")
    public R<Attachment> getPreviewAttachment(@PathVariable Long id) {
        return R.success(attachmentManager.getPreviewAttachment(id));
    }

    @GetMapping("/{id}/canPreview")
    @Operation(summary = "判断附件是否支持预览")
    public R<Boolean> canPreview(@PathVariable Long id) {
        Attachment attachment = attachmentService.get(id);
        return R.success(attachment != null && ArrayUtils.contains(sysProperties.getCanPreview().split(","),
                attachment.getAttachType()));
    }

    @GetMapping("/preview")
    @Operation(summary = "预览附件")
    public void preview(HttpServletResponse response, @RequestParam Long id) throws Exception {
        Attachment attachment = attachmentService.get(id);
        FileInputStream stream = new FileInputStream(attachment.getAttachName());
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

    @GetMapping("/createRandomImage")
    @Operation(summary = "生成默认图片")
    public R<Long> createRandomImage(@RequestParam String groupCode) {
        return R.success(attachmentManager.defaultImage(groupCode));
    }

    private AttachGroup getAttachGroup(String groupCode) {
        if (StringUtils.isEmpty(groupCode)) {
            groupCode = AttachTypeEnum.OTHER.getValue();
        }
        return groupService.findByGroupCode(groupCode);
    }
}
