package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.service.AnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @ApiImplicitParam(name = "id", value = "答题ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Answer> answer(@PathVariable String id) {
        Answer answer = new Answer();
        if (StringUtils.isNotBlank(id)) {
            answer.setId(id);
            answer = answerService.get(answer);
        }
        return new ResponseBean<>(answer);
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
    @RequestMapping("answerList")
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
    @Log("新增答题")
    public ResponseBean<Boolean> addAnswer(@RequestBody Answer answer) {
        answer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
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
    @Log("修改答题")
    public ResponseBean<Boolean> updateAnswer(@RequestBody Answer answer) {
        answer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(answerService.update(answer) > 0);
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
    @Log("删除答题")
    public ResponseBean<Boolean> deleteAnswer(@PathVariable String id) {
        boolean success = false;
        try {
            Answer answer = answerService.get(id);
            if (answer != null) {
                answer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                success = answerService.delete(answer) > 0;
            }
        } catch (Exception e) {
            log.error("删除答题失败！", e);
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
    @ApiOperation(value = "保存答题", notes = "保存答题")
    @ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
    @Log("保存答题")
    public ResponseBean<Boolean> save(@RequestBody Answer answer) {
        return new ResponseBean<>(answerService.save(answer) > 0);
    }

    /**
     * 保存答题，返回下一题信息
     *
     * @param answer answer
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/30 18:06
     */
    @PostMapping("saveAndNext")
    @ApiOperation(value = "保存答题", notes = "保存答题")
    @ApiImplicitParam(name = "answer", value = "答题信息", dataType = "Answer")
    public ResponseBean<SubjectDto> saveAndNext(@RequestBody Answer answer) {
        return new ResponseBean<>(answerService.saveAndNext(answer));
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
    @Log("提交答题")
    public ResponseBean<Boolean> submit(@RequestBody Answer answer) {
        return new ResponseBean<>(answerService.submitAsync(answer));
    }
}
