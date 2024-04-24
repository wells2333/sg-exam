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

package com.github.tangyi.user.service.attach;

import cn.hutool.core.io.resource.ResourceUtil;
import com.github.tangyi.api.user.service.IDefaultImageService;
import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DefaultImageService implements IDefaultImageService {

	public static final String DEFAULT_IMAGE_SUFFIX = EnvUtils.getValue("DEFAULT_IMAGE_SUFFIX", ".jpeg");

	private final List<byte[]> images = Lists.newArrayList();

	public DefaultImageService() {
		try {
			long startNs = System.nanoTime();
			for (int i = 1; i <= 10; i++) {
				try (InputStream in = ResourceUtil.getStream("images/" + i + DEFAULT_IMAGE_SUFFIX)) {
					byte[] bytes = FileCopyUtils.copyToByteArray(in);
					if (bytes.length > 0) {
						images.add(bytes);
					}
				}
			}
			long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
			log.info("Init default image finished, size: {}, took: {}ms", images.size(), took);
		} catch (IOException e) {
			log.error("Failed to init default image.", e);
		}
	}

	@Override
	public byte[] randomImage() {
		if (CollectionUtils.isNotEmpty(images)) {
			return images.get(ThreadLocalRandom.current().nextInt(images.size()));
		}
		return null;
	}
}
