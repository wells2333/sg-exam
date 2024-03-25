package com.github.tangyi.api.exam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

/**
 * 材料题目
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_material")
public class SubjectMaterial extends BaseSubject<SubjectMaterial> {

}
