package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tangyi.api.exam.module.Examination;
import com.github.tangyi.api.exam.module.ExaminationRecord;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tangyi
 * @date 2019/4/30 16:54
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StartExamDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考试记录信息
     */
    private ExaminationRecord examRecord;

    /**
     * 考试信息
     */
    private Examination examination;

    /**
     * 题目信息
     */
    private SubjectDto subjectDto;
}
