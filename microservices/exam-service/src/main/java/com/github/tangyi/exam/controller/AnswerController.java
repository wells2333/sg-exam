package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.RankInfoDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.module.Answer;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.exam.service.AnswerService;
import com.github.tangyi.exam.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 答题controller
 *
 * @author tangyi
 * @date 2018/11/8 21:24
 */
@Slf4j
@AllArgsConstructor
@Api("答题信息管理")
@RestController
@RequestMapping("/v1/answer")
public class AnswerController extends BaseController {

    private final AnswerService answerService;

    private final SubjectService subjectService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:23
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取答题信息", notes = "根据答题id获取答题详细信息")
    @ApiImplicitParam(name = "id", value = "答题ID", required = true, dataType = "Long", paramType = "path")
    public ResponseBean<Answer> answer(@PathVariable Long id) {
        return new ResponseBean<>(answerService.get(id));
    }

    /**
     * 获取分页数据
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param answer   answer
     * @return PageInfo
     * @author tangyi
     * @date 2018/11/10 21:25
     */
    @GetMapping("answerList")
    @ApiOperation(value = "获取答题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
    })
    public PageInfo<Answer> answerList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                       Answer answer) {
        answer.setTenantCode(SysUtil.getTenantCode());
        return answerService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), answer);
    }

    /**
     * 创建
     *
     * @param answer answer
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:26
     */
    @PostMapping
    @ApiOperation(value = "创建答题", notes = "创建答题")
    @ApiImplicitParam(name = "answer", value = "答题实体answer", required = true, dataType = "Answer")
    public ResponseBean<Boolean> addAnswer(@RequestBody @Valid Answer answer) {
        answer.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(answerService.insert(answer) > 0);
    }

    /**
     * 更新
     *
     * @param answer answer
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:27
     */
    @PutMapping
    @ApiOperation(value = "更新答题信息", notes = "根据答题id更新答题的基本信息")
    @ApiImplicitParam(name = "answer", value = "答题实体answer", required = true, dataType = "Answer")
    public ResponseBean<Boolean> updateAnswer(@RequestBody @Valid Answer answer) {
        answer.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(answerService.update(answer) > 0);
    }

    /**
     * 批改答题
     *
     * @param answer answer
     * @return ResponseBean
     * @author tangyi
     * @date 2020/02/22 14:47
     */
    @PutMapping("mark")
    @ApiOperation(value = "批改答题", notes = "根据答题id批改答题")
    @ApiImplicitParam(name = "answer", value = "答题实体answer", required = true, dataType = "Answer")
    public ResponseBean<Boolean> markAnswer(@RequestBody @Valid Answer answer) {
        answer.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(answerService.updateScore(answer) > 0);
    }

    /**
     * 删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:28
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除答题", notes = "根据ID删除答题")
    @ApiImplicitParam(name = "id", value = "答题ID", required = true, paramType = "path")
    public ResponseBean<Boolean> deleteAnswer(@PathVariable Long id) {
        boolean success = false;
        try {
            Answer answer = answerService.get(id);
            if (answer != null) {
                answer.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                success = answerService.delete(answer) > 0;
            }
        } catch (Exception e) {
            log.error("Delete answer failed", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 保存
     *
     * @param answer answer
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/24 20:06
     */
    @PostMapping("save")
    @ApiOperation(value = "保存答题，并返回下一题", notes = "保存答题，并返回下一题")
    @ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
    public ResponseBean<Boolean> save(@RequestBody @Valid Answer answer) {
        return new ResponseBean<>(answerService.save(answer) > 0);
    }

    /**
     * 保存答题，返回下一题信息
     *
     * @param answer          answer
     * @param nextType        0：下一题，1：上一题，2：提交
     * @param nextSubjectId   nextSubjectId
     * @param nextSubjectType 下一题的类型，选择题、判断题
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/30 18:06
     */
    @PostMapping("saveAndNext")
    @ApiOperation(value = "保存答题", notes = "保存答题")
    @ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
    public ResponseBean<SubjectDto> saveAndNext(@RequestBody AnswerDto answer,
                                                @RequestParam Integer nextType,
                                                @RequestParam(required = false) Long nextSubjectId,
                                                @RequestParam(required = false) Integer nextSubjectType) {
        return new ResponseBean<>(answerService.saveAndNext(answer, nextType, nextSubjectId, nextSubjectType));
    }

    /**
     * 保存答题，返回下一题信息
     *
     * @param answer          answer
     * @param nextType        0：下一题，1：上一题，2：提交
     * @param nextSubjectId   nextSubjectId
     * @param nextSubjectType 下一题的类型，选择题、判断题
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/30 18:06
     */
    @PostMapping("anonymousUser/saveAndNext")
    @ApiOperation(value = "保存答题", notes = "保存答题")
    @ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
    public ResponseBean<SubjectDto> anonymousUserSaveAndNext(@RequestBody AnswerDto answer,
                                                @RequestParam Integer nextType,
                                                @RequestParam(required = false) Long nextSubjectId,
                                                @RequestParam(required = false) Integer nextSubjectType) {
        return new ResponseBean<>(answerService.saveAndNext(answer, nextType, nextSubjectId, nextSubjectType));
    }

	/**
	 * 保存答题
	 *
	 * @param answer          answer
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/04/30 18:06
	 */
	@PostMapping("saveAnswer")
	@ApiOperation(value = "保存答题", notes = "保存答题")
	@ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
	public ResponseBean<Boolean> saveAnswer(@RequestBody AnswerDto answer) {
		return new ResponseBean<>(answerService.save(answer, SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode()) > 0);
	}

	/**
	 * 下一题
	 *
	 * @param examinationId       examinationId
	 * @param subjectId          subjectId
	 * @param type          	type
	 * @param nextType          0：下一题，1：上一题
	 * @return ResponseBean
	 * @author tangyi
	 * @date 2019/04/30 18:06
	 */
	@GetMapping("nextSubject")
	@ApiOperation(value = "获取下一题", notes = "获取下一题")
	public ResponseBean<SubjectDto> nextSubject(@RequestParam Long examinationId, @RequestParam Long subjectId,
			@RequestParam Integer type, @RequestParam Integer nextType) {
		return new ResponseBean<>(subjectService.getNextByCurrentIdAndType(examinationId, subjectId, type, nextType));
	}

    /**
     * 提交答卷
     *
     * @param answer answer
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/24 20:44
     */
    @PostMapping("submit")
    @ApiOperation(value = "提交答卷", notes = "提交答卷")
    @ApiImplicitParam(name = "answer", value = "答卷信息", dataType = "Answer")
    public ResponseBean<Boolean> submit(@RequestBody Answer answer) {
        return new ResponseBean<>(answerService.submitAsync(answer));
    }

    /**
     * 提交答卷
     *
     * @param answer answer
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/24 20:44
     */
    @PostMapping("anonymousUser/submit")
    @ApiOperation(value = "提交答卷", notes = "提交答卷")
    @ApiImplicitParam(name = "answer", value = "答卷信息", dataType = "Answer")
    public ResponseBean<Boolean> anonymousUserSubmit(@RequestBody Answer answer) {
        return new ResponseBean<>(answerService.submitAsync(answer));
    }

    /**
     * 答题列表，包括题目的详情
     * 支持查询正确、错误类型的题目
     *
     * @param recordId recordId
     * @param answer   answer
     * @return PageInfo
     * @author tangyi
     * @date 2019/06/18 19:16
     */
    @GetMapping("record/{recordId}/answerListInfo")
    @ApiOperation(value = "获取答题信息列表", notes = "根据考试记录id获取答题详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "recordId", value = "考试记录ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
    })
    public PageInfo<AnswerDto> answerListInfo(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
            @PathVariable Long recordId, Answer answer) {
        return answerService.answerListInfo(pageNum, pageSize, sort, order, recordId, answer);
    }

    /**
     * 答题详情
     *
     * @param recordId        recordId
     * @param currentSubjectId   currentSubjectId
     * @param nextSubjectType nextSubjectType
     * @param nextType        0：下一题，1：上一题
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/18 22:50
     */
    @GetMapping("record/{recordId}/answerInfo")
    @ApiOperation(value = "答题详情", notes = "答题详情")
    @ApiImplicitParam(name = "recordId", value = "考试记录id", dataType = "Long")
    public ResponseBean<AnswerDto> answerInfo(@PathVariable Long recordId,
                                              @RequestParam(required = false) Long currentSubjectId,
                                              @RequestParam(required = false) Integer nextSubjectType,
                                              @RequestParam(required = false) Integer nextType) {
        return new ResponseBean<>(answerService.answerInfo(recordId, currentSubjectId, nextSubjectType, nextType));
    }

    /**
     * 获取排名数据，成绩由高到底排序，返回姓名、头像、分数信息
     * @param recordId recordId
     * @return ResponseBean
     * @author tangyi
     * @date 2019/12/8 23:32
     */
	@GetMapping("record/{recordId}/rankInfo")
	@ApiOperation(value = "排名列表", notes = "排名列表")
	@ApiImplicitParam(name = "recordId", value = "考试记录id", dataType = "Long")
    public ResponseBean<List<RankInfoDto>> rankInfo(@PathVariable Long recordId) {
		return new ResponseBean<>(answerService.getRankInfo(recordId));
	}

    /**
     * 移动端提交答题
     * @param examinationId examinationId
     * @return ResponseBean
     * @author tangyi
     * @date 2020/03/15 16:08
     */
    @PostMapping("anonymousUser/submitAll/{examinationId}")
    @ApiOperation(value = "提交答题", notes = "提交答题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examinationId", value = "考试id", dataType = "Long"),
            @ApiImplicitParam(name = "identifier", value = "考生账号", dataType = "String")
    })
	public ResponseBean<Boolean> anonymousUserSubmitAll(@PathVariable Long examinationId, @RequestParam String identifier, @RequestBody List<SubjectDto> subjectDtos) {
        return new ResponseBean<>(answerService.anonymousUserSubmit(examinationId, identifier, subjectDtos));
    }
}
