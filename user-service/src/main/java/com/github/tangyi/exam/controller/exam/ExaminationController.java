package com.github.tangyi.exam.controller.exam;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.exam.ExaminationService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 考试controller
 *
 * @author tangyi
 * @date 2018/11/8 21:26
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "考试信息管理")
@RestController
@RequestMapping("/v1/examination")
public class ExaminationController extends BaseController {

	private final ExaminationService examinationService;

	@GetMapping("/{id}")
	@Operation(summary = "获取考试信息", description = "根据考试id获取考试详细信息")
	public R<Examination> examination(@PathVariable Long id) {
		return R.success(examinationService.get(id));
	}

	@GetMapping("/anonymousUser/{id}")
	@Operation(summary = "获取考试信息", description = "根据考试id获取考试详细信息")
	public R<Examination> anonymousUserGet(@PathVariable Long id) {
		return R.success(examinationService.get(id));
	}

	@GetMapping("examinationList")
	@Operation(summary = "获取考试列表")
	public R<PageInfo<ExaminationDto>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examinationService.examinationList(condition, pageNum, pageSize));
	}

	@GetMapping("userFavorites")
	@Operation(summary = "获取用户收藏列表")
	public R<PageInfo<ExaminationDto>> userFavorites(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(examinationService.findUserFavoritesByPage(condition, pageNum, pageSize));
	}

	/**
	 * 根据考试ID获取题目分页数据
	 *
	 * @param pageNum    pageNum
	 * @param pageSize   pageSize
	 * @param subjectDto subjectDto
	 * @return PageInfo
	 * @author tangyi
	 * @date 2019/6/16 15:45
	 */
	@RequestMapping("subjectList")
	@Operation(summary = "获取题目列表")
	public R<PageInfo<SubjectDto>> subjectList(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize,
			SubjectDto subjectDto) {
		return R.success(examinationService.findSubjectPageById(subjectDto, condition, pageNum, pageSize));
	}

	/**
	 * 创建
	 *
	 * @param examinationDto examinationDto
	 * @return R
	 * @author tangyi
	 * @date 2018/11/10 21:14
	 */
	@PostMapping
	@Operation(summary = "创建考试", description = "创建考试")
	@SgLog(value = "创建考试", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExaminationDto examinationDto) {
		return R.success(examinationService.insert(examinationDto) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "更新考试信息", description = "根据考试id更新考试的基本信息")
	@SgLog(value = "更新考试", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable Long id, @RequestBody @Valid ExaminationDto examinationDto) {
		examinationDto.setId(id);
		return R.success(examinationService.update(examinationDto) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除考试", description = "根据ID删除考试")
	@SgLog(value = "删除考试", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable Long id) {
		boolean success = false;
		try {
			Examination examination = examinationService.get(id);
			if (examination != null) {
				examination.setCommonValue();
				success = examinationService.delete(examination) > 0;
			}
		} catch (Exception e) {
			log.error("Delete examination failed", e);
		}
		return R.success(success);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除考试", description = "根据考试id批量删除考试")
	@SgLog(value = "删除考试", operationType = OperationType.DELETE)
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		boolean success = false;
		try {
			if (ArrayUtils.isNotEmpty(ids))
				success = examinationService.deleteAll(ids) > 0;
		} catch (Exception e) {
			log.error("Delete examination failed", e);
		}
		return R.success(success);
	}

	@GetMapping("nexSubjectNo/{id}")
	@Operation(summary = "获取下一题的序号")
	public R<Integer> nexSubjectNo(@PathVariable Long id) {
		return R.success(examinationService.nextSubjectNo(id));
	}
}
