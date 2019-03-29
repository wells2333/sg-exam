package com.github.tangyi.exam.api.feign;

import com.github.tangyi.common.core.constant.ServiceConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.feign.config.CustomFeignConfig;
import com.github.tangyi.exam.api.feign.fallback.ExaminationServiceClientFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 考试接口
 *
 * FeignClient需要指定configuration为CustomFeignConfig，用于服务间调用携带token
 *
 * @author tangyi
 * @date 2019-03-01 15:21
 */
@FeignClient(name = ServiceConstant.EXAM_SERVICE, configuration = CustomFeignConfig.class, fallback = ExaminationServiceClientFallbackImpl.class)
public interface ExaminationServiceClient {

    /**
     * 查询考试数量
     *
     * @return ResponseBean
     */
    @GetMapping("/v1/examination/examinationCount")
    ResponseBean<Integer> findExaminationCount();
}
