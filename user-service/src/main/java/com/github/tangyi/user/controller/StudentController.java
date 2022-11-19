package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.StudentDto;
import com.github.tangyi.api.user.model.Student;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.user.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 学生管理Controller
 *
 * @author tangyi
 * @date 2019/07/09 15:29
 */
@Slf4j
@AllArgsConstructor
@Tag(name = "学生管理")
@RestController
@RequestMapping("/v1/students")
public class StudentController extends BaseController {

	private final StudentService studentService;

	@Operation(summary = "获取学生信息", description = "根据学生id获取学生详细信息")
	@GetMapping("/{id}")
	public R<Student> student(@PathVariable Long id) {
		return R.success(studentService.get(id));
	}

	@GetMapping("studentList")
	@Operation(summary = "获取学生列表")
	public PageInfo<Student> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return studentService.findPage(condition, pageNum, pageSize);
	}

	@PostMapping
	@Operation(summary = "新增学生", description = "新增学生")
	public R<Boolean> add(@RequestBody @Valid StudentDto studentDto) {
		return R.success(studentService.add(studentDto) > 0);
	}

	@PutMapping
	@Operation(summary = "更新学生信息", description = "根据学生id更新学生的基本信息")
	public R<Boolean> update(@RequestBody @Valid StudentDto studentDto) {
		try {
			Student student = new Student();
			BeanUtils.copyProperties(studentDto, student);
			student.setId(studentDto.getId());
			student.setCommonValue();
			return R.success(studentService.update(student) > 0);
		} catch (Exception e) {
			log.error("Update student info failed", e);
		}
		return R.success(Boolean.FALSE);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除学生", description = "根据ID删除学生")
	public R<Boolean> delete(@PathVariable Long id) {
		try {
			Student student = studentService.get(id);
			student.setCommonValue();
			studentService.delete(student);
		} catch (Exception e) {
			log.error("Delete student info failed", e);
		}
		return R.success(Boolean.FALSE);
	}

	@PostMapping("deleteAll")
	@Operation(summary = "批量删除学生", description = "根据学生id批量删除学生")
	public R<Boolean> deleteAll(@RequestBody Long[] ids) {
		boolean success = false;
		try {
			if (ArrayUtils.isNotEmpty(ids))
				success = studentService.deleteAll(ids) > 0;
		} catch (Exception e) {
			log.error("Delete student info failed", e);
		}
		return R.success(success);
	}

	@RequestMapping(value = "findById", method = RequestMethod.POST)
	@Operation(summary = "根据ID查询学生", description = "根据ID查询学生")
	public R<List<Student>> findById(@RequestBody Long[] ids) {
		List<Student> studentList = studentService.findListById(ids);
		return Optional.ofNullable(studentList).isPresent() ? R.success(studentList) : null;
	}
}
