package com.github.tangyi.exam.controller;

import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.model.R;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@Tag(name = "首页信息")
@RestController
@RequestMapping("/v1/home")
public class HomeController extends BaseController {

	private final IUserService userService;

	private final ICourseService courseService;

	private final IExaminationService examinationService;

	private final SubjectsService subjectsService;

	@GetMapping("summary")
	public R<Object> summary() {
		Map<String, Object> res = Maps.newHashMapWithExpectedSize(4);
		res.put("userCount", userService.findAllUserCount());
		res.put("courseCount", courseService.findAllCourseCount());
		res.put("examinationCount", examinationService.findAllExaminationCount());
		res.put("subjectCount", subjectsService.findAllSubjectCount());
		return R.success(res);
	}
}
