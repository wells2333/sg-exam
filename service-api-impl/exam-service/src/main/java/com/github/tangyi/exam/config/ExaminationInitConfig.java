package com.github.tangyi.exam.config;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.exam.api.module.Examination;
import com.github.tangyi.exam.service.ExaminationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 考试初始化
 * 启动时加载已发布的考试到缓存
 *
 * @author tangyi
 * @date 2019/4/30 16:02
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class ExaminationInitConfig {

    private final RedisTemplate redisTemplate;

    private final ExaminationService examinationService;

    @PostConstruct
    public void initExamination() {
        log.info("开始加载考试信息.");
        // 查询已发布的考试
        Examination examination = new Examination();
        examination.setStatus(CommonConstant.STATUS_NORMAL);
        Stream<Examination> examinationStream = examinationService.findList(examination).stream();
        if (Optional.ofNullable(examinationStream).isPresent())
            examinationStream.forEach(tempExamination -> redisTemplate.opsForValue().set("examination::" + tempExamination.getId(), tempExamination));
        log.info("考试信息加载完成.");
    }
}
