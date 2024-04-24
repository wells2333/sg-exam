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

package com.github.tangyi.common.utils;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutorUtils {

	private ExecutorUtils() {
	}

	public static ThreadPoolExecutor newExecutor(String threadName, int corePoolSize, int queueCapacity) {
		return new ThreadPoolExecutor(corePoolSize, corePoolSize, 10, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(queueCapacity), new ThreadFactoryBuilder().setNameFormat(threadName).build(),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static ListeningExecutorService newListeningExecutor(String threadName, int corePoolSize,
			int queueCapacity) {
		ThreadPoolExecutor threadPoolExecutor = newExecutor(threadName, corePoolSize, queueCapacity);
		return MoreExecutors.listeningDecorator(threadPoolExecutor);
	}

	public static void waitFutures(List<ListenableFuture<?>> futures) {
		try {
			Futures.allAsList(futures).get();
		} catch (Exception e) {
			log.error("Failed to wait futures", e);
		}
	}
}
