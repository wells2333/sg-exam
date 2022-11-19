package com.github.tangyi.common.vo;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色
 *
 * @author tangyi
 * @date 2018-08-25 13:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVo extends BaseEntity<RoleVo> {

    private String roleName;

    private String roleCode;

    private String roleDesc;

}
