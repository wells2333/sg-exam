package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.*;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Examination;
import com.github.tangyi.exam.api.module.Subject;
import com.github.tangyi.exam.service.AnswerService;
import com.github.tangyi.exam.service.ExaminationService;
import com.github.tangyi.exam.service.SubjectService;
import com.github.tangyi.exam.utils.SubjectUtil;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 题目controller
 *
 * @author tangyi
 * @date 2018/11/8 21:29
 */
@Slf4j
@AllArgsConstructor
@Api("题目信息管理")
@RestController
@RequestMapping("/v1/subject")
public class SubjectController extends BaseController {

    private final SubjectService subjectService;

    private final AnswerService answerService;

    private final ExaminationService examinationService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:43
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取题目信息", notes = "根据题目id获取题目详细信息")
    @ApiImplicitParam(name = "id", value = "题目ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Subject> subject(@PathVariable String id) {
        Subject subject = new Subject();
        if (StringUtils.isNotBlank(id)) {
            subject.setId(id);
            subject = subjectService.get(subject);
        }
        return new ResponseBean<>(subject);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param subject  subject
     * @return PageInfo
     * @author tangyi
     * @date 2018/11/10 21:43
     */
    @RequestMapping("subjectList")
    @ApiOperation(value = "获取题目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "subject", value = "题目信息", dataType = "Subject")
    })
    public PageInfo<Subject> subjectList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                         @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                         @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                         Subject subject) {
        return subjectService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), subject);
    }

    /**
     * 创建
     *
     * @param subject subject
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:43
     */
    @PostMapping
    @PreAuthorize("hasAuthority('exam:exam:subject:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "创建题目", notes = "创建题目")
    @ApiImplicitParam(name = "subject", value = "题目实体subject", required = true, dataType = "Subject")
    @Log("新增题目")
    public ResponseBean<Boolean> addSubject(@RequestBody Subject subject) {
        Assert.notNull(subject.getExaminationId(), CommonConstant.IllEGAL_ARGUMENT);
        boolean success = false;
        subject.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        if (subjectService.insert(subject) > 0) {
            // 更新考试的题目数
            Examination examination = new Examination();
            examination.setId(subject.getExaminationId());
            examination = examinationService.get(examination);
            if (examination != null)
                success = examinationService.update(examination) > 0;
        }
        return new ResponseBean<>(success);
    }

    /**
     * 更新
     *
     * @param subject subject
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:43
     */
    @PutMapping
    @PreAuthorize("hasAuthority('exam:exam:subject:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "更新题目信息", notes = "根据题目id更新题目的基本信息")
    @ApiImplicitParam(name = "subject", value = "角色实体subject", required = true, dataType = "Subject")
    @Log("更新题目")
    public ResponseBean<Boolean> updateSubject(@RequestBody Subject subject) {
        subject.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(subjectService.update(subject) > 0);
    }

    /**
     * 删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:43
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('exam:exam:subject:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "删除题目", notes = "根据ID删除题目")
    @ApiImplicitParam(name = "id", value = "题目ID", required = true, paramType = "path")
    @Log("删除题目")
    public ResponseBean<Boolean> deleteSubject(@PathVariable String id) {
        boolean success = false;
        try {
            Subject subject = new Subject();
            subject.setId(id);
            subject = subjectService.get(subject);
            if (subject != null) {
                subject.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                if (subjectService.delete(subject) > 0) {
                    // 更新考试的题目数
                    Examination examination = new Examination();
                    examination.setId(subject.getExaminationId());
                    examination = examinationService.get(examination);
                    if (examination != null)
                        success = examinationService.update(examination) > 0;
                }
            }
        } catch (Exception e) {
            log.error("删除题目失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 导出题目
     *
     * @param subjectDto subjectDto
     * @author tangyi
     * @date 2018/11/28 12:53
     */
    @PostMapping("/export")
    @PreAuthorize("hasAuthority('exam:exam:subject:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "导出题目", notes = "根据分类id导出题目")
    @ApiImplicitParam(name = "subjectDto", value = "题目信息", required = true, dataType = "SubjectDto")
    @Log("导出题目")
    public void exportSubject(@RequestBody SubjectDto subjectDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "题目信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<Subject> subjects = new ArrayList<>();
            // 根据题目id导出
            if (StringUtils.isNotEmpty(subjectDto.getIdString())) {
                subjects = Stream.of(subjectDto.getIdString().split(","))
                        // 根据ID查找题目信息
                        .map(id -> {
                            Subject subject = new Subject();
                            subject.setId(id);
                            return subjectService.get(subject);
                        })
                        // 过滤收集
                        .filter(Objects::nonNull).collect(Collectors.toList());
            } else if (StringUtils.isNotEmpty(subjectDto.getExaminationId())) {  // 根据考试id导出
                Subject subject = new Subject();
                subject.setExaminationId(subjectDto.getExaminationId());
                subjects = subjectService.findList(subject);
            }
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(subjects), SubjectUtil.getSubjectMap());
        } catch (Exception e) {
            log.error("导出题目数据失败！", e);
        }
    }

    /**
     * 导入数据
     *
     * @param examinationId examinationId
     * @param file          file
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/28 12:59
     */
    @RequestMapping("import")
    @PreAuthorize("hasAuthority('exam:exam:subject:import') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "导入题目", notes = "导入题目")
    @ApiImplicitParam(name = "examinationId", value = "考试ID", required = true, dataType = "String")
    @Log("导入题目")
    public ResponseBean<Boolean> importSubject(String examinationId, @ApiParam(value = "要上传的文件", required = true) MultipartFile file) {
        boolean success = false;
        Assert.notNull(examinationId, CommonConstant.IllEGAL_ARGUMENT);
        try {
            log.debug("开始导入题目数据，分类ID：{}", examinationId);
            Stream<Subject> subjectStream = MapUtil.map2Java(Subject.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), SubjectUtil.getSubjectMap())).stream();
            if (Optional.ofNullable(subjectStream).isPresent()) {
                subjectStream.forEach(subject -> {
                    // 初始化考试ID
                    if (StringUtils.isBlank(subject.getId())) {
                        subject.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                        subject.setExaminationId(examinationId);
                        subjectService.insert(subject);
                    }
                });
                // 更新考试的题目数
                Examination examination = new Examination();
                examination.setId(examinationId);
                examination = examinationService.get(examination);
                if (examination != null)
                    success = examinationService.update(examination) > 0;
            }
            return new ResponseBean<>(Boolean.TRUE);
        } catch (Exception e) {
            log.error("导入题目数据失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量删除
     *
     * @param subjectDto subjectDto
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/04 9:55
     */
    @PostMapping("deleteAll")
    @PreAuthorize("hasAuthority('exam:exam:subject:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "批量删除题目", notes = "根据题目id批量删除题目")
    @ApiImplicitParam(name = "subjectDto", value = "题目信息", dataType = "SubjectDto")
    @Log("批量删除题目")
    public ResponseBean<Boolean> deleteSubjects(@RequestBody SubjectDto subjectDto) {
        boolean success = false;
        Assert.notNull(subjectDto.getIdString(), CommonConstant.IllEGAL_ARGUMENT);
        try {
            String[] subjectIds = subjectDto.getIdString().split(",");
            success = subjectService.deleteAll(subjectIds) > 0;
            Subject subject = new Subject();
            subject.setIds(subjectIds);
            Stream<Subject> subjectStream = subjectService.findListById(subject).stream();
            if (Optional.ofNullable(subjectStream).isPresent()) {
                subjectStream.forEach(tempSubject -> {
                    // 更新考试的题目数
                    Examination examination = new Examination();
                    examination.setId(tempSubject.getExaminationId());
                    examination = examinationService.get(examination);
                    if (examination != null)
                        examinationService.update(examination);
                });
            }
        } catch (Exception e) {
            log.error("删除题目失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 查询题目和答题
     *
     * @param serialNumber serialNumber
     * @param examRecordId examRecordId
     * @param userId       userId
     * @return ResponseBean
     * @author tangyi
     * @date 2019/01/16 22:25
     */
    @GetMapping("subjectAnswer")
    @ApiOperation(value = "查询题目和答题", notes = "根据题目id查询题目和答题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serialNumber", value = "题目序号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "examRecordId", value = "考试记录ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String")
    })
    public ResponseBean<SubjectDto> subjectAnswer(@RequestParam("serialNumber") String serialNumber,
                                                  @RequestParam("examRecordId") String examRecordId,
                                                  @RequestParam(value = "userId", required = false) String userId) {
        return new ResponseBean<>(answerService.subjectAnswer(serialNumber, examRecordId, userId));
    }
}
