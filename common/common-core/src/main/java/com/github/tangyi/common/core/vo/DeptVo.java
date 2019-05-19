package com.github.tangyi.common.core.vo;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 部门vo
 *
 * @author tangyi
 * @date 2018/12/31 22:02
 */
@Data
public class DeptVo extends BaseEntity<DeptVo> {

    private String deptName;
}
