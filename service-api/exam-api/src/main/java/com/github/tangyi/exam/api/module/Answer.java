package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * 答题
 *
 * @author tangyi
 * @date 2018/11/8 20:59
 */
public class Answer extends BaseEntity<Answer> {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 考试ID
     */
    private String examinationId;

    /**
     * 考试记录id
     */
    private String examRecordId;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 题目ID
     */
    private String subjectId;

    /**
     * 答案
     */
    private String answer;

    /**
     * 选择题答案
     */
    private String optionAnswer;

    /**
     * 下一题题目序号
     */
    private String serialNumber;

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExamRecordId() {
        return examRecordId;
    }

    public void setExamRecordId(String examRecordId) {
        this.examRecordId = examRecordId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOptionAnswer() {
        return optionAnswer;
    }

    public void setOptionAnswer(String optionAnswer) {
        this.optionAnswer = optionAnswer;
    }
}
