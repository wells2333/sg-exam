package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.model.SpeechSynthesis;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.user.service.SpeechSynthesisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "语音合成")
@RestController
@RequestMapping(value = "/v1/speechSynthesis")
public class SpeechController extends BaseController {

	private final SpeechSynthesisService speechSynthesisService;

	@Operation(summary = "获取语音信息", description = "根据语音id获取语音详细信息")
	@GetMapping("/{id}")
	public R<SpeechSynthesis> speech(@PathVariable Long id) {
		return R.success(speechSynthesisService.get(id));
	}

	@GetMapping("speechSynthesisList")
	@Operation(summary = "获取语音列表")
	public R<PageInfo<SpeechSynthesis>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(speechSynthesisService.findPage(condition, pageNum, pageSize));
	}

	@PostMapping
	@Operation(summary = "创建语音", description = "创建语音")
	@SgLog(value = "新增语音", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid SpeechSynthesis speechSynthesis) throws Exception {
		return R.success(speechSynthesisService.addSynthesis(speechSynthesis));
	}

	@PutMapping("/{id}")
	@Operation(summary = "更新语音信息", description = "根据id更新语音的基本信息")
	@SgLog(value = "修改语音", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid SpeechSynthesis speechSynthesis)
			throws Exception {
		speechSynthesis.setId(id);
		return R.success(speechSynthesisService.updateSynthesis(speechSynthesis));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除语音", description = "根据ID删除语音")
	@SgLog(value = "删除语音", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		SpeechSynthesis speechSynthesis = speechSynthesisService.get(id);
		speechSynthesis.setCommonValue();
		return R.success(speechSynthesisService.delete(speechSynthesis) > 0);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除语音", description = "根据id批量删除语音")
	@SgLog(value = "批量删除语音", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(ArrayUtils.isNotEmpty(ids) ? speechSynthesisService.deleteAll(ids) > 0 : Boolean.FALSE);
	}

	@PostMapping("uploadSpeech")
	@Operation(summary = "上传语音")
	@SgLog(value = "上传语音", operationType = OperationType.UPLOAD)
	public R<Attachment> uploadSpeech(
			@Parameter(description = "要上传的语音", required = true) @RequestParam("file") MultipartFile file)
			throws IOException {
		return R.success(speechSynthesisService.uploadSpeech(file));
	}
}
