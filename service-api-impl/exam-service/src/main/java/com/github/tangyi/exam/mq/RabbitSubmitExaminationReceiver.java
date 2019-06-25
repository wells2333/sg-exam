package com.github.tangyi.exam.mq;

import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.security.tenant.TenantContextHolder;
import com.github.tangyi.exam.api.constants.ExamExaminationRecordConstant;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.ExaminationRecord;
import com.github.tangyi.exam.service.AnswerService;
import com.github.tangyi.exam.service.ExamRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 提交考试消息消费者
 *
 * @author tangyi
 * @date 2019/5/3 14:55
 */
@Slf4j
@AllArgsConstructor
@Service
public class RabbitSubmitExaminationReceiver {

    private final AnswerService answerService;

    private final ExamRecordService examRecordService;

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
        log.debug("处理考试提交ID：{}, 提交人：{}", answer.getExamRecordId(), answer.getModifier());
        try {
            // 异步提交会丢失tenantCode，需要手动设置
            TenantContextHolder.setTenantCode(answer.getTenantCode());
            ExaminationRecord examRecord = new ExaminationRecord();
            examRecord.setId(answer.getExamRecordId());
            examRecord = examRecordService.get(examRecord);
            if (examRecord == null)
                return;
            if (ExamExaminationRecordConstant.STATUS_NOT_SUBMITTED.equals(examRecord.getSubmitStatus()))
                log.warn("考试：{}未提交", examRecord.getId());
            if (ExamExaminationRecordConstant.STATUS_CALCULATE.equals(examRecord.getSubmitStatus()))
                log.warn("考试：{}正在统计成绩，请勿重复提交", examRecord.getId());
            // 更新状态为正在统计
            examRecord.setSubmitStatus(ExamExaminationRecordConstant.STATUS_CALCULATE);
            // 更新成功
            if (examRecordService.update(examRecord) > 0) {
                log.debug("考试：{}更新状态为正在统计成功", examRecord.getId());
                answerService.submit(answer);
            } else {
                log.warn("考试：{}更新状态为正在统计失败", examRecord.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
