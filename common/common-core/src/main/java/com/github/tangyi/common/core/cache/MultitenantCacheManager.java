package com.github.tangyi.common.core.cache;

import com.github.tangyi.common.core.utils.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

/**
 * 扩展CacheManager支持多租户
 *
 * @author tangyi
 * @date 2019/6/9 15:12
 */
@Slf4j
public class MultitenantCacheManager extends RedisCacheManager {

    public MultitenantCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                   Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    /**
     * 扩展cache name，加上tenantCode作为前缀
     *
     * @param name name
     * @return Cache
     */
    @Override
    public Cache getCache(String name) {
        name = SysUtil.getTenantCode() + ":" + name;
        log.info("cache name: {}", name);
        return super.getCache(name);
    }
}
