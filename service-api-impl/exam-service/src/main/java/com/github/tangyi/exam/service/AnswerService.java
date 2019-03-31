package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.mapper.AnswerMapper;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.ExamRecord;
import com.github.tangyi.exam.api.module.IncorrectAnswer;
import com.github.tangyi.exam.api.module.Subject;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 答题service
 *
 * @author tangyi
 * @date 2018/11/8 21:17
 */
@Service
public class AnswerService extends CrudService<AnswerMapper, Answer> {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private IncorrectAnswerService incorrectAnswerService;

    @Autowired
    private ExamRecordService examRecodeService;

    @Autowired
    private ExaminationService examinationService;

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
     * 提交答卷
     *
     * @param answer answer
     * @return boolean
     * @author tangyi
     * @date 2018/12/26 14:09
     */
    @Transactional
    public boolean submit(Answer answer) {
        boolean success = false;
        logger.debug("提交答卷：{}, {}", answer.getExaminationId(), answer.getUserId());
        // 总分
        Integer totalScore = 0, correctNumber = 0, incorrectNumber = 0;
        // 查找已提交的题目
        List<Answer> answerList = findList(answer);
        if (CollectionUtils.isNotEmpty(answerList)) {
            // 收集题目id
            Set<String> subjectIdSet = new HashSet<>();
            answerList.forEach(tempAnswer -> {
                subjectIdSet.add(tempAnswer.getSubjectId());
            });
            Subject subject = new Subject();
            subject.setIds(subjectIdSet.toArray(new String[subjectIdSet.size()]));
            // 查找题目列表
            List<Subject> subjects = subjectService.findListById(subject);
            if (CollectionUtils.isNotEmpty(subjects)) {
                // 错题列表
                List<IncorrectAnswer> incorrectAnswers = new ArrayList<>();
                for (Subject tempSubject : subjects) {
                    for (Answer tempAnswer : answerList) {
                        if (tempAnswer.getSubjectId().equals(tempSubject.getId())) {
                            // 答题正确
                            if (tempSubject.getAnswer().toUpperCase().equalsIgnoreCase(tempAnswer.getAnswer())) {
                                totalScore += Integer.parseInt(tempSubject.getScore());
                                correctNumber++;
                            } else {
                                incorrectNumber++;
                                // 记录错题
                                IncorrectAnswer incorrectAnswer = new IncorrectAnswer();
                                incorrectAnswer.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                                incorrectAnswer.setExaminationId(tempAnswer.getExaminationId());
                                incorrectAnswer.setExamRecordId(answer.getExamRecordId());
                                incorrectAnswer.setSubjectId(tempAnswer.getSubjectId());
                                incorrectAnswer.setSerialNumber(tempSubject.getSerialNumber());
                                incorrectAnswer.setUserId(tempAnswer.getUserId());
                                incorrectAnswer.setIncorrectAnswer(tempAnswer.getAnswer());
                                incorrectAnswers.add(incorrectAnswer);
                            }
                        }
                    }
                }
                // 保存成绩
                ExamRecord examRecord = new ExamRecord();
                examRecord.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                examRecord.setId(answer.getExamRecordId());
                examRecord.setEndTime(examRecord.getCreateDate());
                examRecord.setScore(totalScore.toString());
                examRecord.setCorrectNumber(correctNumber.toString());
                examRecord.setInCorrectNumber(incorrectNumber.toString());
                examRecodeService.getByUserIdAndExaminationId(examRecord);
                success = examRecodeService.update(examRecord) > 0;
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
        return success;
    }
}
