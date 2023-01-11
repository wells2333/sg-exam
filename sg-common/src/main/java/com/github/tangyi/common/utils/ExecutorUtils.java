package com.github.tangyi.common.utils;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutorUtils {

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
			log.error("failed to waitFutures", e);
		}
	}
}
