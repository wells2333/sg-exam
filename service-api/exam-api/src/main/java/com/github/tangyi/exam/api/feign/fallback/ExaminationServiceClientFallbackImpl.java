package com.github.tangyi.exam.api.feign.fallback;

import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.exam.api.feign.ExaminationServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考试服务熔断
 *
 * @author tangyi
 * @date 2019-03-01 15:22
 */
@Slf4j
@Service
public class ExaminationServiceClientFallbackImpl implements ExaminationServiceClient {

    private Throwable throwable;

    @Override
    public ResponseBean<Integer> findExaminationCount() {
        log.error("调用{}异常,{}", "findExaminationCount", throwable);
        return new ResponseBean<>(0);
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
