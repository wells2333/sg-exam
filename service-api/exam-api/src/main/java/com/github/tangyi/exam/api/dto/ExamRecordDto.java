package com.github.tangyi.exam.api.dto;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * 考试记录DTO
 *
 * @author tangyi
 * @date 2018-12-26 16:26
 */
public class ExamRecordDto extends BaseEntity<ExamRecordDto> {

    /**
     * 考生ID
     */
    private String userId;

    /**
     * 考试名称
     */
    private String examinationName;

    /**
     * 考试类型
     */
    private String type;

    /**
     * 考试注意事项
     */
    private String attention;

    /**
     * 当前时间
     */
    private String currentTime;

    /**
     * 考试开始时间
     */
    private String startTime;

    /**
     * 考试结束时间
     */
    private String endTime;

    /**
     * 考试持续时间
     */
    private String duration;

    /**
     * 总分
     */
    private String totalScore;

    /**
     * 分数
     */
    private String score;

    /**
     * 考试状态
     */
    private String status;

    /**
     * 封面
     */
    private String avatar;

    /**
     * 学院
     */
    private String collegeId;

    /**
     * 专业
     */
    private String majorId;

    /**
     * 课程
     */
    private String courseId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 考生名称
     */
    private String userName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 考试时间
     */
    private String examTime;

    public String getExaminationName() {
        return examinationName;
    }

    public void setExaminationName(String examinationName) {
        this.examinationName = examinationName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }
}
