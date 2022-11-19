package com.github.tangyi.exam.service.subject;

import com.github.tangyi.exam.enums.SubjectTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author tangyi
 * @date 2022/4/13 8:15 下午
 */
@Slf4j
@Getter
@Component
@AllArgsConstructor
public class SubjectServiceFactory {

	private final SubjectChoicesService subjectChoicesService;

	private final SubjectShortAnswerService subjectShortAnswerService;

	private final SubjectJudgementService subjectJudgementService;

	private final SubjectSpeechService subjectSpeechService;

	private final SubjectVideoService subjectVideoService;

	/**
	 * 根据题目类型返回对应的BaseSubjectService
	 *
	 * @param type type
	 * @return BaseSubjectService
	 * @author tangyi
	 * @date 2019/06/16 17:34
	 */
	public ISubjectService service(Integer type) {
		if (SubjectTypeEnum.CHOICES.getValue().equals(type) || SubjectTypeEnum.MULTIPLE_CHOICES.getValue()
				.equals(type)) {
			return subjectChoicesService;
		} else if (SubjectTypeEnum.SHORT_ANSWER.getValue().equals(type)) {
			return subjectShortAnswerService;
		} else if (SubjectTypeEnum.JUDGEMENT.getValue().equals(type)) {
			return subjectJudgementService;
		} else if (SubjectTypeEnum.SPEECH.getValue().equals(type)) {
			return subjectSpeechService;
		} else if (SubjectTypeEnum.VIDEO.getValue().equals(type)) {
			return subjectVideoService;
		}
		throw new IllegalArgumentException("subject service not found: " + type);
	}
}
