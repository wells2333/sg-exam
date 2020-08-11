package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.module.Course;
import com.github.tangyi.api.exam.module.Examination;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author tangyi
 * @date 2018/11/20 22:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ExaminationDto extends Examination {

    private Course course;

    /**
     * 封面地址
     */
    private String logoUrl;

}
