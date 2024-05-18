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

package com.github.tangyi.exam.controller.subject;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.log.OperationType;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.SnowFlakeId;
import com.github.tangyi.exam.excel.SubjectExcelModel;
import com.github.tangyi.exam.service.answer.AnswerService;
import com.github.tangyi.exam.service.fav.SubjectFavService;
import com.github.tangyi.exam.service.subject.SubjectImportExportService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.ExamUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "题目信息管理")
@RestController
@RequestMapping("/v1/subjects")
public class SubjectsController extends BaseController {

	private final SubjectsService subjectsService;
	private final AnswerService answerService;
	private final SubjectImportExportService subjectImportExportService;
	private final SubjectFavService subjectFavService;

	@GetMapping("/{id}")
	@Operation(summary = "获取题目信息", description = "根据题目 ID 获取题目详细信息")
	public R<SubjectDto> subject(@PathVariable Long id,
			@RequestParam(value = "findFav", required = false, defaultValue = "false") boolean findFav,
			@RequestParam(value = "isView", required = false, defaultValue = "false") boolean isView) {
		SubjectDto dto = subjectsService.getSubject(id, isView);
		if (dto != null && findFav) {
			subjectFavService.fillUserFavorites(Collections.singletonList(dto));
		}
		return R.success(dto);
	}

	@GetMapping("subjectList")
	@Operation(summary = "获取题目列表")
	public R<PageInfo<SubjectDto>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(value = "findFav", required = false, defaultValue = "false") boolean findFav,
			@RequestParam(value = "findView", required = false, defaultValue = "false") boolean findView,
			SubjectDto subject) {
		return R.success(subjectsService.findPage(condition, pageNum, pageSize, findFav, findView, subject));
	}

	@GetMapping("getSubjectCountByCategoryId")
	@Operation(summary = "获取题目数量")
	public R<Integer> getSubjectCount(@RequestParam Long categoryId) {
		return R.success(subjectsService.findSubjectCountByCategoryId(categoryId));
	}

	@PostMapping
	@Operation(summary = "创建题目", description = "创建题目")
	@SgLog(value = "创建题目", operationType = OperationType.INSERT)
	public R<SubjectDto> add(@RequestBody @Valid SubjectDto subject) {
		subject.setCommonValue();
		// 自定义 ID
		subject.setId(SnowFlakeId.newId());
		return R.success(subjectsService.insert(subject));
	}

	@PutMapping("{id}")
	@Operation(summary = "更新题目信息", description = "根据题目 id 更新题目的基本信息")
	@SgLog(value = "更新题目", operationType = OperationType.UPDATE)
	public R<SubjectDto> update(@PathVariable Long id, @RequestBody @Valid SubjectDto subject) {
		subject.setId(id);
		subject.setCommonValue();
		return R.success(subjectsService.update(subject));
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除题目", description = "根据 ID 删除题目")
	@SgLog(value = "删除题目", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id, @RequestParam(required = false) Long examinationId,
			@RequestParam(required = false) Long categoryId) {
		return R.success(this.subjectsService.physicalDeleteAndResetSort(id, examinationId, categoryId));
	}

	@GetMapping("template/json")
	@Operation(summary = "下载题目模板（JSON 格式）")
	public void downloadJSONTemplate(HttpServletResponse res) throws IOException {
		List<SubjectDto> subjects = subjectImportExportService.demoSubjects();
		byte[] data = JSON.toJSONString(subjects).getBytes(StandardCharsets.UTF_8);
		res.reset();
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
		res.setHeader("Content-Disposition", "attachment; filename=\"template.json\"");
		res.addHeader("Content-Length", "" + data.length);
		res.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, res.getOutputStream());
	}

	@GetMapping("template/excel")
	@Operation(summary = "下载题目模板（EXCEL 格式）")
	public void downloadEXCELTemplate(HttpServletRequest req, HttpServletResponse res) {
		List<SubjectDto> subjects = subjectImportExportService.demoSubjects();
		String fileName = "template";
		List<SubjectExcelModel> models = ExamUtil.convertSubject(subjects);
		ExcelToolUtil.writeExcel(req, res, models, SubjectExcelModel.class, fileName);
	}

	@PostMapping("importJson")
	@Operation(summary = "导入 JSON 格式题目", description = "导入 JSON 格式题目")
	@SgLog(value = "导入 JSON 格式题目", operationType = OperationType.INSERT)
	public R<Boolean> importJSONSubject(Long categoryId,
			@Parameter(description = "JSON 格式题目", required = true) MultipartFile file) throws IOException {
		log.debug("importJSONSubject, categoryId: {}", categoryId);
		return R.success(subjectImportExportService.importJSONSubject(categoryId, file));
	}

	@PostMapping("importExcel")
	@Operation(summary = "导入 EXCEL 格式题目", description = "导入 EXCEL 格式题目")
	@SgLog(value = "导入 EXCEL 格式题目", operationType = OperationType.INSERT)
	public R<Boolean> importExcelSubject(Long categoryId,
			@Parameter(description = "EXCEL 格式题目", required = true) MultipartFile file) throws IOException {
		log.debug("importExcelSubject, categoryId: {}", categoryId);
		return R.success(subjectImportExportService.importExcelSubject(categoryId, file));
	}

	@PostMapping("export")
	@Operation(summary = "导出题目", description = "根据分类 id 导出题目")
	public void exportSubject(@RequestParam(required = false) Long[] ids,
			@RequestParam(required = false) Long examinationId, @RequestParam(required = false) Long categoryId,
			HttpServletRequest request, HttpServletResponse response) {
		List<SubjectDto> subjects = subjectImportExportService.export(ids, examinationId, categoryId);
		ExcelToolUtil.writeExcel(request, response, ExamUtil.convertSubject(subjects), SubjectExcelModel.class);
	}

	@GetMapping("exportDemoExcel")
	@Operation(summary = "导出 demo 题库 EXCEL 文件")
	public void ex(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<SubjectDto> subjects = subjectImportExportService.demoTxtSubjects();
		ExcelToolUtil.writeExcel(request, response, ExamUtil.convertSubject(subjects), SubjectExcelModel.class);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除题目", description = "根据题目 id 批量删除题目")
	@SgLog(value = "批量删除题目", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		return R.success(subjectsService.physicalDeleteAll(ids) > 0);
	}

	/**
	 * 查询题目和答题
	 * @param type     -1：当前题目，0：下一题，1：上一题
	 */
	@GetMapping("subjectAnswer")
	@Operation(summary = "查询题目和答题", description = "根据题目 id 查询题目和答题")
	public R<SubjectDto> subjectAnswer(@RequestParam("subjectId") @NotBlank Long subjectId,
			@RequestParam("examRecordId") @NotBlank Long examRecordId, @RequestParam Integer type,
			@RequestParam(required = false) Integer nextSubjectSortNo) {
		return R.success(answerService.subjectAnswer(subjectId, examRecordId, type, nextSubjectSortNo));
	}

	/**
	 * 查询题目和答题
	 * @param type     -1：当前题目，0：下一题，1：上一题
	 */
	@GetMapping("anonymousUser/subjectAnswer")
	@Operation(summary = "查询题目和答题", description = "根据题目 id 查询题目和答题")
	public R<SubjectDto> anonymousUserSubjectAnswer(@RequestParam("subjectId") @NotBlank Long subjectId,
			@RequestParam("examRecordId") @NotBlank Long examRecordId,
			@RequestParam(value = "userId", required = false) String userId, @RequestParam Integer type,
			@RequestParam(required = false) Integer nextSubjectSortNo) {
		return R.success(answerService.subjectAnswer(subjectId, examRecordId, type, nextSubjectSortNo));
	}

	@GetMapping("nexSubjectNo/{id}")
	@Operation(summary = "根据分类 ID 获取下一题的序号")
	public R<Integer> nexSubjectNo(@PathVariable Long id) {
		return R.success(subjectsService.nextSubjectNo(id));
	}
}
