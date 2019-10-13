package com.github.tangyi.gateway.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 断言
 *
 * @author tangyi
 * @date 2019-08-16 17:51
 */
@Data
public class Predicates extends BaseEntity<Predicates> {

    /**
     * 路由ID
     */
    @NotBlank(message = "路由ID不能为空")
    private String routeId;

    /**
     * 断言名称
     */
    @NotBlank(message = "predicates name不能为空")
    private String name;

    /**
     * 断言参数
     */
    private String args;
}
