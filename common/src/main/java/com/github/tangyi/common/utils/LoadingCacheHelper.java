package com.github.tangyi.common.utils;

import com.github.tangyi.common.exceptions.CommonException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * LoadingCache工具类，单例
 * 用法：
 * 1. LoadingCacheHelper.getInstance().getOrDefault(SimpleCacheLoader.class, "key", "defaultValue");
 * 2. LoadingCacheHelper.getInstance().get(SimpleCacheLoader.class, "key");
 */
@Slf4j
public class LoadingCacheHelper {

	public static ListeningExecutorService REFRESH_POOLS;

	private static class LoadingCacheHelperInstance {
		public static final LoadingCacheHelper instance = new LoadingCacheHelper();
	}

	public static LoadingCacheHelper getInstance() {
		return LoadingCacheHelper.LoadingCacheHelperInstance.instance;
	}

	/**
	 * 保存所有loadingCache实例
	 */
	private Map<String, LoadingCache> loadingCacheMap;

	private LoadingCacheHelper() {
		loadingCacheMap = new HashMap<>();
		REFRESH_POOLS = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1,
				new ThreadFactoryBuilder().setNameFormat("refresh-cache-thread-%d").build()));
	}

	/**
	 * 初始化loadingCache
	 * @param clazz clazz
	 * @param duration 频率，单位秒
	 * @param <K> key
	 * @param <V> value
	 * @return LoadingCache
	 */
	public <K, V> LoadingCache<K, V> initLoadingCache(Class<? extends CacheLoader<K, V>> clazz, long duration) {
		return initLoadingCache(clazz, duration, 0);
	}

	/**
	 * 初始化loadingCache
	 * @param clazz clazz
	 * @param duration 频率，单位秒
	 * @param <K> key
	 * @param <V> value
	 * @param cacheSize cacheSize
	 * @return LoadingCache
	 */
	private <K, V> LoadingCache<K, V> initLoadingCache(Class<? extends CacheLoader<K, V>> clazz, long duration,
			long cacheSize) {
		return initLoadingCache(clazz, duration, 0, cacheSize);
	}

	/**
	 * 初始化loadingCache
	 * @param clazz clazz
	 * @param refreshAfterWriteDuration 单位秒
	 * @param expireAfterAccessDuration 单位秒
	 * @param <K> key
	 * @param <V> value
	 * @param cacheSize cacheSize
	 * @return LoadingCache
	 */
	private <K, V> LoadingCache<K, V> initLoadingCache(Class<? extends CacheLoader<K, V>> clazz,
			long refreshAfterWriteDuration, long expireAfterAccessDuration, long cacheSize) {
		try {
			log.info("Instantiating LoadingCache: {}", clazz);
			CacheLoader<K, V> cacheLoader = clazz.newInstance();
			CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
			builder.concurrencyLevel(1);
			if (expireAfterAccessDuration > 0) {
				// 在给定时间内没有被读/写访问，则回收
				builder.expireAfterAccess(expireAfterAccessDuration, TimeUnit.SECONDS);
			} else {
				// 自动刷新
				builder.refreshAfterWrite(refreshAfterWriteDuration, TimeUnit.SECONDS);
			}
			if (cacheSize > 0)
				builder.maximumSize(cacheSize);
			LoadingCache<K, V> cache = builder.build(cacheLoader);
			this.loadingCacheMap.put(clazz.getSimpleName(), cache);
			return cache;
		} catch (Exception e) {
			log.error("Error Instantiating LoadingCache: " + clazz, e);
			throw new CommonException(e, "Error Instantiating LoadingCache: " + clazz);
		}
	}

	/**
	 * 从指定的loadingCache里获取内容
	 * @param clazz clazz
	 * @param key key
	 * @param defaultValue defaultValue
	 * @param <K> key
	 * @param <V> value
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	public <K, V> V getOrDefault(Class<? extends CacheLoader<K, V>> clazz, K key, V defaultValue) {
		V value = get(clazz, key);
		return value == null ? defaultValue : value;
	}

	/**
	 * 从指定的loadingCache里获取内容
	 * @param clazz clazz
	 * @param key key
	 * @param <K> key
	 * @param <V> value
	 * @return value，没有对应的key或获取异常则返回null
	 */
	@SuppressWarnings("unchecked")
	public <K, V> V get(Class<? extends CacheLoader<K, V>> clazz, K key) {
		LoadingCache<K, V> cache = this.loadingCacheMap.get(clazz.getSimpleName());
		if (cache != null) {
			try {
				return cache.get(key);
			} catch (Exception e) {
				log.error("Get from loadingCache error, {}, {}, {}", clazz, key, e.getMessage(), e);
			}
		}
		return null;
	}

	public Map<String, LoadingCache> getLoadingCacheMap() {
		return loadingCacheMap;
	}

	/**
	 * 刷新某个key
	 * @param classSimpleName classSimpleName
	 * @param key key
	 */
	@SuppressWarnings("unchecked")
	public void refresh(String classSimpleName, String key) {
		LoadingCache cache = this.loadingCacheMap.get(classSimpleName);
		if (cache != null) {
			cache.refresh(key);
			log.info("Refresh loadingCache: {}, {}", classSimpleName, key);
		}
	}

	/**
	 * 刷新所有key
	 * @param classSimpleName classSimpleName
	 */
	@SuppressWarnings("unchecked")
	public void refreshAll(String classSimpleName) {
		LoadingCache<String, ?> cache = this.loadingCacheMap.get(classSimpleName);
		if (cache != null) {
			for (String key : cache.asMap().keySet()) {
				cache.refresh(key);
				log.info("Refresh loadingCache: {}, {}", classSimpleName, key);
			}
		}
	}
}
