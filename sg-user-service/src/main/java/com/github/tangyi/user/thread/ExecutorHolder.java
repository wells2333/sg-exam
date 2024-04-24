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

package com.github.tangyi.user.thread;

import com.github.tangyi.api.exam.thread.IExecutorHolder;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.ExecutorUtils;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ExecutorHolder implements IExecutorHolder {

	private static final int EXECUTOR_QUEUE_SIZE = EnvUtils.getInt("EXECUTOR_QUEUE_SIZE", 2000);
	private static final int EXECUTOR_CORE_SIZE = EnvUtils.getInt("EXECUTOR_CORE_SIZE",
			Runtime.getRuntime().availableProcessors() + 1);

	private ListeningExecutorService commonExecutor;
	private ListeningExecutorService examExecutor;
	private ListeningExecutorService subjectExecutor;
	private ListeningExecutorService submitExecutor;
	private ListeningExecutorService importExecutor;
	private ListeningExecutorService qiNiuUploadExecutor;

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

		log.info("Initializing qiNiu upload executor");
		this.qiNiuUploadExecutor = ExecutorUtils.newListeningExecutor("qiniu-upload-%d", coreSize, EXECUTOR_QUEUE_SIZE);
		log.info("Init qiNiu upload executor finished, coreSize: {}", coreSize);
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

	public ListeningExecutorService getQiNiuUploadExecutor() {
		return qiNiuUploadExecutor;
	}
}
