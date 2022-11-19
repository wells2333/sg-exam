package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author tangyi
 * @date 2019/4/30 16:54
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StartExamDto implements Serializable {

    @Serial
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

	private Integer total;

	/**
	 * 答题卡
	 */
	private List<CardDto> cards;
}
