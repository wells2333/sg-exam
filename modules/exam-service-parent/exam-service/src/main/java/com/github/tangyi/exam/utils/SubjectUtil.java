package com.github.tangyi.exam.utils;

import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.SubjectChoices;
import com.github.tangyi.exam.api.module.SubjectShortAnswer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import org.apache.commons.collections4.CollectionUtils;

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

    /**
     * 获取Subject属性的map
     *
     * @return LinkedHashMap
     * @author tangyi
     * @date 2018/11/28 12:57
     */
    public static LinkedHashMap<String, String> getSubjectMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "题目ID");
        map.put("examinationId", "考试ID");
        map.put("subjectName", "题目");
        map.put("type", "题目类型");
        map.put("options.list#0.optionContent", "选项A");
        map.put("options.list#1.optionContent", "选项B");
        map.put("options.list#2.optionContent", "选项C");
        map.put("options.list#3.optionContent", "选项D");
        map.put("answer.answer", "参考答案");
        map.put("score", "分值");
        map.put("analysis", "解析");
        map.put("level", "难度等级");
        map.put("creator", "创建人");
        return map;
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
        subjectDto.setSerialNumber(subjectChoice.getSerialNumber());
        subjectDto.setSubjectName(subjectChoice.getSubjectName());
        subjectDto.setScore(subjectChoice.getScore());
        subjectDto.setAnalysis(subjectChoice.getAnalysis());
        subjectDto.setLevel(subjectChoice.getLevel());
        // 选择题类型匹配
        SubjectTypeEnum subjectTypeEnum = SubjectTypeEnum.match(subjectChoice.getChoicesType());
        if (subjectTypeEnum != null)
            subjectDto.setType(subjectTypeEnum.getValue());
        subjectDto.setChoicesType(subjectChoice.getChoicesType());
        subjectDto.setOptions(subjectChoice.getOptions());
        subjectDto.setCreator(subjectChoice.getCreator());
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
        subjectDto.setSerialNumber(subjectShortAnswer.getSerialNumber());
        subjectDto.setSubjectName(subjectShortAnswer.getSubjectName());
        subjectDto.setScore(subjectShortAnswer.getScore());
        subjectDto.setAnalysis(subjectShortAnswer.getAnalysis());
        subjectDto.setLevel(subjectShortAnswer.getLevel());
        subjectDto.setType(SubjectTypeEnum.SHORT_ANSWER.getValue());
        subjectDto.setCreator(subjectShortAnswer.getCreator());
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
            subjectDtoList = subjectShortAnswers.stream().map(SubjectUtil::subjectShortAnswerToDto).collect(Collectors.toList());
        }
        return subjectDtoList;
    }
}
