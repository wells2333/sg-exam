package com.github.tangyi.api.exam.thread;

import com.google.common.util.concurrent.ListeningExecutorService;

public interface IExecutorHolder {

	ListeningExecutorService getCommonExecutor();

	ListeningExecutorService getExamExecutor();

	ListeningExecutorService getSubmitExecutor();

	ListeningExecutorService getImportExecutor();

	ListeningExecutorService getSubjectExecutor();
}
