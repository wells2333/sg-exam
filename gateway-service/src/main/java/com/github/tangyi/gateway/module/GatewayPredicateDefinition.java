package com.github.tangyi.gateway.module;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 断言信息
 *
 * @author tangyi
 * @date 2019/3/27 11:08
 */
public class GatewayPredicateDefinition {

    /**
     * 断言对应的Name
     */
    private String name;

    /**
     * 配置的断言规则
     */
    private Map<String, String> args = new LinkedHashMap<String, String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
