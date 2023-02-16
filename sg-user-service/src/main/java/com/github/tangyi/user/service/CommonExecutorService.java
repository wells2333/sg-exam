package com.github.tangyi.user.service;

import com.github.tangyi.api.exam.service.IExecutorService;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.ExecutorUtils;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class CommonExecutorService implements IExecutorService {

	public static final int EXECUTOR_QUEUE_SIZE = EnvUtils.getInt("EXECUTOR_QUEUE_SIZE", 2000);

	public static final int EXECUTOR_CORE_SIZE = EnvUtils.getInt("EXECUTOR_CORE_SIZE",
			Runtime.getRuntime().availableProcessors() + 1);

	private ListeningExecutorService commonExecutor;

	private ListeningExecutorService examExecutor;

	private ListeningExecutorService subjectExecutor;

	private ListeningExecutorService submitExecutor;

	private ListeningExecutorService importExecutor;

	@PostConstruct
	public void init() {
		log.info("Initializing common executor");
		int coreSize = EXECUTOR_CORE_SIZE;
		this.commonExecutor = ExecutorUtils.newListeningExecutor("common-%d", coreSize, EXECUTOR_QUEUE_SIZE);
		log.info("Init common executor finished, coreSize: {}", coreSize);

		log.info("Initializing exam executor");
		this.examExecutor = ExecutorUtils.newListeningExecutor("exam-%d", coreSize, EXECUTOR_QUEUE_SIZE);
		log.info("Init exam executor finished, coreSize: {}", coreSize);

		log.info("Initializing subject executor");
		this.subjectExecutor = ExecutorUtils.newListeningExecutor("subject-%d", coreSize, EXECUTOR_QUEUE_SIZE);
		log.info("Init subject executor finished, coreSize: {}", coreSize);

		log.info("Initializing submit exam executor");
		this.submitExecutor = ExecutorUtils.newListeningExecutor("submit-exam-%d", coreSize, EXECUTOR_QUEUE_SIZE);
		log.info("Init submit exam executor finished, coreSize: {}", coreSize);

		log.info("Initializing import executor");
		this.importExecutor = ExecutorUtils.newListeningExecutor("import-%d", coreSize, EXECUTOR_QUEUE_SIZE);
		log.info("Init import executor finished, coreSize: {}", coreSize);
	}

	public ListeningExecutorService getCommonExecutor() {
		return commonExecutor;
	}

	public ListeningExecutorService getExamExecutor() {
		return examExecutor;
	}

	public ListeningExecutorService getSubmitExecutor() {
		return submitExecutor;
	}

	public ListeningExecutorService getImportExecutor() {
		return importExecutor;
	}

	public ListeningExecutorService getSubjectExecutor() {
		return subjectExecutor;
	}
}
