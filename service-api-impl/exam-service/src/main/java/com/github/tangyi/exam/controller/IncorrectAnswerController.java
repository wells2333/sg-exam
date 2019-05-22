package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.dto.IncorrectAnswerDto;
import com.github.tangyi.exam.api.module.IncorrectAnswer;
import com.github.tangyi.exam.api.module.Subject;
import com.github.tangyi.exam.service.IncorrectAnswerService;
import com.github.tangyi.exam.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 错题controller
 *
 * @author tangyi
 * @date 2018/11/8 21:28
 */
@Slf4j
@AllArgsConstructor
@Api("错题信息管理")
@RestController
@RequestMapping("/v1/incorrectAnswer")
public class IncorrectAnswerController extends BaseController {

    private final IncorrectAnswerService incorrectAnswerService;

    private final SubjectService subjectService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:36
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取错题信息", notes = "根据错题id获取错题详细信息")
    @ApiImplicitParam(name = "id", value = "错题ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<IncorrectAnswer> examRecord(@PathVariable String id) {
        IncorrectAnswer incorrectAnswer = new IncorrectAnswer();
        if (StringUtils.isNotBlank(id)) {
            incorrectAnswer.setId(id);
            incorrectAnswer = incorrectAnswerService.get(incorrectAnswer);
        }
        return new ResponseBean<>(incorrectAnswer);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum         pageNum
     * @param pageSize        pageSize
     * @param sort            sort
     * @param order           order
     * @param incorrectAnswer incorrectAnswer
     * @return PageInfo
     * @author tangyi
     * @date 2018/11/10 21:37
     */
    @RequestMapping("incorrectAnswerList")
    @ApiOperation(value = "获取错题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "incorrectAnswer", value = "错题信息", dataType = "IncorrectAnswer")
    })
    public PageInfo<IncorrectAnswerDto> incorrectAnswerList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                            IncorrectAnswer incorrectAnswer) {
        // 查找错题
        PageInfo<IncorrectAnswer> incorrectAnswerPageInfo = incorrectAnswerService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), incorrectAnswer);
        PageInfo<IncorrectAnswerDto> pageInfo = new PageInfo<>();
        List<IncorrectAnswerDto> incorrectAnswerDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(incorrectAnswerPageInfo.getList())) {
            Subject subject = new Subject();
            // 获取题目ID
            subject.setIds(incorrectAnswerPageInfo.getList().stream().map(IncorrectAnswer::getSubjectId).distinct().toArray(String[]::new));
            // 查找题目
            List<Subject> subjects = subjectService.findListById(subject);
            if (CollectionUtils.isNotEmpty(subjects)) {
                subjects.forEach(tempSubject -> {
                    incorrectAnswerPageInfo.getList().stream()
                            .filter(tempIncorrectAnswer -> tempSubject.getId().equalsIgnoreCase(tempIncorrectAnswer.getSubjectId()))
                            .forEach(tempIncorrectAnswer -> {
                                IncorrectAnswerDto incorrectAnswerDto = new IncorrectAnswerDto();
                                BeanUtils.copyProperties(tempSubject, incorrectAnswerDto);
                                incorrectAnswerDto.setIncorrectAnswer(tempIncorrectAnswer.getIncorrectAnswer());
                                incorrectAnswerDtoList.add(incorrectAnswerDto);
                            });
                });
            }
        }
        pageInfo.setPageSize(incorrectAnswerPageInfo.getPageSize());
        pageInfo.setPageNum(incorrectAnswerPageInfo.getPageNum());
        pageInfo.setTotal(incorrectAnswerPageInfo.getTotal());
        pageInfo.setList(incorrectAnswerDtoList);
        return pageInfo;
    }

    /**
     * 创建
     *
     * @param incorrectAnswer incorrectAnswer
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:38
     */
    @PostMapping
    @ApiOperation(value = "创建错题", notes = "创建错题")
    @ApiImplicitParam(name = "incorrectAnswer", value = "错题实体incorrectAnswer", required = true, dataType = "IncorrectAnswer")
    @Log("新增错题")
    public ResponseBean<Boolean> addIncorrectAnswer(@RequestBody IncorrectAnswer incorrectAnswer) {
        incorrectAnswer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(incorrectAnswerService.insert(incorrectAnswer) > 0);
    }

    /**
     * 更新
     *
     * @param incorrectAnswer incorrectAnswer
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:38
     */
    @PutMapping
    @ApiOperation(value = "更新错题信息", notes = "根据错题id更新错题的基本信息")
    @ApiImplicitParam(name = "incorrectAnswer", value = "错题实体incorrectAnswer", required = true, dataType = "IncorrectAnswer")
    @Log("更新错题")
    public ResponseBean<Boolean> updateIncorrectAnswer(@RequestBody IncorrectAnswer incorrectAnswer) {
        incorrectAnswer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return new ResponseBean<>(incorrectAnswerService.update(incorrectAnswer) > 0);
    }

    /**
     * 删除
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:39
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除错题", notes = "根据ID删除错题")
    @ApiImplicitParam(name = "id", value = "错题ID", required = true, paramType = "path")
    @Log("删除错题")
    public ResponseBean<Boolean> deleteIncorrectAnswer(@PathVariable String id) {
        boolean success = false;
        try {
            IncorrectAnswer incorrectAnswer = incorrectAnswerService.get(id);
            if (incorrectAnswer != null) {
                incorrectAnswer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                success = incorrectAnswerService.delete(incorrectAnswer) > 0;
            }
        } catch (Exception e) {
            log.error("删除错题失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
