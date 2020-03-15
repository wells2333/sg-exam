package com.github.tangyi.common.basic.vo;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 角色
 *
 * @author tangyi
 * @date 2018-08-25 13:58
 */
@Data
public class RoleVo extends BaseEntity<RoleVo> {

    private String roleName;

    private String roleCode;

    private String roleDesc;

}
