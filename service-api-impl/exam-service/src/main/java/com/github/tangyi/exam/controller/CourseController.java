package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.module.Course;
import com.github.tangyi.exam.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 课程controller
 *
 * @author tangyi
 * @date 2018/11/8 21:25
 */
@Slf4j
@AllArgsConstructor
@Api("课程信息管理")
@RestController
@RequestMapping("/v1/course")
public class CourseController extends BaseController {

    private final CourseService courseService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:28
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取课程信息", notes = "根据课程id获取课程详细信息")
    @ApiImplicitParam(name = "id", value = "课程ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Course> course(@PathVariable String id) {
        Course course = new Course();
        if (StringUtils.isNotBlank(id)) {
            course.setId(id);
            course = courseService.get(course);
        }
        return new ResponseBean<>(course);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param course   course
     * @return PageInfo
     * @author tangyi
     * @date 2018/11/10 21:30
     */
    @RequestMapping("courseList")
    @ApiOperation(value = "获取课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "course", value = "课程信息", dataType = "Course")
    })
    public PageInfo<Course> courseList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                       Course course) {
        return courseService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), course);
    }

    /**
     * 创建
     *
     * @param course course
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:31
     */
    @PostMapping
    @PreAuthorize("hasAuthority('exam:course:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "创建课程", notes = "创建课程")
    @ApiImplicitParam(name = "course", value = "课程实体course", required = true, dataType = "Course")
    @Log("新增课程")
    public ResponseBean<Boolean> addCourse(@RequestBody Course course) {
        course.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(courseService.insert(course) > 0);
    }

    /**
     * 更新
     *
     * @param course course
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:31
     */
    @PutMapping
    @PreAuthorize("hasAuthority('exam:course:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "更新课程信息", notes = "根据课程id更新课程的基本信息")
    @ApiImplicitParam(name = "course", value = "课程实体course", required = true, dataType = "Course")
    @Log("更新课程")
    public ResponseBean<Boolean> updateCourse(@RequestBody Course course) {
        course.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(courseService.update(course) > 0);
    }

    /**
     * 删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:32
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('exam:course:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "删除课程", notes = "根据ID删除课程")
    @ApiImplicitParam(name = "id", value = "课程ID", required = true, paramType = "path")
    @Log("删除课程")
    public ResponseBean<Boolean> deleteCourse(@PathVariable String id) {
        boolean success = false;
        try {
            Course course = new Course();
            course.setId(id);
            course = courseService.get(course);
            if (course != null) {
                course.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                success = courseService.delete(course) > 0;
            }
        } catch (Exception e) {
            log.error("删除课程失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量删除
     *
     * @param course course
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/4 11:26
     */
    @PostMapping("/deleteAll")
    @PreAuthorize("hasAuthority('exam:course:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "批量删除课程", notes = "根据课程id批量删除课程")
    @ApiImplicitParam(name = "course", value = "课程信息", dataType = "Course")
    @Log("批量删除课程")
    public ResponseBean<Boolean> deleteAllCourses(@RequestBody Course course) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(course.getIdString()))
                success = courseService.deleteAll(course.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除课程失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
