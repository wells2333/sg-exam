package com.github.tangyi.exam.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.module.Examination;
import com.github.tangyi.api.exam.module.ExaminationSubject;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.persistence.BaseEntity;
import com.github.tangyi.common.web.BaseController;
import com.github.tangyi.exam.service.ExaminationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * 考试controller
 *
 * @author tangyi
 * @date 2018/11/8 21:26
 */
@Slf4j
@AllArgsConstructor
@Api("考试信息管理")
@RestController
@RequestMapping("/v1/examination")
public class ExaminationController extends BaseController {

    private final ExaminationService examinationService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:08
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取考试信息", notes = "根据考试id获取考试详细信息")
    @ApiImplicitParam(name = "id", value = "考试ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Examination> examination(@PathVariable Long id) {
        return new ResponseBean<>(examinationService.get(id));
    }

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:08
     */
    @GetMapping("/anonymousUser/{id}")
    @ApiOperation(value = "获取考试信息", notes = "根据考试id获取考试详细信息")
    @ApiImplicitParam(name = "id", value = "考试ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Examination> anonymousUserGet(@PathVariable Long id) {
        return new ResponseBean<>(examinationService.get(id));
    }

    /**
     * 获取分页数据
     *
     * @param pageNum     pageNum
     * @param pageSize    pageSize
     * @param sort        sort
     * @param order       order
     * @param examination examination
     * @return PageInfo
     * @author tangyi
     * @date 2018/11/10 21:10
     */
    @GetMapping("examinationList")
    @ApiOperation(value = "获取考试列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "examination", value = "考试信息", dataType = "Examination")
    })
    public PageInfo<ExaminationDto> examinationList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                    Examination examination) {
        return examinationService.examinationList(pageNum, pageSize, sort, order, examination);
    }

    /**
     * 根据考试ID获取题目分页数据
     *
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param sort       sort
     * @param order      order
     * @param subjectDto subjectDto
     * @return PageInfo
     * @author tangyi
     * @date 2019/6/16 15:45
     */
    @RequestMapping("subjectList")
    @ApiOperation(value = "获取题目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "subjectDto", value = "题目信息", dataType = "SubjectDto")
    })
    public PageInfo<SubjectDto> subjectList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                            SubjectDto subjectDto) {
        return examinationService.findSubjectPageById(subjectDto, pageNum, pageSize, sort, order);
    }

    /**
     * 获取全部题目
     * @param subjectDto subjectDto
     * @return ResponseBean
     * @author tangyi
     * @date 2020/3/12 1:00 下午
     */
	@RequestMapping("anonymousUser/allSubjectList")
	@ApiOperation(value = "获取全部题目列表")
	@ApiImplicitParam(name = "subjectDto", value = "题目信息", dataType = "SubjectDto")
    public ResponseBean<List<SubjectDto>> allSubjectList(SubjectDto subjectDto) {
		return new ResponseBean<>(examinationService.allSubjectList(subjectDto));
	}

    /**
     * 创建
     *
     * @param examinationDto examinationDto
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:14
     */
    @PostMapping
    @ApiOperation(value = "创建考试", notes = "创建考试")
    @ApiImplicitParam(name = "examinationDto", value = "考试实体examinationDto", required = true, dataType = "ExaminationDto")
    public ResponseBean<Boolean> addExamination(@RequestBody @Valid ExaminationDto examinationDto) {
        return new ResponseBean<>(examinationService.insert(examinationDto) > 0);
    }

    /**
     * 更新
     *
     * @param examinationDto examinationDto
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:15
     */
    @PutMapping
    @ApiOperation(value = "更新考试信息", notes = "根据考试id更新考试的基本信息")
    @ApiImplicitParam(name = "examinationDto", value = "考试实体answer", required = true, dataType = "ExaminationDto")
    public ResponseBean<Boolean> updateExamination(@RequestBody @Valid ExaminationDto examinationDto) {
        return new ResponseBean<>(examinationService.update(examinationDto) > 0);
    }

    /**
     * 删除考试
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/10 21:20
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除考试", notes = "根据ID删除考试")
    @ApiImplicitParam(name = "id", value = "考试ID", required = true, paramType = "path")
    public ResponseBean<Boolean> deleteExamination(@PathVariable Long id) {
        boolean success = false;
        try {
            Examination examination = examinationService.get(id);
            if (examination != null) {
                examination.setCommonValue();
                success = examinationService.delete(examination) > 0;
            }
        } catch (Exception e) {
            log.error("Delete examination failed", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/03 22:03
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除考试", notes = "根据考试id批量删除考试")
    @ApiImplicitParam(name = "ids", value = "考试ID", dataType = "Long")
    public ResponseBean<Boolean> deleteAllExaminations(@RequestBody Long[] ids) {
        boolean success = false;
        try {
            if (ArrayUtils.isNotEmpty(ids))
                success = examinationService.deleteAll(ids) > 0;
        } catch (Exception e) {
            log.error("Delete examination failed", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据考试ID查询题目id列表
     *
     * @param examinationId examinationId
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/18 14:31
     */
    @ApiImplicitParam(name = "examinationId", value = "考试ID", required = true, paramType = "path")
    @GetMapping("/{examinationId}/subjectIds")
    public ResponseBean<List<ExaminationSubject>> findExaminationSubjectIds(@PathVariable Long examinationId) {
		List<ExaminationSubject> subjects = examinationService.findListByExaminationId(examinationId);
		subjects.forEach(BaseEntity::clearCommonValue);
        return new ResponseBean<>(subjects);
    }

    /**
     * 根据考试ID查询题目id列表
     *
     * @param examinationId examinationId
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/18 14:31
     */
    @ApiImplicitParam(name = "examinationId", value = "考试ID", required = true, paramType = "path")
    @GetMapping("/anonymousUser/{examinationId}/subjectIds")
    public ResponseBean<List<ExaminationSubject>> anonymousUserFindExaminationSubjectIds(@PathVariable Long examinationId) {
        List<ExaminationSubject> subjects = examinationService.findListByExaminationId(examinationId);
        subjects.forEach(BaseEntity::clearCommonValue);
        return new ResponseBean<>(subjects);
    }

    /**
     * 根据考试ID生成二维码
     * @param examinationId examinationId
     * @param response response
     * @author tangyi
     * @date 2020/3/15 1:16 下午
     */
    @ApiOperation(value = "生成二维码", notes = "生成二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examinationId", value = "考试ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("anonymousUser/generateQrCode/{examinationId}")
    public void produceCode(@PathVariable Long examinationId, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store, no-com.github.tangyi.common.basic.cache");
        response.setContentType("image/jpeg");
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(examinationService.produceCode(examinationId)); ServletOutputStream out = response.getOutputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "PNG", out);
        }
    }

    /**
     * 根据考试ID生成二维码
     * @param examinationId examinationId
     * @param response response
     * @author tangyi
     * @date 2020/3/21 5:38 下午
     */
    @ApiOperation(value = "生成二维码(v2)", notes = "生成二维码(v2)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examinationId", value = "考试ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("anonymousUser/generateQrCode/v2/{examinationId}")
    public void produceCodeV2(@PathVariable Long examinationId, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store, no-com.github.tangyi.common.basic.cache");
        response.setContentType("image/jpeg");
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(examinationService.produceCodeV2(examinationId)); ServletOutputStream out = response.getOutputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "PNG", out);
        }
    }
}
