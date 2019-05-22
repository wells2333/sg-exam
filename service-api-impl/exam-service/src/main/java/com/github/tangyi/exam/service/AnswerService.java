package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.constants.ExamRecordConstant;
import com.github.tangyi.exam.api.constants.SubjectConstant;
import com.github.tangyi.exam.api.dto.StartExamDto;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.*;
import com.github.tangyi.exam.mapper.AnswerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 答题service
 *
 * @author tangyi
 * @date 2018/11/8 21:17
 */
@Slf4j
@AllArgsConstructor
@Service
public class AnswerService extends CrudService<AnswerMapper, Answer> {

    private final AmqpTemplate amqpTemplate;

    private final SubjectService subjectService;

    private final IncorrectAnswerService incorrectAnswerService;

    private final ExamRecordService examRecordService;

    private final ExaminationService examinationService;

    /**
     * 查找答题
     *
     * @param answer answer
     * @return Answer
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Cacheable(value = "answer", key = "#answer.id")
    public Answer get(Answer answer) {
        return super.get(answer);
    }

    /**
     * 根据用户ID、考试ID、考试记录ID、题目ID查找答题
     *
     * @param answer answer
     * @return Answer
     * @author tangyi
     * @date 2019/01/21 19:41
     */
    public Answer getAnswer(Answer answer) {
        return this.dao.getAnswer(answer);
    }

    /**
     * 更新答题
     *
     * @param answer answer
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Transactional
    @CacheEvict(value = "answer", key = "#answer.id")
    public int update(Answer answer) {
        return super.update(answer);
    }

    /**
     * 删除答题
     *
     * @param answer answer
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Transactional
    @CacheEvict(value = "answer", key = "#answer.id")
    public int delete(Answer answer) {
        return super.delete(answer);
    }

    /**
     * 批量删除答题
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Transactional
    @CacheEvict(value = "answer", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }

    /**
     * 保存
     *
     * @param answer answer
     * @return int
     * @author tangyi
     * @date 2019/04/30 18:03
     */
    @Transactional
    public int save(Answer answer) {
        answer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        return super.save(answer);
    }

    /**
     * 保存答题，返回下一题信息
     *
     * @param answer answer
     * @return SubjectDto
     * @author tangyi
     * @date 2019/05/01 11:42
     */
    @Transactional
    public SubjectDto saveAndNext(Answer answer) {
        Answer tempAnswer = this.getAnswer(answer);
        if (tempAnswer != null) {
            tempAnswer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
            tempAnswer.setAnswer(answer.getAnswer());
            tempAnswer.setOptionAnswer(answer.getOptionAnswer());
            this.update(tempAnswer);
        } else {
            answer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
            this.insert(answer);
        }
        return this.subjectAnswer(answer.getSerialNumber(), answer.getExamRecordId(), answer.getUserId());
    }

    /**
     * 提交答卷，自动统计选择题得分
     *
     * @param answer answer
     * @return boolean
     * @author tangyi
     * @date 2018/12/26 14:09
     */
    @Transactional
    public boolean submit(Answer answer) {
        long start = System.currentTimeMillis();
        boolean success = false;
        String currentUsername = answer.getModifier();
        // 查找已提交的题目
        List<Answer> answerList = findList(answer);
        if (CollectionUtils.isNotEmpty(answerList)) {
            Subject subject = new Subject();
            // 获取题目ID，去重，转成字符串数组
            subject.setIds(answerList.stream().map(Answer::getSubjectId).distinct().toArray(String[]::new));
            // 查找题目列表
            List<Subject> subjects = subjectService.findListById(subject);
            if (CollectionUtils.isNotEmpty(subjects)) {
                // 保存答题正确的题目分数
                List<String> rightScore = new ArrayList<>();
                answerList.forEach(tempAnswer -> {
                    // 题目集合
                    subjects.stream()
                            // 选择题
                            .filter(tempSubject -> SubjectConstant.SUBJECT_TYPE_CHOICE.equals(tempSubject.getType()))
                            // 题目ID、题目答案匹配
                            .filter(tempSubject -> tempSubject.getId().equals(tempAnswer.getSubjectId()) && tempSubject.getAnswer().equalsIgnoreCase(tempAnswer.getAnswer()))
                            // 记录答题正确的成绩
                            .findFirst().ifPresent(right -> rightScore.add(right.getScore()));
                });
                // 求和计算总分
                int totalScore = rightScore.stream().mapToInt(Integer::parseInt).sum();
                // 错题列表
                List<IncorrectAnswer> incorrectAnswers = new ArrayList<>();
                answerList.forEach(tempAnswer -> {
                    // 题目集合
                    subjects.stream()
                            // 选择题
                            .filter(tempSubject -> SubjectConstant.SUBJECT_TYPE_CHOICE.equals(tempSubject.getType()))
                            // 题目ID、题目答案匹配
                            .filter(tempSubject -> tempSubject.getId().equals(tempAnswer.getSubjectId()) && !tempSubject.getAnswer().equalsIgnoreCase(tempAnswer.getAnswer()))
                            // 错题
                            .findFirst()
                            .ifPresent(tempSubject -> {
                                // 记录错题
                                IncorrectAnswer incorrectAnswer = new IncorrectAnswer();
                                incorrectAnswer.setCommonValue(currentUsername, SysUtil.getSysCode());
                                incorrectAnswer.setExaminationId(tempAnswer.getExaminationId());
                                incorrectAnswer.setExamRecordId(answer.getExamRecordId());
                                incorrectAnswer.setSubjectId(tempAnswer.getSubjectId());
                                incorrectAnswer.setSerialNumber(tempSubject.getSerialNumber());
                                incorrectAnswer.setUserId(tempAnswer.getUserId());
                                incorrectAnswer.setIncorrectAnswer(tempAnswer.getAnswer());
                                incorrectAnswers.add(incorrectAnswer);
                            });
                });
                // 保存成绩
                ExamRecord examRecord = new ExamRecord();
                examRecord.setCommonValue(currentUsername, SysUtil.getSysCode());
                examRecord.setId(answer.getExamRecordId());
                examRecord.setEndTime(examRecord.getCreateDate());
                examRecord.setScore(Integer.toString(totalScore));
                examRecord.setCorrectNumber(String.valueOf(rightScore.size()));
                examRecord.setInCorrectNumber(String.valueOf(incorrectAnswers.size()));
                // 如果全部为选择题，则更新状态为统计完成，否则需要阅卷完成后才更改统计状态
                if (subjects.stream().noneMatch(tempSubject -> SubjectConstant.SUBJECT_TYPE_QAS.equals(tempSubject.getType())))
                    examRecord.setSubmitStatus(ExamRecordConstant.STATUS_CALCULATED);
                success = examRecordService.update(examRecord) > 0;
                // 保存错题
                ExamRecord searchExamRecord = new ExamRecord();
                searchExamRecord.setUserId(answer.getUserId());
                searchExamRecord.setExaminationId(answer.getExaminationId());
                searchExamRecord.setId(answer.getExamRecordId());
                // 先删除之前的错题
                incorrectAnswerService.deleteByExaminationRecord(searchExamRecord);
                if (CollectionUtils.isNotEmpty(incorrectAnswers)) {
                    // 批量插入
                    incorrectAnswerService.insertBatch(incorrectAnswers);
                }
            }
        }
        log.debug("提交答卷，用户名：{}，考试ID：{}，耗时：{}ms", currentUsername, answer.getExaminationId(), System.currentTimeMillis() - start);
        return success;
    }

    /**
     * 通过mq异步处理
     * 1. 先发送消息
     * 2. 发送消息成功，更新提交状态，发送失败，返回提交失败
     * 3. 消费消息，计算成绩
     *
     * @param answer answer
     * @return boolean
     * @author tangyi
     * @date 2019/05/03 14:35
     */
    @Transactional
    public boolean submitAsync(Answer answer) {
        long start = System.currentTimeMillis();
        String currentUsername = SecurityUtil.getCurrentUsername();
        answer.setModifier(currentUsername);
        ExamRecord examRecord = new ExamRecord();
        examRecord.setCommonValue(currentUsername, SysUtil.getSysCode());
        examRecord.setId(answer.getExamRecordId());
        // 提交时间
        examRecord.setEndTime(examRecord.getCreateDate());
        examRecord.setSubmitStatus(ExamRecordConstant.STATUS_SUBMITTED);
        // 1. 发送消息
        amqpTemplate.convertAndSend(MqConstant.SUBMIT_EXAMINATION_QUEUE, answer);
        // 2. 更新考试状态
        boolean success = examRecordService.update(examRecord) > 0;
        log.debug("异步提交答卷成功，提交人：{}，考试ID：{}，耗时：{}ms", currentUsername, answer.getExaminationId(), System.currentTimeMillis() - start);
        return success;
    }

    /**
     * 开始考试
     *
     * @param examRecord examRecord
     * @return StartExamDto
     * @author tangyi
     * @date 2019/04/30 23:06
     */
    @Transactional
    public StartExamDto start(ExamRecord examRecord) {
        StartExamDto startExamDto = new StartExamDto();
        String currentUsername = SecurityUtil.getCurrentUsername(), applicationCode = SysUtil.getSysCode();
        // 创建考试记录
        if (StringUtils.isEmpty(examRecord.getExaminationId()))
            throw new CommonException("参数校验失败，考试id为空！");
        if (StringUtils.isEmpty(examRecord.getUserId()))
            throw new CommonException("参数校验失败，用户id为空！");
        Examination examination = new Examination();
        examination.setId(examRecord.getExaminationId());
        // 查找考试信息
        examination = examinationService.get(examination);
        if (examination != null) {
            examRecord.setExaminationName(examination.getExaminationName());
            examRecord.setCourseId(examination.getCourseId());
        }
        examRecord.setCommonValue(currentUsername, applicationCode);
        examRecord.setStartTime(examRecord.getCreateDate());
        // 默认未提交状态
        examRecord.setSubmitStatus(ExamRecordConstant.STATUS_NOT_SUBMITTED);
        // 保存考试记录
        if (examRecordService.insert(examRecord) > 0) {
            startExamDto.setExamination(examination);
            startExamDto.setExamRecord(examRecord);

            // 封装dto
            SubjectDto subjectDto = new SubjectDto();
            startExamDto.setSubjectDto(subjectDto);

            // 查找第一题
            Subject subject = new Subject();
            subject.setExaminationId(examRecord.getExaminationId());
            subject.setSerialNumber(SubjectConstant.DEFAULT_SERIAL_NUMBER);
            subject = subjectService.getByExaminationIdAndSerialNumber(subject);
            if (subject == null)
                throw new CommonException("没有第一题");
            // 获取题目信息
            BeanUtils.copyProperties(subject, subjectDto);
            // 创建第一题的答题
            Answer answer = new Answer();
            answer.setCommonValue(currentUsername, applicationCode);
            answer.setUserId(examRecord.getUserId());
            answer.setExaminationId(examRecord.getExaminationId());
            answer.setExamRecordId(examRecord.getId());
            answer.setSubjectId(subject.getId());
            // 默认题号为1
            answer.setSerialNumber(subject.getSerialNumber());
            // 保存答题
            this.save(answer);
            subjectDto.setAnswer(answer);
        }
        return startExamDto;
    }

    /**
     * 查询题目和答题
     *
     * @param serialNumber serialNumber
     * @param examRecordId examRecordId
     * @param userId       userId
     * @return SubjectDto
     * @author tangyi
     * @date 2019/04/30 17:10
     */
    @Transactional
    public SubjectDto subjectAnswer(String serialNumber, String examRecordId, String userId) {
        SubjectDto subjectDto = null;
        ExamRecord examRecord = new ExamRecord();
        examRecord.setId(examRecordId);
        // 查找考试记录
        examRecord = examRecordService.get(examRecord);
        if (examRecord != null) {
            // 查找题目
            Subject subject = new Subject();
            subject.setExaminationId(examRecord.getExaminationId());
            subject.setSerialNumber(serialNumber);
            subject = subjectService.getByExaminationIdAndSerialNumber(subject);
            if (subject != null) {
                subjectDto = new SubjectDto();
                // 查找答题
                Answer answer = new Answer();
                answer.setSubjectId(subject.getId());
                answer.setExaminationId(examRecord.getExaminationId());
                answer.setExamRecordId(examRecordId);
                answer.setUserId(userId);
                Answer userAnswer = this.getAnswer(answer);
                // 设置答题
                if (userAnswer != null)
                    subjectDto.setAnswer(userAnswer);
                BeanUtils.copyProperties(subject, subjectDto);
                subjectDto.setExaminationRecordId(examRecordId);
            }
        }
        return subjectDto;
    }
}
