package com.github.tangyi.common.core.config;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.properties.SysProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动时的一些处理
 *
 * @author tangyi
 * @date 2019/07/14 16:09
 */
@Slf4j
@AllArgsConstructor
@Component
public class AppStartupRunner implements CommandLineRunner {

    private final SysProperties sysProperties;

    @Override
    public void run(String... args) throws Exception {
        log.info("start command line...");
        log.info("set system properties...");
        // 设置系统属性
        System.setProperty(CommonConstant.CACHE_EXPIRE, sysProperties.getCacheExpire());
    }
}
