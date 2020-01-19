package com.github.tangyi.exam.utils;

import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.SubjectChoices;
import com.github.tangyi.exam.api.module.SubjectShortAnswer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.excel.model.SubjectExcelModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目工具类
 *
 * @author tangyi
 * @version V1.0
 * @date 2018-11-28 12:56
 */
public class SubjectUtil {

    private SubjectUtil() {
    }

    /**
     * 转换对象
     *
     * @param subjectDtoList subjectDtoList
     * @return List
     */
    public static List<SubjectExcelModel> convertToExcelModel(List<SubjectDto> subjectDtoList) {
        List<SubjectExcelModel> subjectExcelModels = new ArrayList<>(subjectDtoList.size());
        subjectDtoList.forEach(subject -> {
            SubjectExcelModel subjectExcelModel = new SubjectExcelModel();
            BeanUtils.copyProperties(subject, subjectExcelModel);
            if (CollectionUtils.isNotEmpty(subject.getOptions())) {
                List<String> optionString = subject.getOptions().stream()
                        .map(option -> "$$" + option.getOptionName() + "# " + option.getOptionContent()).collect(Collectors.toList());
                subjectExcelModel.setOptions(StringUtils.join(optionString, "\n"));
            }
            subjectExcelModel.setAnswer(subject.getAnswer().getAnswer());
            subjectExcelModels.add(subjectExcelModel);
        });
        return subjectExcelModels;
    }

    /**
     * SubjectChoices转SubjectDto
     *
     * @param subjectChoice subjectChoice
     * @return List
     * @author tangyi
     * @date 2019/06/16 16:50
     */
    public static SubjectDto subjectChoicesToDto(SubjectChoices subjectChoice) {
        if (subjectChoice == null)
            return null;
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subjectChoice.getId());
        subjectDto.setSubjectName(subjectChoice.getSubjectName());
        subjectDto.setScore(subjectChoice.getScore());
        subjectDto.setAnalysis(subjectChoice.getAnalysis());
        subjectDto.setLevel(subjectChoice.getLevel());
        // 选择题类型匹配
        SubjectTypeEnum subjectTypeEnum = SubjectTypeEnum.matchByValue(subjectChoice.getChoicesType());
        if (subjectTypeEnum != null)
            subjectDto.setType(subjectTypeEnum.getValue());
        subjectDto.setChoicesType(subjectChoice.getChoicesType());
        subjectDto.setOptions(subjectChoice.getOptions());
        subjectDto.setCreator(subjectChoice.getCreator());
        subjectDto.setModifier(subjectChoice.getModifier());
        subjectDto.setModifyDate(subjectChoice.getModifyDate());
        // 参考答案
        Answer answer = new Answer();
        answer.setAnswer(subjectChoice.getAnswer());
        subjectDto.setAnswer(answer);
        return subjectDto;
    }

    /**
     * SubjectChoices转SubjectDto
     *
     * @param subjectChoices subjectChoices
     * @return List
     * @author tangyi
     * @date 2019/06/16 16:50
     */
    public static List<SubjectDto> subjectChoicesToDto(List<SubjectChoices> subjectChoices) {
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subjectChoices)) {
            subjectDtoList = subjectChoices.stream().map(SubjectUtil::subjectChoicesToDto).collect(Collectors.toList());
        }
        return subjectDtoList;
    }

    /**
     * SubjectShortAnswer转SubjectDto
     *
     * @param subjectShortAnswer subjectShortAnswer
     * @return List
     * @author tangyi
     * @date 2019/06/16 16:59
     */
    public static SubjectDto subjectShortAnswerToDto(SubjectShortAnswer subjectShortAnswer) {
        if (subjectShortAnswer == null)
            return null;
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subjectShortAnswer.getId());
        subjectDto.setSubjectName(subjectShortAnswer.getSubjectName());
        subjectDto.setScore(subjectShortAnswer.getScore());
        subjectDto.setAnalysis(subjectShortAnswer.getAnalysis());
        subjectDto.setLevel(subjectShortAnswer.getLevel());
        subjectDto.setType(SubjectTypeEnum.SHORT_ANSWER.getValue());
        subjectDto.setCreator(subjectShortAnswer.getCreator());
        subjectDto.setModifyDate(subjectShortAnswer.getModifyDate());
        // 题目类型
        subjectDto.setType(SubjectTypeEnum.SHORT_ANSWER.getValue());

        // 参考答案
        Answer answer = new Answer();
        answer.setAnswer(subjectShortAnswer.getAnswer());
        subjectDto.setAnswer(answer);
        return subjectDto;
    }

    /**
     * SubjectShortAnswer转SubjectDto
     *
     * @param subjectShortAnswers subjectShortAnswers
     * @return List
     * @author tangyi
     * @date 2019/06/16 16:59
     */
    public static List<SubjectDto> subjectShortAnswerToDto(List<SubjectShortAnswer> subjectShortAnswers) {
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subjectShortAnswers)) {
            subjectDtoList = subjectShortAnswers.stream().map(SubjectUtil::subjectShortAnswerToDto)
                    .collect(Collectors.toList());
        }
        return subjectDtoList;
    }
}
