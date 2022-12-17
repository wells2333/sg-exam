package com.github.tangyi.exam.controller.fav;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.ExamUserFav;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.constants.UserFavConstant;
import com.github.tangyi.exam.service.course.CourseService;
import com.github.tangyi.exam.service.exam.ExaminationService;
import com.github.tangyi.exam.service.fav.CourseFavoritesService;
import com.github.tangyi.exam.service.fav.ExamFavoritesService;
import com.github.tangyi.exam.service.fav.SubjectFavoritesService;
import com.github.tangyi.exam.service.fav.UserFavService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.constants.OperationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 用户收藏Controller
 *
 * @author tangyi
 * @date 2022-12-17
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "用户收藏管理")
@RequestMapping("/v1/favorites")
public class UserFavController extends BaseController {

	private final UserFavService userFavService;

	private final ExamFavoritesService examFavoritesService;

	private final SubjectFavoritesService subjectFavoritesService;

	private final CourseFavoritesService courseFavoritesService;

	private final ExaminationService examinationService;

	private final CourseService courseService;

	private final SubjectsService subjectsService;

	@GetMapping("/list")
	@Operation(summary = "查询用户收藏列表")
	public R<PageInfo<ExamUserFav>> list(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		return R.success(userFavService.findPage(condition, pageNum, pageSize));
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "获取用户收藏详细信息")
	public R<ExamUserFav> get(@PathVariable("id") Long id) {
		return R.success(userFavService.get(id));
	}

	@PostMapping
	@Operation(summary = "新增用户收藏")
	@SgLog(value = "新增用户收藏", operationType = OperationType.INSERT)
	public R<Boolean> add(@RequestBody @Valid ExamUserFav examUserFav) {
		examUserFav.setCommonValue();
		return R.success(userFavService.insert(examUserFav) > 0);
	}

	@PutMapping("{id}")
	@Operation(summary = "修改用户收藏")
	@SgLog(value = "修改用户收藏", operationType = OperationType.UPDATE)
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody @Valid ExamUserFav examUserFav) {
		examUserFav.setId(id);
		return R.success(userFavService.update(examUserFav) > 0);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除用户收藏")
	@SgLog(value = "删除用户收藏", operationType = OperationType.DELETE)
	public R<Boolean> delete(@PathVariable("id") Long id) {
		ExamUserFav examUserFav = userFavService.get(id);
		examUserFav.setCommonValue();
		return R.success(userFavService.delete(examUserFav) > 0);
	}

	@PostMapping("favExam/{examinationId}")
	@Operation(summary = "收藏/取消收藏考试")
	@SgLog(value = "收藏/取消收藏考试", operationType = OperationType.UPDATE)
	public R<Boolean> favExam(@PathVariable Long examinationId, @RequestParam Long userId, @RequestParam Integer type) {
		return R.success(examFavoritesService.favorite(userId, examinationId, type));
	}

	@PostMapping("favSubject/{subjectId}")
	@Operation(summary = "收藏/取消收藏题目")
	@SgLog(value = "收藏/取消收藏题目", operationType = OperationType.UPDATE)
	public R<Boolean> favSubject(@PathVariable Long subjectId, @RequestParam Long userId, @RequestParam Integer type) {
		return R.success(subjectFavoritesService.favorite(userId, subjectId, type));
	}

	@PostMapping("favCourse/{courseId}")
	@Operation(summary = "收藏/取消收藏课程")
	@SgLog(value = "收藏/取消收藏课程", operationType = OperationType.UPDATE)
	public R<Boolean> favCourse(@PathVariable Long courseId, @RequestParam Long userId, @RequestParam Integer type) {
		return R.success(courseFavoritesService.favorite(userId, courseId, type));
	}

	@GetMapping("user/favorites")
	@Operation(summary = "查询用户收藏列表，支持考试/课程/题目类型")
	public R<PageInfo<?>> favorites(@RequestParam Map<String, Object> condition,
			@RequestParam(value = PAGE, required = false, defaultValue = PAGE_DEFAULT) int pageNum,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize) {
		String targetType = condition.getOrDefault("targetType", UserFavConstant.FAV_TYPE_EXAM).toString();
		PageInfo<ExamUserFav> page = userFavService.findUserFavoritesPage(condition, pageNum, pageSize, targetType);
		if (CollectionUtils.isNotEmpty(page.getList())) {
			int targetTypeInt = Integer.parseInt(targetType);
			PageInfo<?> result = switch (targetTypeInt) {
				case UserFavConstant.FAV_TYPE_EXAM -> examinationService.findUserFavoritesPage(page);
				case UserFavConstant.FAV_TYPE_COURSE -> courseService.findUserFavoritesPage(page);
				case UserFavConstant.FAV_TYPE_SUBJECT -> subjectsService.findUserFavoritesPage(page);
				default -> new PageInfo<>();
			};
			return R.success(result);
		}
		return R.success(new PageInfo<>());
	}
}
