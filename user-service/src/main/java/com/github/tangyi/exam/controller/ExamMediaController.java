package com.github.tangyi.exam.controller;

import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.media.ExamMediaService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 内容（图片、视频）
 * @author tangyi
 * @date 2022/11/7 10:38 下午
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "内容（图片、视频）")
@RestController
@RequestMapping("/v1/examMedia")
public class ExamMediaController extends BaseController {

	private final ExamMediaService examMediaService;

	@Operation(summary = "上传视频")
	@PostMapping("uploadVideo")
	@SgLog(value = "上传视频", operationType = OperationType.UPLOAD)
	public R<Attachment> uploadVideo(
			@Parameter(description = "要上传的视频", required = true) @RequestParam("file") MultipartFile file) {
		return R.success(examMediaService.uploadVideo(file));
	}

	@Operation(summary = "上传图片")
	@PostMapping("uploadImage")
	@SgLog(value = "上传图片", operationType = OperationType.UPLOAD)
	public R<Attachment> uploadImage(
			@Parameter(description = "要上传的图片", required = true) @RequestParam("file") MultipartFile file) {
		return R.success(examMediaService.uploadImage(file));
	}

	@Operation(summary = "删除视频")
	@PostMapping("deleteVideo")
	@SgLog(value = "删除视频", operationType = OperationType.DELETE)
	public R<Boolean> deleteVideo() {
		return R.success(Boolean.TRUE);
	}

	@Operation(summary = "获取视频URL")
	@GetMapping("videoUrl/{id}")
	public R<Boolean> videoUrl(@PathVariable Long id) {
		return R.success(Boolean.TRUE);
	}

	@Operation(summary = "上传图片")
	@PostMapping("uploadImage/{id}")
	@SgLog(value = "上传图片", operationType = OperationType.UPLOAD)
	public R<Boolean> uploadImage(
			@Parameter(description = "要上传的图片", required = true) @RequestParam("file") MultipartFile file,
			@PathVariable Long id) {
		return R.success(Boolean.TRUE);
	}

	@Operation(summary = "删除图片")
	@PostMapping("deleteImage/{id}")
	@SgLog(value = "删除图片", operationType = OperationType.DELETE)
	public R<Boolean> deleteImage(@PathVariable Long id) {
		return R.success(Boolean.TRUE);
	}

	@Operation(summary = "获取图片URL")
	@GetMapping("imageUrl/{id}")
	public R<Boolean> imageUrl(@PathVariable Long id) {
		return R.success(Boolean.TRUE);
	}
}
