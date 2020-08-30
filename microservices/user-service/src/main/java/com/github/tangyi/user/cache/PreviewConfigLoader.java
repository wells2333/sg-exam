package com.github.tangyi.user.cache;

import com.github.tangyi.common.cache.LoadingCacheHelper;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.Map;

/**
 * 加载演示环境配置
 * @author tangyi
 * @date 2019/12/11 13:56
 */
@Slf4j
public class PreviewConfigLoader extends CacheLoader<String, Map<String, String>> {

	/**
	 * 30秒刷新一次loadingCache
	 */
	public static final int REFRESH_CACHE_DURATION = 30;

	public static final String PREVIEW_ENABLE = "preview_enable";

	@Override
	public Map<String, String> load(String key) throws Exception {
		return loadData(key);
	}

	@Override
	public ListenableFuture<Map<String, String>> reload(String key, Map<String, String> oldValue) throws Exception {
		return LoadingCacheHelper.REFRESH_POOLS.submit(() -> loadData(key));
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> loadData(String key) {
		// 从Redis获取配置
		RedisTemplate<String, String> redisTemplate = (RedisTemplate) SpringContextHolder.getApplicationContext().getBean("redisTemplate");
		Object enablePreview = redisTemplate.opsForValue().get(PREVIEW_ENABLE);
		if (enablePreview != null)
			return Collections.singletonMap(PREVIEW_ENABLE, enablePreview.toString());
		return Collections.emptyMap();
	}
}
