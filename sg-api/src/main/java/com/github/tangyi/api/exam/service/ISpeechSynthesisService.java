package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.user.model.SpeechSynthesis;
import com.github.tangyi.common.service.ICrudService;

public interface ISpeechSynthesisService extends ICrudService<SpeechSynthesis> {

	boolean addSynthesis(SpeechSynthesis speechSynthesis) throws Exception;

	boolean updateSynthesis(SpeechSynthesis speechSynthesis) throws Exception;

	void synthesis(SpeechSynthesis speechSynthesis) throws Exception;
}
