package com.github.tangyi.common.vo;

import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogVo extends BaseEntity<LogVo> {

    private Log log;

    private String username;
}
