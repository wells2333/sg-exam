package com.github.tangyi.exam.utils;

import java.util.LinkedHashMap;

/**
 * 题目工具类
 *
 * @author tangyi
 * @version V1.0
 * @date 2018/12/9 14:18
 */
public class SubjectBankUtil {

    /**
     * 获取SubjectBank属性的map
     *
     * @return LinkedHashMap
     * @author tangyi
     * @date 2018/12/9 14:18
     */
    public static LinkedHashMap<String, String> getSubjectBankMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "题目ID");
        map.put("categoryId", "分类ID");
        map.put("subjectName", "题目名称");
        map.put("type", "题目类型");
        map.put("content", "题目内容");
        map.put("optionA", "选项A");
        map.put("optionB", "选项B");
        map.put("optionC", "选项C");
        map.put("optionD", "选项D");
        map.put("optionE", "选项E");
        map.put("optionF", "选项F");
        map.put("answer", "参考答案");
        map.put("score", "分值");
        map.put("analysis", "解析");
        map.put("level", "难度等级");
        map.put("creator", "创建人");
        map.put("createDate", "创建时间");
        map.put("modifier", "修改人");
        map.put("modifyDate", "修改时间");
        map.put("delFlag", "删除标记");
        map.put("applicationCode", "系统编码");
        return map;
    }
}
