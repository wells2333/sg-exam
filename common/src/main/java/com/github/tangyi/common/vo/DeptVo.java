package com.github.tangyi.common.vo;

import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门vo
 *
 * @author tangyi
 * @date 2018/12/31 22:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeptVo extends BaseEntity<DeptVo> {

    private String deptName;
}
