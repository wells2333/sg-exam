package com.github.tangyi.exam.api.dto;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * @author tangyi
 * @date 2019/1/9 21:09
 */
@Data
public class SubjectBankDto extends BaseEntity<SubjectBankDto> {

    /**
     * 分类ID
     */
    private String categoryId;
}
