package com.github.tangyi.api.exam.service;

import com.google.common.util.concurrent.ListeningExecutorService;

public interface IExecutorService {

	ListeningExecutorService getCommonExecutor();

	ListeningExecutorService getExamExecutor();

	ListeningExecutorService getSubmitExecutor();

	ListeningExecutorService getImportExecutor();

	ListeningExecutorService getSubjectExecutor();
}
