package com.github.tangyi.api.exam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

/**
 * 判断题
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_judgement")
public class SubjectJudgement extends BaseSubject<SubjectJudgement> {

}
