package com.github.tangyi.common.utils.executor;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author tangyi
 * @date 2021/3/1 7:42 下午
 */
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
}
