package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.*;
import com.github.tangyi.common.core.vo.DeptVo;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.dto.ExamRecordDto;
import com.github.tangyi.exam.api.module.ExamRecord;
import com.github.tangyi.exam.api.module.Examination;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.exam.service.ExaminationService;
import com.github.tangyi.exam.utils.ExamRecordUtil;
import com.github.tangyi.user.api.feign.UserServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 考试记录controller
 *
 * @author tangyi
 * @date 2018/11/8 21:27
 */
@Api("考试记录信息管理")
@RestController
@RequestMapping("/v1/examRecord")
public class ExamRecordController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExamRecordController.class);

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:33
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取考试记录信息", notes = "根据考试记录id获取考试记录详细信息")
    @ApiImplicitParam(name = "id", value = "考试记录ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<ExamRecord> examRecord(@PathVariable String id) {
        ExamRecord examRecord = new ExamRecord();
        if (StringUtils.isNotBlank(id)) {
            examRecord.setId(id);
            examRecord = examRecordService.get(examRecord);
        }
        return new ResponseBean<>(examRecord);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param sort       sort
     * @param order      order
     * @param examRecord examRecord
     * @return PageInfo
     * @author tangyi
     * @date 2018/11/10 21:33
     */
    @RequestMapping("examRecordList")
    @ApiOperation(value = "获取考试记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "examRecord", value = "考试记录信息", dataType = "ExamRecord")
    })
    public PageInfo<ExamRecordDto> examRecordList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                  @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                  @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                  @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                  ExamRecord examRecord) {
        PageInfo<ExamRecordDto> examRecordDtoPageInfo = new PageInfo<>();
        List<ExamRecordDto> examRecordDtoList = new ArrayList<>();
        // 查询考试记录
        PageInfo<ExamRecord> examRecordPageInfo = examRecordService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examRecord);
        if (CollectionUtils.isNotEmpty(examRecordPageInfo.getList())) {
            // 考试id、用户id
            Set<String> examIdSet = new HashSet<>(), userIdSet = new HashSet<>();
            examRecordPageInfo.getList().forEach(tempExamRecord -> {
                examIdSet.add(tempExamRecord.getExaminationId());
                userIdSet.add(tempExamRecord.getUserId());
            });
            Examination examination = new Examination();
            examination.setIds(examIdSet.toArray(new String[examIdSet.size()]));
            // 查询考试信息
            List<Examination> examinations = examinationService.findListById(examination);
            // 查询用户信息
            UserVo userVo = new UserVo();
            userVo.setIds(userIdSet.toArray(new String[userIdSet.size()]));
            ResponseBean<List<UserVo>> returnT = userServiceClient.findUserById(userVo);
            if (returnT != null && CollectionUtils.isNotEmpty(returnT.getData())) {
                examRecordPageInfo.getList().forEach(tempExamRecord -> {
                    examinations.forEach(tempExamination -> {
                        if (tempExamRecord.getExaminationId().equals(tempExamination.getId())) {
                            ExamRecordDto examRecordDto = new ExamRecordDto();
                            BeanUtils.copyProperties(tempExamination, examRecordDto);
                            examRecordDto.setId(tempExamRecord.getId());
                            examRecordDto.setStartTime(tempExamRecord.getStartTime());
                            examRecordDto.setEndTime(tempExamRecord.getEndTime());
                            examRecordDto.setScore(tempExamRecord.getScore());
                            examRecordDto.setUserId(tempExamRecord.getUserId());
                            examRecordDtoList.add(examRecordDto);
                        }
                    });
                });
                // 查询部门信息
                Set<String> deptIdSet = new HashSet<>();
                returnT.getData().forEach(tempUserVo -> deptIdSet.add(tempUserVo.getDeptId()));
                DeptVo deptVo = new DeptVo();
                deptVo.setIds(deptIdSet.toArray(new String[deptIdSet.size()]));
                ResponseBean<List<DeptVo>> deptResponseBean = userServiceClient.findDeptById(deptVo);
                for (ExamRecordDto tempExamRecordDto : examRecordDtoList) {
                    for (UserVo tempUserVo : returnT.getData()) {
                        if (tempExamRecordDto.getUserId().equals(tempUserVo.getId())) {
                            tempExamRecordDto.setUserName(tempUserVo.getName());
                            // 查询部门信息
                            if (deptResponseBean != null && CollectionUtils.isNotEmpty(deptResponseBean.getData())) {
                                for (DeptVo tempDept : deptResponseBean.getData()) {
                                    // 设置所属部门名称
                                    if (tempDept.getId().equals(tempUserVo.getDeptId())) {
                                        tempExamRecordDto.setDeptName(tempDept.getDeptName());
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        examRecordDtoPageInfo.setTotal(examRecordPageInfo.getTotal());
        examRecordDtoPageInfo.setPages(examRecordPageInfo.getPages());
        examRecordDtoPageInfo.setPageSize(examRecordPageInfo.getPageSize());
        examRecordDtoPageInfo.setPageNum(examRecordPageInfo.getPageNum());
        examRecordDtoPageInfo.setList(examRecordDtoList);
        return examRecordDtoPageInfo;
    }

    /**
     * 创建
     *
     * @param examRecord examRecord
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:33
     */
    @PostMapping
    @ApiOperation(value = "创建考试记录", notes = "创建考试记录")
    @ApiImplicitParam(name = "examRecord", value = "考试记录实体examRecord", required = true, dataType = "ExamRecord")
    @Log("新增考试记录")
    public ResponseBean<ExamRecord> addExamRecord(@RequestBody ExamRecord examRecord) {
        if (StringUtils.isEmpty(examRecord.getExaminationId()))
            throw new CommonException("参数校验失败，考试id为空！");
        if (StringUtils.isEmpty(examRecord.getUserId()))
            throw new CommonException("参数校验失败，用户id为空！");
        Examination examination = new Examination();
        examination.setId(examRecord.getExaminationId());
        // 查找考试信息
        examination = examinationService.get(examination);
        if (examination != null) {
            examRecord.setExaminationName(examination.getExaminationName());
            examRecord.setCourseId(examination.getCourseId());
        }
        examRecord.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        examRecord.setStartTime(examRecord.getCreateDate());
        examRecordService.insert(examRecord);
        return new ResponseBean<>(examRecord);
    }

    /**
     * 更新
     *
     * @param examRecord examRecord
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:34
     */
    @PutMapping
    @ApiOperation(value = "更新考试记录信息", notes = "根据考试记录id更新考试记录的基本信息")
    @ApiImplicitParam(name = "examRecord", value = "考试记录实体examRecord", required = true, dataType = "ExamRecord")
    @Log("更新考试记录")
    public ResponseBean<Boolean> updateExamRecord(@RequestBody ExamRecord examRecord) {
        examRecord.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(examRecordService.update(examRecord) > 0);
    }

    /**
     * 删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:34
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除考试记录", notes = "根据ID删除考试记录")
    @ApiImplicitParam(name = "id", value = "考试记录ID", required = true, paramType = "path")
    @Log("删除考试记录")
    public ResponseBean<Boolean> deleteExamRecord(@PathVariable String id) {
        boolean success = false;
        try {
            ExamRecord examRecord = examRecordService.get(id);
            if (examRecord != null) {
                examRecord.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                success = examRecordService.delete(examRecord) > 0;
            }
        } catch (Exception e) {
            logger.error("删除考试记录失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 导出
     *
     * @param examRecordDto examRecordDto
     * @author tangyi
     * @date 2018/12/31 22:28
     */
    @PostMapping("/export")
    @PreAuthorize("hasAuthority('exam:examRecord:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "导出考试成绩", notes = "根据成绩id导出成绩")
    @ApiImplicitParam(name = "examRecordDto", value = "成绩信息", required = true, dataType = "ExamRecordDto")
    @Log("导出考试记录")
    public void exportExamRecord(@RequestBody ExamRecordDto examRecordDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "考试成绩" + new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date()) + ".xlsx"));
            List<ExamRecord> examRecordList;
            if (StringUtils.isNotEmpty(examRecordDto.getIdString())) {
                Set<String> examRecordIdSet = new HashSet<>();
                for (String id : examRecordDto.getIdString().split(",")) {
                    if (StringUtils.isNotEmpty(id))
                        examRecordIdSet.add(id);
                }
                ExamRecord examRecord = new ExamRecord();
                examRecord.setIds(examRecordIdSet.toArray(new String[examRecordIdSet.size()]));
                examRecordList = examRecordService.findListById(examRecord);
            } else {
                // 导出全部
                examRecordList = examRecordService.findList(new ExamRecord());
            }
            // 查询考试、用户、部门数据
            if (CollectionUtils.isNotEmpty(examRecordList)) {
                List<ExamRecordDto> examRecordDtoList = new ArrayList<>();
                // 查询考试信息
                Set<String> examIdSet = new HashSet<>();
                examRecordList.forEach(tempExamRecord -> examIdSet.add(tempExamRecord.getExaminationId()));
                Examination examination = new Examination();
                examination.setIds(examIdSet.toArray(new String[examIdSet.size()]));
                List<Examination> examinations = examinationService.findListById(examination);
                // 用户id
                Set<String> userIdSet = new HashSet<>();
                examRecordList.forEach(tempExamRecord -> {
                    examinations.forEach(tempExamination -> {
                        if (tempExamRecord.getExaminationId().equals(tempExamination.getId())) {
                            ExamRecordDto recordDto = new ExamRecordDto();
                            recordDto.setId(tempExamRecord.getId());
                            recordDto.setExaminationName(tempExamination.getExaminationName());
                            recordDto.setExamTime(tempExamRecord.getCreateDate());
                            recordDto.setScore(tempExamRecord.getScore());
                            recordDto.setUserId(tempExamRecord.getUserId());
                            userIdSet.add(tempExamRecord.getUserId());
                            examRecordDtoList.add(recordDto);
                        }
                    });
                });
                // 查询用户信息
                UserVo userVo = new UserVo();
                userVo.setIds(userIdSet.toArray(new String[userIdSet.size()]));
                ResponseBean<List<UserVo>> returnT = userServiceClient.findUserById(userVo);
                if (returnT != null && CollectionUtils.isNotEmpty(returnT.getData())) {
                    // 查询部门信息
                    Set<String> deptIdSet = new HashSet<>();
                    returnT.getData().forEach(tempUserVo -> deptIdSet.add(tempUserVo.getDeptId()));
                    DeptVo deptVo = new DeptVo();
                    deptVo.setIds(deptIdSet.toArray(new String[deptIdSet.size()]));
                    ResponseBean<List<DeptVo>> deptResponseBean = userServiceClient.findDeptById(deptVo);
                    examRecordDtoList.forEach(tempExamRecordDto -> returnT.getData().forEach(tempUserVo -> {
                        if (tempExamRecordDto.getUserId().equals(tempUserVo.getId())) {
                            tempExamRecordDto.setUserName(tempUserVo.getName());
                            // 查询部门信息
                            if (deptResponseBean != null && CollectionUtils.isNotEmpty(deptResponseBean.getData())) {
                                deptResponseBean.getData().forEach(tempDept -> {
                                    // 设置所属部门名称
                                    if (tempDept.getId().equals(tempUserVo.getDeptId()))
                                        tempExamRecordDto.setDeptName(tempDept.getDeptName());
                                });
                            }
                        }
                    }));
                }
                // 导出
                ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(examRecordDtoList), ExamRecordUtil.getExamRecordDtoMap());
            }
        } catch (Exception e) {
            logger.error("导出成绩数据失败！", e);
        }
    }
}
