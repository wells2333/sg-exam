package com.github.tangyi.common.oss.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * fastDfs配置
 *
 * @author tangyi
 * @date 2018-01-05 14:45
 */
@Deprecated
@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@ConditionalOnExpression("!'${fdfs}'.isEmpty()")
public class FastDfsConfig {

}

