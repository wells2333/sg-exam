package com.github.tangyi.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.PageUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.exam.api.constants.AnswerConstant;
import com.github.tangyi.exam.api.constants.ExamExaminationRecordConstant;
import com.github.tangyi.exam.api.dto.AnswerDto;
import com.github.tangyi.exam.api.dto.StartExamDto;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.Examination;
import com.github.tangyi.exam.api.module.ExaminationRecord;
import com.github.tangyi.exam.api.module.ExaminationSubject;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.mapper.AnswerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final ExamRecordService examRecordService;

    private final ExaminationService examinationService;

    private final ExaminationSubjectService examinationSubjectService;

    /**
     * 查找答题
     *
     * @param answer answer
     * @return Answer
     * @author tangyi
     * @date 2019/1/3 14:27
     */
    @Override
    @Cacheable(value = "answer#" + CommonConstant.CACHE_EXPIRE, key = "#answer.id")
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
    public int deleteAll(Long[] ids) {
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
        answer.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return super.save(answer);
    }

    /**
     * 保存答题，返回下一题信息
     *
     * @param answerDto       answerDto
     * @param type            0：下一题，1：上一题
     * @param nextSubjectId   nextSubjectId
     * @param nextSubjectType 下一题的类型，选择题、判断题
     * @return SubjectDto
     * @author tangyi
     * @date 2019/05/01 11:42
     */
    @Transactional
    public SubjectDto saveAndNext(AnswerDto answerDto, Integer type, Long nextSubjectId, Integer nextSubjectType) {
        String userCode = SysUtil.getUser();
        String sysCode = SysUtil.getSysCode();
        String tenantCode = SysUtil.getTenantCode();
        Answer answer = new Answer();
        BeanUtils.copyProperties(answerDto, answer);
        Answer tempAnswer = this.getAnswer(answer);
        if (tempAnswer != null) {
            tempAnswer.setCommonValue(userCode, sysCode, tenantCode);
            tempAnswer.setAnswer(answer.getAnswer());
            tempAnswer.setType(answer.getType());
            tempAnswer.setEndTime(tempAnswer.getModifyDate());
            this.update(tempAnswer);
        } else {
            answer.setCommonValue(userCode, sysCode, tenantCode);
            answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
            answer.setAnswerType(AnswerConstant.WRONG);
            answer.setEndTime(answer.getModifyDate());
            this.insert(answer);
        }

        // 查询下一题
        return this.subjectAnswer(answerDto.getSubjectId(), answer.getExamRecordId(),
                userCode, sysCode, tenantCode, type, nextSubjectId, nextSubjectType);
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
    public void submit(Answer answer) {
        long start = System.currentTimeMillis();
        String currentUsername = answer.getModifier();
        // 查找已提交的题目
        List<Answer> answerList = findList(answer);
        if (CollectionUtils.isEmpty(answerList))
            return;
        // 成绩
        ExaminationRecord record = new ExaminationRecord();
        // 分类题目
        Map<String, List<Answer>> distinctAnswer = this.distinctAnswer(answerList);
        // TODO 暂时只自动统计选择题，简答题由老师阅卷批改
        if (distinctAnswer.containsKey(SubjectTypeEnum.CHOICES.name())) {
            this.submitChoicesAnswer(distinctAnswer.get(SubjectTypeEnum.CHOICES.name()), record);
        }
        // 如果全部为选择题，则更新状态为统计完成，否则需要阅卷完成后才更改统计状态
        if (!distinctAnswer.containsKey(SubjectTypeEnum.SHORT_ANSWER.name()))
            record.setSubmitStatus(ExamExaminationRecordConstant.STATUS_CALCULATED);
        // 保存成绩
        record.setCommonValue(currentUsername, SysUtil.getSysCode());
        record.setId(answer.getExamRecordId());
        record.setEndTime(record.getCreateDate());
        examRecordService.update(record);
        log.debug("提交答卷，用户名：{}，耗时：{}ms", currentUsername, System.currentTimeMillis() - start);
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
        String currentUsername = SysUtil.getUser();
        String applicationCode = SysUtil.getSysCode();
        String tenantCode = SysUtil.getTenantCode();
        answer.setModifier(currentUsername);
        answer.setApplicationCode(applicationCode);
        answer.setTenantCode(tenantCode);

        ExaminationRecord examRecord = new ExaminationRecord();
        examRecord.setCommonValue(currentUsername, applicationCode, tenantCode);
        examRecord.setId(answer.getExamRecordId());
        // 提交时间
        examRecord.setEndTime(examRecord.getCreateDate());
        examRecord.setSubmitStatus(ExamExaminationRecordConstant.STATUS_SUBMITTED);
        // 1. 发送消息
        amqpTemplate.convertAndSend(MqConstant.SUBMIT_EXAMINATION_QUEUE, answer);
        // 2. 更新考试状态
        boolean success = examRecordService.update(examRecord) > 0;
        log.debug("异步提交答卷成功，提交人：{}，考试ID：{}，耗时：{}ms", currentUsername, examRecord.getExaminationId(),
                System.currentTimeMillis() - start);
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
    public StartExamDto start(ExaminationRecord examRecord) {
        StartExamDto startExamDto = new StartExamDto();
        String currentUsername = SysUtil.getUser();
        String applicationCode = SysUtil.getSysCode();
        String tenantCode = SysUtil.getTenantCode();
        // 创建考试记录
        if (examRecord.getExaminationId() == null)
            throw new CommonException("参数校验失败，考试id为空！");
        if (examRecord.getUserId() == null)
            throw new CommonException("参数校验失败，用户id为空！");
        Examination examination = new Examination();
        examination.setId(examRecord.getExaminationId());
        // 查找考试信息
        examination = examinationService.get(examination);
        examRecord.setCommonValue(currentUsername, applicationCode, tenantCode);
        examRecord.setStartTime(examRecord.getCreateDate());
        // 默认未提交状态
        examRecord.setSubmitStatus(ExamExaminationRecordConstant.STATUS_NOT_SUBMITTED);
        // 保存考试记录
        if (examRecordService.insert(examRecord) > 0) {
            startExamDto.setExamination(examination);
            startExamDto.setExamRecord(examRecord);
            // 根据题目ID，类型获取第一题的详细信息
            SubjectDto subjectDto = subjectService.findFirstSubjectByExaminationId(examRecord.getExaminationId());
            startExamDto.setSubjectDto(subjectDto);
            // 创建第一题的答题
            Answer answer = new Answer();
            answer.setCommonValue(currentUsername, applicationCode, tenantCode);
            answer.setExamRecordId(examRecord.getId());
            answer.setSubjectId(subjectDto.getId());
            // 默认待批改状态
            answer.setMarkStatus(AnswerConstant.TO_BE_MARKED);
            answer.setAnswerType(AnswerConstant.WRONG);
            answer.setStartTime(answer.getCreateDate());
            // 保存答题
            this.save(answer);
            subjectDto.setAnswer(answer);
        }
        return startExamDto;
    }

    /**
     * 查询题目和答题
     *
     * @param subjectId       subjectId
     * @param examRecordId    examRecordId
     * @param userCode        userCode
     * @param sysCode         sysCode
     * @param tenantCode      tenantCode
     * @param nextType        0：下一题，1：上一题
     * @param nextSubjectId   nextSubjectId
     * @param nextSubjectType 下一题的类型，选择题、判断题
     * @return SubjectDto
     * @author tangyi
     * @date 2019/04/30 17:10
     */
    @Transactional
    public SubjectDto subjectAnswer(Long subjectId, Long examRecordId, String userCode, String sysCode,
                                    String tenantCode, Integer nextType, Long nextSubjectId, Integer nextSubjectType) {
        ExaminationRecord examRecord = new ExaminationRecord();
        examRecord.setId(examRecordId);
        // 查找考试记录
        examRecord = examRecordService.get(examRecord);
        if (examRecord == null)
            throw new CommonException("考试记录不存在.");

        // 考试ID，题目ID查找关联关系
        ExaminationSubject examinationSubject = new ExaminationSubject();
        examinationSubject.setExaminationId(examRecord.getExaminationId());
        examinationSubject.setSubjectId(subjectId);
        PageInfo<ExaminationSubject> examinationSubjectPageInfo = examinationSubjectService.findPage(
                PageUtil.pageInfo(CommonConstant.PAGE_NUM_DEFAULT, CommonConstant.PAGE_SIZE_DEFAULT, "id",
                        CommonConstant.PAGE_ORDER_DEFAULT), examinationSubject);
        if (CollectionUtils.isEmpty(examinationSubjectPageInfo.getList()))
            throw new CommonException("序号为" + subjectId + "的题目不存在.");

        // 查询下一题
        SubjectDto subject;
        if (nextSubjectId != null) {
            subject = subjectService.get(nextSubjectId, nextSubjectType);
        } else {
            subject = subjectService.getNextByCurrentIdAndType(examRecord.getExaminationId(), subjectId, examinationSubjectPageInfo.getList().get(0).getType(), nextType);
        }
        if (subject == null) {
            log.error("ID为{}的题目下一题不存在.", subjectId);
            return null;
        }

        // 查找答题
        Answer answer = new Answer();
        answer.setSubjectId(subject.getId());
        answer.setExamRecordId(examRecordId);
        Answer userAnswer = this.getAnswer(answer);
        userAnswer = userAnswer == null ? new Answer() : userAnswer;
        // 设置答题
        subject.setAnswer(userAnswer);
        subject.setExaminationRecordId(examRecordId);
        return subject;
    }

    /**
     * 分类答题
     *
     * @param answers answers
     * @return Map
     * @author tangyi
     * @date 2019/06/18 16:32
     */
    private Map<String, List<Answer>> distinctAnswer(List<Answer> answers) {
        Map<String, List<Answer>> distinctMap = new HashMap<>();
        answers.stream().collect(Collectors.groupingBy(Answer::getType, Collectors.toList())).forEach((type, temp) -> {
            // 匹配类型
            SubjectTypeEnum subjectType = SubjectTypeEnum.match(type);
            if (subjectType != null) {
                switch (subjectType) {
                    case CHOICES:
                        distinctMap.put(SubjectTypeEnum.CHOICES.name(), temp);
                        break;
                    case SHORT_ANSWER:
                        distinctMap.put(SubjectTypeEnum.SHORT_ANSWER.name(), temp);
                        break;
                }
            }
        });
        return distinctMap;
    }

    /**
     * 统计选择题
     *
     * @param choicesAnswer choicesAnswer
     * @param record        record
     * @author tangyi
     * @date 2019/06/18 16:39
     */
    private void submitChoicesAnswer(List<Answer> choicesAnswer, ExaminationRecord record) {
        // 查找题目信息
        List<SubjectDto> subjects = subjectService.findListById(SubjectTypeEnum.CHOICES.getValue(), choicesAnswer.stream().map(Answer::getSubjectId).distinct().toArray(Long[]::new));
        // 保存答题正确的题目分数
        List<Integer> rightScore = new ArrayList<>();
        choicesAnswer.forEach(tempAnswer -> {
            // 题目集合
            subjects.stream()
                    // 题目ID、题目答案匹配
                    .filter(tempSubject -> tempSubject.getId().equals(tempAnswer.getSubjectId()) && tempSubject
                            .getAnswer().getAnswer().equalsIgnoreCase(tempAnswer.getAnswer()))
                    // 记录答题正确的成绩
                    .findFirst().ifPresent(right -> {
                rightScore.add(right.getScore());
                tempAnswer.setAnswerType(AnswerConstant.RIGHT);
                tempAnswer.setScore(right.getScore());
                tempAnswer.setMarkStatus(AnswerConstant.MARKED);
            });
        });
        // 统计错题
        choicesAnswer.forEach(tempAnswer -> {
            // 题目集合
            subjects.stream()
                    // 题目ID、题目答案匹配
                    .filter(tempSubject -> tempSubject.getId().equals(tempAnswer.getSubjectId()) && !tempSubject
                            .getAnswer().getAnswer().equalsIgnoreCase(tempAnswer.getAnswer()))
                    // 错题
                    .findFirst().ifPresent(tempSubject -> {
                tempAnswer.setAnswerType(AnswerConstant.WRONG);
                tempAnswer.setScore(0);
                tempAnswer.setMarkStatus(AnswerConstant.MARKED);
            });
        });
        // 记录总分、正确题目数、错误题目数
        record.setScore(rightScore.stream().mapToInt(Integer::valueOf).sum());
        record.setCorrectNumber(rightScore.size());
        record.setInCorrectNumber(choicesAnswer.size() - rightScore.size());
        // 循环更新答题的状态
        choicesAnswer.forEach(this::update);
    }

    /**
     * 答题详情
     *
     * @param recordId         recordId
     * @param currentSubjectId currentSubjectId
     * @param nextSubjectType  nextSubjectType
     * @param nextType         nextType
     * @return AnswerDto
     * @author tangyi
     * @date 2019/06/18 23:05
     */
    public AnswerDto answerInfo(Long recordId, Long currentSubjectId, Integer nextSubjectType, Integer nextType) {
        ExaminationRecord record = new ExaminationRecord();
        record.setId(recordId);
        record = examRecordService.get(record);
        SubjectDto subjectDto;
        // 题目为空，则加载第一题
        if (currentSubjectId == null) {
            subjectDto = subjectService.findFirstSubjectByExaminationId(record.getExaminationId());
        } else {
            ExaminationSubject examinationSubject = new ExaminationSubject();
            examinationSubject.setExaminationId(record.getExaminationId());
            examinationSubject.setSubjectId(currentSubjectId);

            // 查询该考试和指定序号的题目的关联信息
            // 下一题
            if (AnswerConstant.NEXT.equals(nextType)) {
                examinationSubject = examinationSubjectService.getByPreviousId(examinationSubject);
            } else if (AnswerConstant.PREVIOUS.equals(nextType)) {
                // 上一题
                examinationSubject = examinationSubjectService.getPreviousByCurrentId(examinationSubject);
            } else {
                examinationSubject = examinationSubjectService.findByExaminationIdAndSubjectId(examinationSubject);
            }
            if (examinationSubject == null)
                throw new CommonException("ID为" + currentSubjectId + "的题目不存在");
            // 查询题目的详细信息
            subjectDto = subjectService.get(examinationSubject.getSubjectId(), examinationSubject.getType());
        }
        AnswerDto answerDto = new AnswerDto();
        answerDto.setSubject(subjectDto);
        // 查询答题
        Answer answer = new Answer();
        answer.setSubjectId(subjectDto.getId());
        answer.setExamRecordId(recordId);
        Answer userAnswer = this.getAnswer(answer);
        BeanUtils.copyProperties(userAnswer, answerDto);
        return answerDto;
    }

    /**
     * 完成批改
     *
     * @param examRecord examRecord
     * @return Boolean
     * @author tangyi
     * @date 2019/06/19 14:44
     */
    public Boolean completeMarking(ExaminationRecord examRecord) {
        long start = System.currentTimeMillis();
        examRecord = examRecordService.get(examRecord);
        if (examRecord == null)
            throw new CommonException("考试记录不存在.");
        Answer answer = new Answer();
        answer.setExamRecordId(examRecord.getId());
        List<Answer> answers = this.findList(answer);
        if (CollectionUtils.isNotEmpty(answers)) {
            long correctNumber = answers.stream()
                    .filter(tempAnswer -> tempAnswer.getAnswerType().equals(AnswerConstant.RIGHT)).count();
            // 总分
            Integer score = answers.stream().mapToInt(Answer::getScore).sum();
            examRecord.setScore(score);
            examRecord.setSubmitStatus(ExamExaminationRecordConstant.STATUS_CALCULATED);
            examRecord.setCorrectNumber((int) correctNumber);
            examRecord.setInCorrectNumber(answers.size() - examRecord.getCorrectNumber());
            examRecordService.update(examRecord);
            log.debug("批改完成，用户名：{}，考试ID：{}，总分：{}，耗时：{}ms", examRecord.getCreator(), examRecord.getExaminationId(),
                    score, System.currentTimeMillis() - start);
        }
        return Boolean.TRUE;
    }
}
