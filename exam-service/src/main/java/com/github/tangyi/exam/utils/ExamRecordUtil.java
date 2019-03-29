package com.github.tangyi.exam.utils;

import java.util.LinkedHashMap;

/**
 * 考试记录工具类
 *
 * @author tangyi
 * @date 2018/12/31 22:35
 */
public class ExamRecordUtil {

    /**
     * 获取ExamRecordDto属性的map
     *
     * @return LinkedHashMap
     * @author tangyi
     * @date 2018/12/31 22:35
     */
    public static LinkedHashMap<String, String> getExamRecordDtoMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "成绩id");
        map.put("examinationName", "考试名称");
        map.put("userName", "考生名称");
        map.put("deptName", "部门名称");
        map.put("score", "成绩");
        map.put("examTime", "考试时间");
        return map;
    }
}
