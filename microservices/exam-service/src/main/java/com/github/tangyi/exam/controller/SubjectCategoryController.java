package com.github.tangyi.exam.controller;

import com.github.tangyi.api.exam.constants.ExamSubjectConstant;
import com.github.tangyi.api.exam.dto.SubjectCategoryDto;
import com.github.tangyi.api.exam.module.SubjectCategory;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.exam.service.SubjectCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 题目分类controller
 *
 * @author tangyi
 * @date 2018/12/4 21:57
 */
@AllArgsConstructor
@Api("题库分类信息管理")
@RestController
@RequestMapping("/v1/subjectCategory")
public class SubjectCategoryController extends BaseController {

    private final SubjectCategoryService categoryService;

    /**
     * 返回树形分类集合
     *
     * @return List
     * @author tangyi
     * @date 2018/12/04 22:03
     */
    @GetMapping(value = "categories")
    @ApiOperation(value = "获取分类列表")
    public List<SubjectCategoryDto> menus() {
       return categoryService.menus();
    }

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/04 21:59
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取分类信息", notes = "根据分类id获取分类详细信息")
    @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long", paramType = "path")
    public ResponseBean<SubjectCategory> subjectCategory(@PathVariable Long id) {
        return new ResponseBean<>(categoryService.get(id));
    }

    /**
     * 新增分类
     *
     * @param subjectCategory subjectCategory
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/04 22:00
     */
    @PostMapping
    @ApiOperation(value = "创建分类", notes = "创建分类")
    @ApiImplicitParam(name = "subjectCategory", value = "分类实体subjectCategory", required = true, dataType = "SubjectCategory")
    public ResponseBean<Boolean> addSubjectCategory(@RequestBody @Valid SubjectCategory subjectCategory) {
        subjectCategory.setCommonValue();
        subjectCategory.setType(ExamSubjectConstant.PUBLIC_CATEGORY);
        return new ResponseBean<>(categoryService.insert(subjectCategory) > 0);
    }

    /**
     * 更新分类
     *
     * @param subjectCategory subjectCategory
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/04 22:01
     */
    @PutMapping
    @ApiOperation(value = "更新分类信息", notes = "根据分类id更新分类的基本信息")
    @ApiImplicitParam(name = "subjectCategory", value = "分类实体subjectCategory", required = true, dataType = "SubjectCategory")
    public ResponseBean<Boolean> updateSubjectCategory(@RequestBody @Valid SubjectCategory subjectCategory) {
        subjectCategory.setCommonValue();
        return new ResponseBean<>(categoryService.update(subjectCategory) > 0);
    }

    /**
     * 根据ID删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/04 22:02
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除分类", notes = "根据ID删除分类")
    @ApiImplicitParam(name = "id", value = "分类ID", required = true, paramType = "path")
    public ResponseBean<Boolean> deleteSubjectCategory(@PathVariable Long id) {
        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setId(id);
        return new ResponseBean<>(categoryService.delete(subjectCategory) > 0);
    }
}
