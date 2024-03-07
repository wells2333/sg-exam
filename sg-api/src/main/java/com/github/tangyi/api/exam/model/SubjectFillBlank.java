package com.github.tangyi.api.exam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 填空题
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_fill_blank")
public class SubjectFillBlank extends BaseSubject<SubjectFillBlank> {

}
