package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.*;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.dto.SubjectBankDto;
import com.github.tangyi.exam.api.module.SubjectBank;
import com.github.tangyi.exam.api.module.SubjectCategory;
import com.github.tangyi.exam.service.SubjectBankService;
import com.github.tangyi.exam.service.SubjectCategoryService;
import com.github.tangyi.exam.utils.SubjectBankUtil;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 题库controller
 *
 * @author tangyi
 * @date 2018/12/9 14:12
 */
@Slf4j
@AllArgsConstructor
@Api("题库信息管理")
@RestController
@RequestMapping("/v1/subjectBank")
public class SubjectBankController extends BaseController {

    private final SubjectBankService subjectBankService;

    private final SubjectCategoryService subjectCategoryService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/9 14:12
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取题库信息", notes = "根据题库id获取题库详细信息")
    @ApiImplicitParam(name = "id", value = "题库ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<SubjectBank> subjectBank(@PathVariable String id) {
        SubjectBank subjectBank = new SubjectBank();
        if (StringUtils.isNotBlank(id)) {
            subjectBank.setId(id);
            subjectBank = subjectBankService.get(subjectBank);
        }
        return new ResponseBean<>(subjectBank);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum     pageNum
     * @param pageSize    pageSize
     * @param sort        sort
     * @param order       order
     * @param subjectBank subjectBank
     * @return PageInfo
     * @author tangyi
     * @date 2018/12/9 14:13
     */
    @RequestMapping("subjectBankList")
    @ApiOperation(value = "获取题库列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "subjectBank", value = "题库信息", dataType = "SubjectBank")
    })
    public PageInfo<SubjectBank> subjectBankList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                 @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                 @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                 @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                 SubjectBank subjectBank) {
        PageInfo<SubjectBank> page = subjectBankService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), subjectBank);
        if (CollectionUtils.isNotEmpty(page.getList())) {
            // 查询分类信息
            SubjectCategory subjectCategory = new SubjectCategory();
            // 流处理获取分类ID，去重，转成字符串数组
            subjectCategory.setIds(page.getList().stream().map(SubjectBank::getCategoryId).distinct().toArray(String[]::new));
            List<SubjectCategory> subjectCategoryList = subjectCategoryService.findListById(subjectCategory);
            if (CollectionUtils.isNotEmpty(subjectCategoryList)) {
                page.getList().forEach(tempSubjectBank -> {
                    SubjectCategory category = subjectCategoryList.stream()
                            .filter(tempSubjectCategory -> tempSubjectCategory.getId().equals(tempSubjectBank.getCategoryId()))
                            .findFirst()
                            .orElse(null);
                    // 设置分类名称
                    if (category != null)
                        tempSubjectBank.setCategoryName(category.getCategoryName());
                });
            }
        }
        return page;
    }

    /**
     * 创建
     *
     * @param subjectBank subjectBank
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/9 14:14
     */
    @PostMapping
    @PreAuthorize("hasAuthority('exam:subject:bank:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "创建题库", notes = "创建题库")
    @ApiImplicitParam(name = "subjectBank", value = "题库实体subjectBank", required = true, dataType = "SubjectBank")
    @Log("新增题库")
    public ResponseBean<Boolean> addSubjectBank(@RequestBody SubjectBank subjectBank) {
        subjectBank.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(subjectBankService.insert(subjectBank) > 0);
    }

    /**
     * 更新
     *
     * @param subjectBank subjectBank
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/9 14:15
     */
    @PutMapping
    @PreAuthorize("hasAuthority('exam:subject:bank:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "更新题库信息", notes = "根据题库id更新题库的基本信息")
    @ApiImplicitParam(name = "subjectBank", value = "题库实体subjectBank", required = true, dataType = "SubjectBank")
    @Log("更新题库")
    public ResponseBean<Boolean> updateSubjectBank(@RequestBody SubjectBank subjectBank) {
        subjectBank.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(subjectBankService.update(subjectBank) > 0);
    }

    /**
     * 删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/9 14:15
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('exam:subject:bank:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "删除题库", notes = "根据ID删除题库")
    @ApiImplicitParam(name = "id", value = "题库ID", required = true, paramType = "path")
    @Log("删除题库")
    public ResponseBean<Boolean> deleteSubjectBank(@PathVariable String id) {
        boolean success = false;
        try {
            SubjectBank subjectBank = new SubjectBank();
            subjectBank.setId(id);
            subjectBank = subjectBankService.get(subjectBank);
            if (subjectBank != null) {
                subjectBank.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                success = subjectBankService.delete(subjectBank) > 0;
            }
        } catch (Exception e) {
            log.error("删除题目失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 导出题目
     *
     * @param subjectBankDto subjectBankDto
     * @author tangyi
     * @date 2018/12/9 14:16
     */
    @PostMapping("/export")
    @PreAuthorize("hasAuthority('exam:subject:bank:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "导出题目", notes = "根据分类id导出题目")
    @ApiImplicitParam(name = "subjectBankDto", value = "分类信息", required = true, dataType = "SubjectBankDto")
    @Log("导出题库题目")
    public void exportSubjectBank(@RequestBody SubjectBankDto subjectBankDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "题目信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<SubjectBank> subjectBanks = new ArrayList<>();
            // 根据题目id导出
            if (StringUtils.isNotEmpty(subjectBankDto.getIdString())) {
                for (String id : subjectBankDto.getIdString().split(",")) {
                    SubjectBank subjectBank = new SubjectBank();
                    subjectBank.setId(id);
                    subjectBank = subjectBankService.get(subjectBank);
                    if (subjectBank != null)
                        subjectBanks.add(subjectBank);
                }
            } else if (StringUtils.isNotBlank(subjectBankDto.getCategoryId())) {    // 根据分类ID导出
                SubjectBank subjectBank = new SubjectBank();
                subjectBank.setCategoryId(subjectBankDto.getCategoryId());
                subjectBanks = subjectBankService.findList(subjectBank);
            }
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(subjectBanks), SubjectBankUtil.getSubjectBankMap());
        } catch (Exception e) {
            log.error("导出题目数据失败！", e);
        }
    }

    /**
     * 导入数据
     *
     * @param categoryId categoryId
     * @param file       file
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/9 14:19
     */
    @RequestMapping("/import")
    @PreAuthorize("hasAuthority('exam:subject:bank:import') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "导入题目", notes = "导入题目")
    @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "String")
    @Log("导入题库题目")
    public ResponseBean<Boolean> importSubjectBank(String categoryId, @ApiParam(value = "要上传的文件", required = true) MultipartFile file) {
        try {
            log.debug("开始导入题目数据，分类ID：{}", categoryId);
            List<SubjectBank> subjectBanks = MapUtil.map2Java(SubjectBank.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), SubjectBankUtil.getSubjectBankMap()));
            if (CollectionUtils.isNotEmpty(subjectBanks)) {
                for (SubjectBank subjectBank : subjectBanks) {
                    // 初始化考试ID
                    if (StringUtils.isBlank(subjectBank.getId())) {
                        subjectBank.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                        subjectBank.setCategoryId(categoryId);
                        subjectBankService.insert(subjectBank);
                    } else {
                        // 绑定分类ID
                        if (StringUtils.isNotBlank(categoryId))
                            subjectBank.setCategoryId(categoryId);
                        subjectBankService.update(subjectBank);
                    }
                }
            }
            return new ResponseBean<>(Boolean.TRUE);
        } catch (Exception e) {
            log.error("导入题目数据失败！", e);
        }
        return new ResponseBean<>(Boolean.FALSE);
    }

    /**
     * 批量删除
     *
     * @param subjectBankDto subjectBankDto
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/04 9:55
     */
    @PostMapping("/deleteAll")
    @PreAuthorize("hasAuthority('exam:subject:bank:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "批量删除题目", notes = "根据题目id批量删除题目")
    @ApiImplicitParam(name = "subjectBankDto", value = "题目信息", dataType = "SubjectBankDto")
    @Log("批量删除题库题目")
    public ResponseBean<Boolean> deleteSubjectBanks(@RequestBody SubjectBankDto subjectBankDto) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(subjectBankDto.getIdString()))
                success = subjectBankService.deleteAll(subjectBankDto.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除题目失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
