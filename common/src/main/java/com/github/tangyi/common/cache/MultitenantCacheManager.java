package com.github.tangyi.common.cache;

import com.github.tangyi.common.utils.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Map;

/**
 * 扩展CacheManager支持多租户
 *
 * @author tangyi
 * @date 2019/6/9 15:12
 */
@Slf4j
public class MultitenantCacheManager extends RedisCacheManager {

    private static final String SPLIT_FLAG = "#";

    private static final int CACHE_LENGTH = 2;

    public MultitenantCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                   Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    /**
     * 扩展@Cacheable，支持配置失效时间
     *
     * @param name        name
     * @param cacheConfig cacheConfig
     * @return RedisCache
     */
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        if (StringUtils.isBlank(name) || !name.contains(SPLIT_FLAG)) {
            return super.createRedisCache(name, cacheConfig);
        }
        String[] cacheArray = name.split(SPLIT_FLAG);
        if (cacheArray.length < CACHE_LENGTH) {
            return super.createRedisCache(name, cacheConfig);
        }
        String cacheName = cacheArray[0];
        if (cacheConfig != null) {
            // 从系统属性里读取超时时间
            long cacheAge = Long.getLong(cacheArray[1], -1);
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(cacheAge));
        }
        return super.createRedisCache(cacheName, cacheConfig);
    }

    /**
     * 扩展cache name，加上tenantCode作为前缀
     *
     * @param name name
     * @return Cache
     */
    @Override
    public Cache getCache(String name) {
        return super.getCache(SysUtil.getTenantCode() + ":" + name);
    }
}
