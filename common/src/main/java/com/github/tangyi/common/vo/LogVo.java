package com.github.tangyi.common.vo;

import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * logVo
 *
 * @author tangyi
 * @date 2019-01-05 17:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogVo extends BaseEntity<LogVo> {

    private Log log;

    private String username;
}
