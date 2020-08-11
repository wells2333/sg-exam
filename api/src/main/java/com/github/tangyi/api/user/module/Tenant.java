package com.github.tangyi.api.user.module;

import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 租户
 *
 * @author tangyi
 * @date 2019/5/22 22:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Tenant extends BaseEntity<Tenant> {

    /**
     * 租户标识
     */
    @NotBlank(message = "租户标识不能为空")
    private String tenantCode;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    private String tenantName;

    /**
     * 租户描述信息
     */
    private String tenantDesc;

    /**
     * 状态，0-待审核，1-正常，2-审核不通过
     */
    private Integer status;
}
