package com.github.tangyi.exam.service.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author tangyi
 * @date 2022/11/12 11:21 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class RedisCounterService {

	private final RedisTemplate<String, Long> longRedisTemplate;

	public void setCount(String key, Long id, Long value) {
		longRedisTemplate.opsForValue().set(key + id, value);
	}

	public void incrCount(String key, Long id) {
		longRedisTemplate.opsForValue().increment(key + id);
	}

	public void decrCount(String key, Long id) {
		longRedisTemplate.opsForValue().decrement(key + id);
	}

	public Map<Long, Long> getCounts(String key, List<Long> ids) {
		Map<Long, Long> countMap = Maps.newHashMapWithExpectedSize(ids.size());
		for (List<Long> tempIds : Lists.partition(ids, 50)) {
			List<String> keys = tempIds.stream().map(id -> key + id).collect(Collectors.toList());
			List<Long> values = longRedisTemplate.opsForValue().multiGet(keys);
			if (CollectionUtils.isNotEmpty(values)) {
				for (int i = 0; i < values.size(); i++) {
					Long value = values.get(i);
					if (value != null) {
						countMap.put(ids.get(i), values.get(i));
					}
				}
			}
		}
		log.info("get redis counts success, size: {}", countMap.size());
		return countMap;
	}
}
