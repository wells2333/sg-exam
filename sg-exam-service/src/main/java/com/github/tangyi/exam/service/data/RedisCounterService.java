/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RedisCounterService {

    private final RedisTemplate<String, Long> longRedisTemplate;

    public Long get(String key, Long id) {
        return longRedisTemplate.opsForValue().get(key + id);
    }

    public void expire(String key, Long id, int timeoutSecond) {
        longRedisTemplate.opsForValue().getOperations().expire(key + id, timeoutSecond, TimeUnit.SECONDS);
    }

    public void setCount(String key, Long id, Long value) {
        longRedisTemplate.opsForValue().set(key + id, value);
    }

    public Long incrCount(String key, Long id) {
        return longRedisTemplate.opsForValue().increment(key + id);
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
