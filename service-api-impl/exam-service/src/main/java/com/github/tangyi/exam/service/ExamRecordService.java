package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.exam.api.constants.ExamRecordConstant;
import com.github.tangyi.exam.api.constants.SubjectConstant;
import com.github.tangyi.exam.api.dto.StartExamDto;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.ExamRecord;
import com.github.tangyi.exam.api.module.Examination;
import com.github.tangyi.exam.api.module.Subject;
import com.github.tangyi.exam.mapper.ExamRecordMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 考试记录service
 *
 * @author tangyi
 * @date 2018/11/8 21:20
 */
@Service
public class ExamRecordService extends CrudService<ExamRecordMapper, ExamRecord> {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExaminationService examinationService;

    /**
     * 查询考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Cacheable(value = "record", key = "#examRecord.id")
    public ExamRecord get(ExamRecord examRecord) {
        return super.get(examRecord);
    }

    /**
     * 更新考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", key = "#examRecord.id")
    public int update(ExamRecord examRecord) {
        return super.update(examRecord);
    }

    /**
     * 删除考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", key = "#examRecord.id")
    public int insert(ExamRecord examRecord) {
        return super.insert(examRecord);
    }

    /**
     * 根据用户id、考试id查找
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2018/12/26 13:58
     */
    public ExamRecord getByUserIdAndExaminationId(ExamRecord examRecord) {
        return this.dao.getByUserIdAndExaminationId(examRecord);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:11
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
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
            answerService.save(answer);
            subjectDto.setAnswer(answer);
        }
        return startExamDto;
    }
}
