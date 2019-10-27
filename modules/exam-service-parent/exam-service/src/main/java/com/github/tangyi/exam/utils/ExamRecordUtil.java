package com.github.tangyi.exam.utils;

import com.github.tangyi.common.core.utils.DateUtils;

import java.util.Date;
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
        map.put("submitStatusName", "批改状态");
        map.put("startTime", "开始时间");
        map.put("endTime", "结束时间");
        map.put("duration", "持续时间");
        map.put("score", "成绩");
        map.put("correctNumber", "正确题数");
        map.put("inCorrectNumber", "错误题数");
        return map;
    }

    /**
     * 计算持续时间
     * @param startTime startTime
     * @param endTime endTime
     * @return String
     */
    public static String getExamDuration(Date startTime, Date endTime) {
        // 持续时间
        String suffix = "分钟";
        Integer duration = DateUtils.getBetweenMinutes(startTime, endTime);
        if (duration <= 0) {
            duration = DateUtils.getBetweenSecond(startTime, endTime);
            suffix = "秒";
        }
        return duration + suffix;
    }
}
