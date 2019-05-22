package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.ExamRecord;
import com.github.tangyi.exam.api.module.Subject;
import com.github.tangyi.exam.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 题目service
 *
 * @author tangyi
 * @date 2018/11/8 21:23
 */
@AllArgsConstructor
@Service
public class SubjectService extends CrudService<SubjectMapper, Subject> {

    private final ExamRecordService examRecordService;

    private final AnswerService answerService;

    /**
     * 查找题目
     *
     * @param subject subject
     * @return Subject
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Cacheable(value = "subject", key = "#subject.id")
    public Subject get(Subject subject) {
        return super.get(subject);
    }

    /**
     * 根据考试ID和序号查找
     *
     * @param subject subject
     * @return Subject
     * @author tangyi
     * @date 2019/01/20 12:22
     */
    public Subject getByExaminationIdAndSerialNumber(Subject subject) {
        return this.dao.getByExaminationIdAndSerialNumber(subject);
    }

    /**
     * 根据考试ID查询题目数
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/01/23 20:19
     */
    int getExaminationTotalSubject(Subject subject) {
        return this.dao.getExaminationTotalSubject(subject);
    }

    /**
     * 新增
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/01/23 20:03
     */
    @Override
    @Transactional
    public int insert(Subject subject) {
        return super.insert(subject);
    }

    /**
     * 更新题目
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Transactional
    @CacheEvict(value = "subject", key = "#subject.id")
    public int update(Subject subject) {
        return super.update(subject);
    }

    /**
     * 删除题目
     *
     * @param subject subject
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Transactional
    @CacheEvict(value = "subject", key = "#subject.id")
    public int delete(Subject subject) {
        return super.delete(subject);
    }

    /**
     * 批量删除题目
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:24
     */
    @Override
    @Transactional
    @CacheEvict(value = "subject", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
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
            subject = this.getByExaminationIdAndSerialNumber(subject);
            if (subject != null) {
                subjectDto = new SubjectDto();
                // 查找答题
                Answer answer = new Answer();
                answer.setSubjectId(subject.getId());
                answer.setExaminationId(examRecord.getExaminationId());
                answer.setExamRecordId(examRecordId);
                answer.setUserId(userId);
                Answer userAnswer = answerService.getAnswer(answer);
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
