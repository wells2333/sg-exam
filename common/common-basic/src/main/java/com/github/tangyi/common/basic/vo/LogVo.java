package com.github.tangyi.common.basic.vo;

import com.github.tangyi.common.basic.model.Log;
import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * logVo
 *
 * @author tangyi
 * @date 2019-01-05 17:07
 */
@Data
public class LogVo extends BaseEntity<LogVo> {

    private Log log;

    private String username;
}
