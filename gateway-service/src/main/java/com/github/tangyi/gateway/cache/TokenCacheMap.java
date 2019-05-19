package com.github.tangyi.gateway.cache;

import com.github.tangyi.gateway.model.AccessToken;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存token
 *
 * @author tangyi
 * @date 2019/5/19 18:01
 */
@Slf4j
public class TokenCacheMap {

    private static final long DEFAULT_TIMEOUT = 30000;

    /**
     * 单例
     */
    private static TokenCacheMap instance;

    /**
     * concurrentHashMap
     */
    private static ConcurrentHashMap<String, AccessToken> concurrentHashMap;

    public TokenCacheMap() {
        concurrentHashMap = new ConcurrentHashMap<>();
        new ClearThread().start();
    }

    /**
     * 获取单例
     *
     * @return TokenCacheMap
     */
    public static synchronized TokenCacheMap getInstance() {
        if (instance == null) {
            instance = new TokenCacheMap();
        }
        return instance;
    }

    /**
     * 获取AccessToken
     *
     * @param key key
     * @return AccessToken
     */
    public static AccessToken get(String key) {
        return concurrentHashMap.get(key);
    }

    /**
     * 设置AccessToken
     *
     * @param key         key
     * @param accessToken accessToken
     */
    public static void set(String key, AccessToken accessToken) {
        concurrentHashMap.put(key, accessToken);
    }

    /**
     * 定时清除线程
     */
    private class ClearThread extends Thread {
        ClearThread() {
            setName("clear token cache thread");
        }

        public void run() {
            while (true) {
                try {
                    long now = System.currentTimeMillis();
                    concurrentHashMap.keySet().forEach(key -> {
                        AccessToken accessToken = concurrentHashMap.get(key);
                        // 定时清除
                        if (now - accessToken.getCreateTime() >= accessToken.getExpiresIn()) {
                            concurrentHashMap.remove(key);
                        }
                    });
                    Thread.sleep(DEFAULT_TIMEOUT);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
