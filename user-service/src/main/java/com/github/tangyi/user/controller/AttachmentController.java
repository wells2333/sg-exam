package com.github.tangyi.user.controller;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.AttachmentVo;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import com.github.tangyi.user.enums.AttachTypeEnum;
import com.github.tangyi.user.service.AttachGroupService;
import com.github.tangyi.user.service.AttachmentService;
import com.github.tangyi.user.service.QiNiuService;
import com.qiniu.common.QiniuException;
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
import java.util.Optional;
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

	private final QiNiuService qiNiuService;

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
		return R.success(attachmentService.findPage(condition, pageNum, pageSize));
	}

	@PostMapping("upload")
	@Operation(summary = "上传文件")
	@SgLog(value = "上传文件", operationType = OperationType.UPLOAD)
	public R<Attachment> upload(
			@Parameter(description = "要上传的文件", required = true) @RequestParam("file") MultipartFile file,
			@RequestParam(required = false) String groupCode) throws IOException {
		if (StringUtils.isEmpty(groupCode)) {
			groupCode = AttachTypeEnum.OTHER.getValue();
		}
		return R.success(qiNiuService.upload(file, groupCode, SysUtil.getUser(), SysUtil.getTenantCode()));
	}

	@GetMapping("getDownloadUrl")
	@Operation(summary = "获取下载链接")
	@SgLog(value = "获取下载链接", operationType = OperationType.PREVIEW)
	public R<String> getDownloadUrl(@NotBlank Long id) {
		Attachment attachment = attachmentService.get(id);
		SgPreconditions.checkNull(attachment, "attachment does not exist");
		AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
		SgPreconditions.checkNull(group, "group does not exist");
		return R.success(attachment.getUrl());
	}

	@GetMapping("download")
	@Operation(summary = "下载附件", description = "根据ID下载附件")
	@SgLog(value = "下载文件", operationType = OperationType.DOWNLOAD)
	public R<String> download(@NotBlank Long id) {
		Attachment attachment = attachmentService.get(id);
		SgPreconditions.checkNull(attachment, "attachment does not exist");
		AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
		SgPreconditions.checkNull(group, "group does not exist");
		return R.success(qiNiuService.getDownloadUrl(group, attachment.getAttachName()));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除附件", description = "根据ID删除附件")
	@SgLog(value = "删除附件", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) throws QiniuException {
		Attachment attachment = attachmentService.get(id);
		SgPreconditions.checkNull(attachment, "attachment does not exist");
		qiNiuService.delete(attachment);
		return R.success(attachmentService.delete(attachment) > 0);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除附件")
	@SgLog(value = "批量删除附件", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) throws QiniuException {
		List<Attachment> attachments = Lists.newArrayList();
		for (Long id : ids) {
			Optional.ofNullable(attachmentService.get(id)).ifPresent(attachments::add);
		}
		return R.success(qiNiuService.deleteAll(attachments));
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
		return R.success(qiNiuService.getPreviewUrl(id));
	}

	@GetMapping("/{id}/getPreviewAttachment")
	@Operation(summary = "查询附件预览信息")
	public R<Attachment> getPreviewAttachment(@PathVariable Long id) {
		return R.success(qiNiuService.getPreviewAttachment(id));
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
		return R.success(qiNiuService.createRandomImage(groupCode));
	}
}
