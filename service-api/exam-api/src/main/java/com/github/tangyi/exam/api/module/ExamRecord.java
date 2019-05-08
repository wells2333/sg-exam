package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * 考试记录
 *
 * @author tangyi
 * @date 2018/11/8 21:05
 */
public class ExamRecord extends BaseEntity<ExamRecord> {

    /**
     * 考生ID
     */
    private String userId;

    /**
     * 考试ID
     */
    private String examinationId;

    /**
     * 考试名称
     */
    private String examinationName;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 成绩
     */
    private String score;

    /**
     * 错误题目数量
     */
    private String inCorrectNumber;

    /**
     * 正确题目数量
     */
    private String correctNumber;

    /**
     * 提交状态 1-已提交 0-未提交
     */
    private String submitStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(String examinationId) {
        this.examinationId = examinationId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getExaminationName() {
        return examinationName;
    }

    public void setExaminationName(String examinationName) {
        this.examinationName = examinationName;
    }

    public String getInCorrectNumber() {
        return inCorrectNumber;
    }

    public void setInCorrectNumber(String inCorrectNumber) {
        this.inCorrectNumber = inCorrectNumber;
    }

    public String getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(String correctNumber) {
        this.correctNumber = correctNumber;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }
}
