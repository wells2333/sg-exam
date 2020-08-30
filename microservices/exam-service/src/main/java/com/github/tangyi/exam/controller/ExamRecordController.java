package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDashboardDto;
import com.github.tangyi.api.exam.dto.ExaminationRecordDto;
import com.github.tangyi.api.exam.dto.StartExamDto;
import com.github.tangyi.api.exam.module.ExaminationRecord;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.exam.service.AnswerService;
import com.github.tangyi.exam.service.ExamRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
        return new ResponseBean<>(examRecordService.get(id));
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
        return examRecordService.examRecordList(examRecord, pageNum, pageSize, sort, order);
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
    public ResponseBean<ExaminationRecord> addExamRecord(@RequestBody @Valid ExaminationRecord examRecord) {
        examRecord.setCommonValue();
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
    public ResponseBean<Boolean> updateExamRecord(@RequestBody @Valid ExaminationRecord examRecord) {
        examRecord.setCommonValue();
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
    public ResponseBean<Boolean> deleteExamRecord(@PathVariable Long id) {
        boolean success = false;
        try {
            ExaminationRecord examRecord = examRecordService.get(id);
            if (examRecord != null) {
                examRecord.setCommonValue();
                success = examRecordService.delete(examRecord) > 0;
            }
        } catch (Exception e) {
            log.error("Delete examRecord failed", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 导出
     *
     * @param ids ids
	 * @param request request
	 * @param response response
     * @author tangyi
     * @date 2018/12/31 22:28
     */
    @PostMapping("export")
    @ApiOperation(value = "导出考试成绩", notes = "根据成绩id导出成绩")
    @ApiImplicitParam(name = "ids", value = "成绩ID", required = true, dataType = "Long")
    public void exportExamRecord(@RequestBody Long[] ids, HttpServletRequest request, HttpServletResponse response) {
    	examRecordService.exportExamRecord(ids, request, response);
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
	@ApiOperation(value = "获取服务器当前时间", notes = "获取服务器当前时间")
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

	/**
	 * 查询考试监控数据
	 *
	 * @param tenantCode tenantCode
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/10/27 20:07:38
	 */
	@GetMapping("dashboard")
	public ResponseBean<ExaminationDashboardDto> findExamDashboardData(@RequestParam @NotBlank String tenantCode) {
		return new ResponseBean<>(examRecordService.findExamDashboardData(tenantCode));
	}

	/**
	 * 查询过去n天的考试记录数据
	 * @param tenantCode tenantCode
	 * @param pastDays pastDays
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2020/1/31 5:46 下午
	 */
	@GetMapping("dashboard/examRecordTendency")
	public ResponseBean<ExaminationDashboardDto> findExamRecordTendency(@RequestParam @NotBlank String tenantCode,
			@RequestParam @NotBlank Integer pastDays) {
		return new ResponseBean<>(examRecordService.findExamRecordTendency(tenantCode, pastDays));
	}

    /**
     * 成绩详情
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2020/2/20 23:54
     */
    @GetMapping("/{id}/details")
	@ApiOperation(value = "成绩详情", notes = "根据考试记录id获取成绩详情")
	@ApiImplicitParam(name = "id", value = "考试记录ID", required = true, dataType = "Long", paramType = "path")
	public ResponseBean<ExaminationRecordDto> details(@PathVariable Long id) {
    	return new ResponseBean<>(examRecordService.details(id));
	}

    /**
     * 开始考试
     *
     * @param examinationId examinationId
     * @param identifier identifier
     * @return ResponseBean
     * @author tangyi
     * @date 2020/3/21 5:51 下午
     */
    @PostMapping("anonymousUser/start")
    public ResponseBean<StartExamDto> anonymousUserStart(Long examinationId, String identifier) {
        return new ResponseBean<>(answerService.anonymousUserStart(examinationId, identifier));
    }
}
