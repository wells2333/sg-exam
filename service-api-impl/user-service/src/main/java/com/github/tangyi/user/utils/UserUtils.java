package com.github.tangyi.user.utils;

import java.util.LinkedHashMap;

/**
 * 用户工具类
 *
 * @author tangyi
 * @date 2018/11/26 22:32
 */
public class UserUtils {

    /**
     * 获取User属性的map
     *
     * @return LinkedHashMap
     * @author tangyi
     * @date 2018/11/26 22:35
     */
    public static LinkedHashMap<String, String> getUserMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "用户id");
        map.put("name", "账号");
        map.put("username", "姓名");
        map.put("phone", "联系电话");
        map.put("email", "邮箱");
        map.put("born", "出生日期");
        map.put("remark", "备注");
        map.put("status", "状态");
        map.put("deptId", "部门ID");
        map.put("creator", "创建人");
        map.put("createDate", "创建时间");
        map.put("modifier", "修改人");
        map.put("modifyDate", "修改时间");
        map.put("delFlag", "删除标记");
        map.put("applicationCode", "系统编码");
        return map;
    }
}
