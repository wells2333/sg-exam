package com.github.tangyi.exam.mq;

import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.exam.api.constants.ExamRecordConstant;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.ExamRecord;
import com.github.tangyi.exam.service.AnswerService;
import com.github.tangyi.exam.service.ExamRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提交考试消息消费者
 *
 * @author tangyi
 * @date 2019/5/3 14:55
 */
@Service
public class RabbitSubmitExaminationReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RabbitSubmitExaminationReceiver.class);

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ExamRecordService examRecordService;

    /**
     * 处理提交考试逻辑：统计错题，计算成绩等
     * 1. 先更新考试记录的状态为正在计算
     * 2. 更新成功则执行提交逻辑
     *
     * @param answer answer
     * @author tangyi
     * @date 2019/05/03 14:56
     */
    @RabbitListener(queues = {MqConstant.SUBMIT_EXAMINATION_QUEUE})
    public void submitExamination(Answer answer) {
        logger.debug("处理考试提交ID：{}, 提交人：{}", answer.getExamRecordId(), answer.getModifier());
        try {
            ExamRecord examRecord = new ExamRecord();
            examRecord.setId(answer.getExamRecordId());
            examRecord = examRecordService.get(examRecord);
            if (examRecord == null)
                return;
            if (ExamRecordConstant.STATUS_NOT_SUBMITTED.equals(examRecord.getSubmitStatus()))
                logger.warn("考试：{}未提交", examRecord.getId());
            if (ExamRecordConstant.STATUS_CALCULATE.equals(examRecord.getSubmitStatus()))
                logger.warn("考试：{}正在统计成绩，请勿重复提交", examRecord.getId());
            // 更新状态为正在统计
            examRecord.setSubmitStatus(ExamRecordConstant.STATUS_CALCULATE);
            // 更新成功
            if (examRecordService.update(examRecord) > 0) {
                logger.debug("考试：{}更新状态为正在统计成功", examRecord.getId());
                answerService.submit(answer);
            } else {
                logger.warn("考试：{}更新状态为正在统计失败", examRecord.getId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
