package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.*;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.annotations.AdminTenantTeacherAuthorization;
import com.github.tangyi.exam.api.dto.ExaminationRecordDto;
import com.github.tangyi.exam.api.dto.StartExamDto;
import com.github.tangyi.exam.api.enums.SubmitStatusEnum;
import com.github.tangyi.exam.api.module.Examination;
import com.github.tangyi.exam.api.module.ExaminationRecord;
import com.github.tangyi.exam.service.AnswerService;
import com.github.tangyi.exam.service.ExamRecordService;
import com.github.tangyi.exam.service.ExaminationService;
import com.github.tangyi.exam.utils.ExamRecordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 考试记录controller
 *
 * @author tangyi
 * @date 2018/11/8 21:27
 */
@Slf4j
@AllArgsConstructor
@Api("考试记录信息管理")
@RestController
@RequestMapping("/v1/examRecord")
public class ExamRecordController extends BaseController {

    private final ExamRecordService examRecordService;

    private final ExaminationService examinationService;

    private final AnswerService answerService;

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
    @ApiImplicitParam(name = "id", value = "考试记录ID", required = true, dataType = "Long", paramType = "path")
    public ResponseBean<ExaminationRecord> examRecord(@PathVariable Long id) {
        ExaminationRecord examRecord = new ExaminationRecord();
        examRecord.setId(id);
        return new ResponseBean<>(examRecordService.get(examRecord));
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
    @GetMapping("examRecordList")
    @ApiOperation(value = "获取考试记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "examRecord", value = "考试记录信息", dataType = "ExamRecord")
    })
    public PageInfo<ExaminationRecordDto> examRecordList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                         @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                         @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                         ExaminationRecord examRecord) {
        examRecord.setTenantCode(SysUtil.getTenantCode());
        PageInfo<ExaminationRecordDto> examRecordDtoPageInfo = new PageInfo<>();
        List<ExaminationRecordDto> examRecordDtoList = new ArrayList<>();
        // 查询考试记录
        PageInfo<ExaminationRecord> examRecordPageInfo = examRecordService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examRecord);
        if (CollectionUtils.isNotEmpty(examRecordPageInfo.getList())) {
            // 查询考试信息
            List<Examination> examinations = examinationService.findListById(examRecordPageInfo.getList().stream().map(ExaminationRecord::getExaminationId).distinct().toArray(Long[]::new));
			examRecordPageInfo.getList().forEach(tempExamRecord -> {
				// 找到考试记录所属的考试信息
				Examination examinationRecordExamination = examinations.stream()
						.filter(tempExamination -> tempExamRecord.getExaminationId().equals(tempExamination.getId()))
						.findFirst().orElse(null);
				// 转换成ExamRecordDto
				if (examinationRecordExamination != null) {
					ExaminationRecordDto examRecordDto = new ExaminationRecordDto();
					BeanUtils.copyProperties(examinationRecordExamination, examRecordDto);
					examRecordDto.setId(tempExamRecord.getId());
					examRecordDto.setStartTime(tempExamRecord.getStartTime());
					examRecordDto.setEndTime(tempExamRecord.getEndTime());
					examRecordDto.setScore(tempExamRecord.getScore());
					examRecordDto.setUserId(tempExamRecord.getUserId());
					examRecordDto.setExaminationId(tempExamRecord.getExaminationId());
					// 正确题目数
					examRecordDto.setCorrectNumber(tempExamRecord.getCorrectNumber());
					examRecordDto.setInCorrectNumber(tempExamRecord.getInCorrectNumber());
					// 提交状态
					examRecordDto.setSubmitStatus(tempExamRecord.getSubmitStatus());
					examRecordDtoList.add(examRecordDto);
				}
			});
			examRecordService.fillExamUserInfo(examRecordDtoList, examRecordPageInfo.getList().stream().map(ExaminationRecord::getUserId).distinct().toArray(Long[]::new));
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
    public ResponseBean<ExaminationRecord> addExamRecord(@RequestBody @Valid ExaminationRecord examRecord) {
        examRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
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
    public ResponseBean<Boolean> updateExamRecord(@RequestBody @Valid ExaminationRecord examRecord) {
        examRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
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
    public ResponseBean<Boolean> deleteExamRecord(@PathVariable Long id) {
        boolean success = false;
        try {
            ExaminationRecord examRecord = examRecordService.get(id);
            if (examRecord != null) {
                examRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                success = examRecordService.delete(examRecord) > 0;
            }
        } catch (Exception e) {
            log.error("删除考试记录失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 导出
     *
     * @param ids ids
     * @author tangyi
     * @date 2018/12/31 22:28
     */
    @PostMapping("export")
    @AdminTenantTeacherAuthorization
    @ApiOperation(value = "导出考试成绩", notes = "根据成绩id导出成绩")
    @ApiImplicitParam(name = "ids", value = "成绩ID", required = true, dataType = "Long")
    @Log("导出考试记录")
    public void exportExamRecord(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "考试成绩" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<ExaminationRecord> examRecordList;
            if (ArrayUtils.isNotEmpty(ids)) {
                examRecordList = examRecordService.findListById(ids);
            } else {
                // 导出全部
                ExaminationRecord examRecord = new ExaminationRecord();
                examRecord.setTenantCode(SysUtil.getTenantCode());
                examRecordList = examRecordService.findList(examRecord);
            }
            // 查询考试、用户、部门数据
            if (CollectionUtils.isNotEmpty(examRecordList)) {
                List<ExaminationRecordDto> examRecordDtoList = new ArrayList<>();
                // 查询考试信息
                List<Examination> examinations = examinationService.findListById(examRecordList.stream().map(ExaminationRecord::getExaminationId).distinct().toArray(Long[]::new));
                // 用户id
                Set<Long> userIdSet = new HashSet<>();
                examRecordList.forEach(tempExamRecord -> {
                    // 查找考试记录所属的考试信息
                    Examination examRecordExamination = examinations.stream()
                            .filter(tempExamination -> tempExamRecord.getExaminationId().equals(tempExamination.getId()))
                            .findFirst().orElse(null);
                    if (examRecordExamination != null) {
                        ExaminationRecordDto recordDto = new ExaminationRecordDto();
                        recordDto.setId(tempExamRecord.getId());
                        recordDto.setExaminationName(examRecordExamination.getExaminationName());
                        recordDto.setStartTime(tempExamRecord.getStartTime());
                        recordDto.setEndTime(tempExamRecord.getEndTime());
                        recordDto.setDuration(ExamRecordUtil.getExamDuration(tempExamRecord.getStartTime(), tempExamRecord.getEndTime()));
                        recordDto.setScore(tempExamRecord.getScore());
                        recordDto.setUserId(tempExamRecord.getUserId());
                        recordDto.setCorrectNumber(tempExamRecord.getCorrectNumber());
                        recordDto.setInCorrectNumber(tempExamRecord.getInCorrectNumber());
                        recordDto.setSubmitStatusName(SubmitStatusEnum.match(tempExamRecord.getSubmitStatus(), SubmitStatusEnum.NOT_SUBMITTED).getName());
                        userIdSet.add(tempExamRecord.getUserId());
                        examRecordDtoList.add(recordDto);
                    }
                });
                examRecordService.fillExamUserInfo(examRecordDtoList, userIdSet.toArray(new Long[0]));
                // 导出
                ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(examRecordDtoList), ExamRecordUtil.getExamRecordDtoMap());
            }
        } catch (Exception e) {
            log.error("导出成绩数据失败！", e);
        }
    }

    /**
     * 开始考试
     *
     * @param examRecord examRecord
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/30 16:45
     */
    @PostMapping("start")
    @Log("开始考试")
    public ResponseBean<StartExamDto> start(@RequestBody ExaminationRecord examRecord) {
        return new ResponseBean<>(answerService.start(examRecord));
    }

    /**
     * 获取服务器当前时间
     *
     * @return ResponseBean
     * @author tangyi
     * @date 2019/05/07 22:03
     */
    @GetMapping("currentTime")
    public ResponseBean<String> currentTime() {
        return new ResponseBean<>(DateUtils.localDateToString(LocalDateTime.now()));
    }

    /**
     * 完成批改
     *
     * @param examRecord examRecord
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/19 14:33
     */
    @PutMapping("completeMarking")
    public ResponseBean<Boolean> completeMarking(@RequestBody ExaminationRecord examRecord) {
        return new ResponseBean<>(answerService.completeMarking(examRecord));
    }
}
