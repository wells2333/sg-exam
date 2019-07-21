package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.exam.api.constants.ExamSubjectConstant;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.ExaminationSubject;
import com.github.tangyi.exam.api.module.SubjectChoices;
import com.github.tangyi.exam.api.module.SubjectShortAnswer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.utils.SubjectUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 题目service
 *
 * @author tangyi
 * @date 2019/6/16 17:22
 */
@AllArgsConstructor
@Slf4j
@Service
public class SubjectService {

    private final SubjectChoicesService subjectChoicesService;

    private final SubjectShortAnswerService subjectShortAnswerService;

    private final ExaminationSubjectService examinationSubjectService;

    private final SubjectOptionService subjectOptionService;

    private final SubjectJudgementService subjectJudgementService;

    /**
     * 根据题目ID，题目类型查询题目信息
     *
     * @param id   id
     * @param type type
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/16 17:24
     */
    public SubjectDto get(String id, Integer type) {
        return subjectService(type).getSubject(id);
    }

    /**
     * 根据题目ID查询题目信息
     *
     * @param id id
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/16 17:26
     */
    public SubjectDto get(String id) {
        Integer type = SubjectTypeEnum.CHOICES.getValue();
        ExaminationSubject es = new ExaminationSubject();
        es.setSubjectId(id);
        List<ExaminationSubject> examinationSubjects = examinationSubjectService.findListBySubjectId(es);
        if (CollectionUtils.isNotEmpty(examinationSubjects)) {
            type = examinationSubjects.get(0).getType();
        }
        return subjectService(type).getSubject(id);
    }

    /**
     * 根据题目序号查找
     *
     * @param serialNumber serialNumber
     * @param type         type
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/18 13:49
     */
    public SubjectDto getBySerialNumber(Integer serialNumber, Integer type) {
        return subjectService(type).getSubjectBySerialNumber(serialNumber);
    }

    /**
     * 查询列表
     *
     * @param subjectDto subjectDto
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/16 18:12
     */
    public List<SubjectDto> findList(SubjectDto subjectDto) {
        // 先查询题目考试关联表
        ExaminationSubject examinationSubject = new ExaminationSubject();
        examinationSubject.setExaminationId(subjectDto.getExaminationId());
        List<ExaminationSubject> examinationSubjects = examinationSubjectService.findList(examinationSubject);
        return this.findSubjectDtoList(examinationSubjects, true);
    }

    /**
     * 查询具体类型的题目列表
     *
     * @param subjectDto subjectDto
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/17 17:12
     */
    public List<SubjectDto> findListByType(SubjectDto subjectDto) {
        List<SubjectDto> subjectDtos = subjectService(subjectDto.getType()).findSubjectList(subjectDto);
        // 选择题则查找具体的选项
        if (SubjectTypeEnum.CHOICES.getValue().equals(subjectDto.getType())) {
            if (CollectionUtils.isNotEmpty(subjectDtos)) {
                // 查找选项信息
                subjectDtos = subjectDtos.stream().map(dto -> {
                    SubjectChoices subjectChoices = new SubjectChoices();
                    subjectChoices.setId(dto.getId());
                    subjectChoices = subjectChoicesService.get(subjectChoices);
                    return SubjectUtil.subjectChoicesToDto(subjectChoices);
                }).collect(Collectors.toList());
            }
        }
        return subjectDtos;
    }

    /**
     * 查询分页列表
     *
     * @param pageInfo   pageInfo
     * @param subjectDto subjectDto
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/16 18:12
     */
    public PageInfo<SubjectDto> findPage(PageInfo pageInfo, SubjectDto subjectDto) {
        return subjectService(subjectDto.getType()).findSubjectPage(pageInfo, subjectDto);
    }

    /**
     * 查询列表
     *
     * @param subjectDto subjectDto
     * @return SubjectDto
     * @author tangyi
     * @date 2019/06/16 18:14
     */
    public List<SubjectDto> findListById(SubjectDto subjectDto) {
        return subjectService(subjectDto.getType()).findSubjectListById(subjectDto);
    }

    /**
     * 保存
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 17:59
     */
    @Transactional
    public int insert(SubjectDto subjectDto) {
        // 保存与考试的关联关系
        if (StringUtils.isNotBlank(subjectDto.getExaminationId())) {
            ExaminationSubject examinationSubject = new ExaminationSubject();
            examinationSubject.setCommonValue(subjectDto.getCreator(), subjectDto.getApplicationCode(), subjectDto.getTenantCode());
            examinationSubject.setExaminationId(subjectDto.getExaminationId());
            examinationSubject.setSubjectId(subjectDto.getId());
            examinationSubject.setType(subjectDto.getType());
            examinationSubject.setSerialNumber(subjectDto.getSerialNumber());
            examinationSubjectService.insert(examinationSubject);
        }
        // 保存选项
        if (CollectionUtils.isNotEmpty(subjectDto.getOptions())) {
            // 初始化基本属性
            subjectDto.getOptions().forEach(subjectOption -> {
                subjectOption.setCommonValue(subjectDto.getCreator(), subjectDto.getApplicationCode(), subjectDto.getTenantCode());
                subjectOption.setSubjectChoicesId(subjectDto.getId());
            });
            // 批量保存
            subjectOptionService.insertBatch(subjectDto.getOptions());
        }
        return subjectService(subjectDto.getType()).insertSubject(subjectDto);
    }

    /**
     * 更新
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 18:01
     */
    @Transactional
    public int update(SubjectDto subjectDto) {
        return subjectService(subjectDto.getType()).updateSubject(subjectDto);
    }

    /**
     * 删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 18:04
     */
    @Transactional
    public int delete(SubjectDto subjectDto) {
        return subjectService(subjectDto.getType()).deleteSubject(subjectDto);
    }

    /**
     * 物理删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:51
     */
    @Transactional
    public int physicalDelete(SubjectDto subjectDto) {
        return subjectService(subjectDto.getType()).physicalDeleteSubject(subjectDto);
    }

    /**
     * 批量删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 18:04
     */
    @Transactional
    public int deleteAll(SubjectDto subjectDto) {
        return subjectService(subjectDto.getType()).deleteAllSubject(subjectDto);
    }

    /**
     * 物理批量删除
     *
     * @param subjectDto subjectDto
     * @return int
     * @author tangyi
     * @date 2019/06/16 22:52
     */
    @Transactional
    public int physicalDeleteAll(SubjectDto subjectDto) {
        return subjectService(subjectDto.getType()).physicalDeleteAllSubject(subjectDto);
    }

    /**
     * 根据题目类型返回对应的BaseSubjectService
     *
     * @param type type
     * @return BaseSubjectService
     * @author tangyi
     * @date 2019/06/16 17:34
     */
    private BaseSubjectService subjectService(Integer type) {
        BaseSubjectService baseSubjectService = this.subjectChoicesService;
        // 匹配类型
        SubjectTypeEnum subjectType = SubjectTypeEnum.match(type);
        if (subjectType != null) {
            switch (subjectType) {
                case CHOICES:
                case MULTIPLE_CHOICES:
                    baseSubjectService = this.subjectChoicesService;
                    break;
                case SHORT_ANSWER:
                    baseSubjectService = this.subjectShortAnswerService;
                    break;
                case JUDGEMENT:
                    baseSubjectService = this.subjectJudgementService;
                    break;
            }
        }
        return baseSubjectService;
    }

    /**
     * 导入题目
     *
     * @param subjects      subjects
     * @param examinationId examinationId
     * @param categoryId    categoryId
     * @return int
     * @author tangyi
     * @date 2019/06/17 14:39
     */
    @Transactional
    public int importSubject(List<SubjectDto> subjects, String examinationId, String categoryId) {
        int updated = 0;
        String creator = SysUtil.getUser(), sysCode = SysUtil.getSysCode(), tenantCode = SysUtil.getTenantCode();
        // 暂时循环遍历保存
        for (SubjectDto subject : subjects) {
            if (StringUtils.isNotBlank(examinationId))
                subject.setExaminationId(examinationId);
            if (StringUtils.isBlank(categoryId))
                categoryId = ExamSubjectConstant.DEFAULT_CATEGORY_ID.toString();
            subject.setCategoryId(categoryId);
            if (StringUtils.isBlank(subject.getId())) {
                subject.setCommonValue(creator, sysCode, tenantCode);
                updated += this.insert(subject);
            } else {
                subject.setCommonValue(creator, sysCode, tenantCode);
                updated += this.update(subject);
            }
        }
        return updated;
    }

    /**
     * 遍历关系集合，按类型分组题目ID，返回map
     *
     * @param examinationSubjects examinationSubjects
     * @return Map
     * @author tangyi
     * @date 2019/06/17 10:43
     */
    public Map<String, String[]> getSubjectIdByType(List<ExaminationSubject> examinationSubjects) {
        Map<String, String[]> idMap = new HashMap<>();
        examinationSubjects.stream()
                .collect(Collectors.groupingBy(ExaminationSubject::getType, Collectors.toList()))
                .forEach((type, temp) -> {
                    // 匹配类型
                    SubjectTypeEnum subjectType = SubjectTypeEnum.match(type);
                    if (subjectType != null) {
                        switch (subjectType) {
                            case CHOICES:
                                idMap.put(SubjectTypeEnum.CHOICES.name(), temp.stream().map(ExaminationSubject::getSubjectId).distinct().toArray(String[]::new));
                                break;
                            case SHORT_ANSWER:
                                idMap.put(SubjectTypeEnum.SHORT_ANSWER.name(), temp.stream().map(ExaminationSubject::getSubjectId).distinct().toArray(String[]::new));
                                break;
                        }
                    }
                });
        return idMap;
    }

    /**
     * 根据关系列表查询对应的题目的详细信息
     *
     * @param examinationSubjects examinationSubjects
     * @return List
     * @author tangyi
     * @date 2019/06/17 10:54
     */
    public List<SubjectDto> findSubjectDtoList(List<ExaminationSubject> examinationSubjects) {
        return findSubjectDtoList(examinationSubjects, false);
    }

    /**
     * 根据关系列表查询对应的题目的详细信息
     *
     * @param examinationSubjects examinationSubjects
     * @return List
     * @author tangyi
     * @date 2019/06/17 11:54
     */
    public List<SubjectDto> findSubjectDtoList(List<ExaminationSubject> examinationSubjects, boolean findOptions) {
        Map<String, String[]> idMap = this.getSubjectIdByType(examinationSubjects);
        // 查询题目信息，聚合
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        if (idMap.containsKey(SubjectTypeEnum.CHOICES.name())) {
            SubjectChoices subjectChoices = new SubjectChoices();
            subjectChoices.setIds(idMap.get(SubjectTypeEnum.CHOICES.name()));
            List<SubjectChoices> subjectChoicesList = subjectChoicesService.findListById(subjectChoices);
            if (CollectionUtils.isNotEmpty(subjectChoicesList)) {
                // 查找选项信息
                if (findOptions) {
                    subjectChoicesList = subjectChoicesList.stream().map(subjectChoicesService::get).collect(Collectors.toList());
                }
                subjectDtoList.addAll(SubjectUtil.subjectChoicesToDto(subjectChoicesList));
            }
        }
        if (idMap.containsKey(SubjectTypeEnum.SHORT_ANSWER.name())) {
            SubjectShortAnswer subjectShortAnswer = new SubjectShortAnswer();
            subjectShortAnswer.setIds(idMap.get(SubjectTypeEnum.SHORT_ANSWER.name()));
            List<SubjectShortAnswer> subjectShortAnswers = subjectShortAnswerService.findListById(subjectShortAnswer);
            if (CollectionUtils.isNotEmpty(subjectShortAnswers)) {
                subjectDtoList.addAll(SubjectUtil.subjectShortAnswerToDto(subjectShortAnswers));
            }
        }
        return subjectDtoList;
    }
}
