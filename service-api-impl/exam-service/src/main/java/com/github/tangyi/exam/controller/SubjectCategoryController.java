package com.github.tangyi.exam.controller;

import cn.hutool.core.collection.CollUtil;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.utils.TreeUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.dto.SubjectCategoryDto;
import com.github.tangyi.exam.api.module.SubjectCategory;
import com.github.tangyi.exam.service.SubjectCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping(value = "/categories")
    @ApiOperation(value = "获取分类列表")
    public List<SubjectCategoryDto> menus() {
        // 查询所有分类
        List<SubjectCategory> subjectCategoryList = categoryService.findList(new SubjectCategory());
        if (CollectionUtils.isNotEmpty(subjectCategoryList)) {
            // 转成dto
            List<SubjectCategoryDto> subjectCategorySetTreeList = subjectCategoryList.stream().map(SubjectCategoryDto::new).distinct().collect(Collectors.toList());
            // 排序、组装树形结构
            return TreeUtil.buildTree(CollUtil.sort(subjectCategorySetTreeList, Comparator.comparingInt(SubjectCategoryDto::getSort)), "-1");
        }
        return new ArrayList<>();
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
    @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<SubjectCategory> subjectCategory(@PathVariable String id) {
        SubjectCategory subjectCategory = new SubjectCategory();
        if (StringUtils.isNotBlank(id)) {
            subjectCategory.setId(id);
            subjectCategory = categoryService.get(subjectCategory);
        }
        return new ResponseBean<>(subjectCategory);
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
    @PreAuthorize("hasAuthority('exam:subject:category:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "创建分类", notes = "创建分类")
    @ApiImplicitParam(name = "subjectCategory", value = "分类实体subjectCategory", required = true, dataType = "SubjectCategory")
    @Log("新增题目分类")
    public ResponseBean<Boolean> addSubjectCategory(@RequestBody SubjectCategory subjectCategory) {
        subjectCategory.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
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
    @PreAuthorize("hasAuthority('exam:subject:category:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "更新分类信息", notes = "根据分类id更新分类的基本信息")
    @ApiImplicitParam(name = "subjectCategory", value = "分类实体subjectCategory", required = true, dataType = "SubjectCategory")
    @Log("更新题目分类")
    public ResponseBean<Boolean> updateSubjectCategory(@RequestBody SubjectCategory subjectCategory) {
        subjectCategory.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
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
    @PreAuthorize("hasAuthority('exam:subject:category:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "删除分类", notes = "根据ID删除分类")
    @ApiImplicitParam(name = "id", value = "分类ID", required = true, paramType = "path")
    @Log("删除题目分类")
    public ResponseBean<Boolean> deleteSubjectCategory(@PathVariable String id) {
        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setId(id);
        return new ResponseBean<>(categoryService.delete(subjectCategory) > 0);
    }
}
