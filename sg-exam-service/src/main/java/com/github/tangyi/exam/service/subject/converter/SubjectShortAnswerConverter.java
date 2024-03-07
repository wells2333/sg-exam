package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectShortAnswer;
import com.github.tangyi.exam.enums.SubjectType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectShortAnswerConverter implements ISubjectConverter<SubjectShortAnswer> {

	@Override
	public SubjectDto convert(SubjectShortAnswer subject) {
		return this.convert(subject, true);
	}

	@Override
	public SubjectDto convert(SubjectShortAnswer subject, boolean findAnswer) {
		if (subject == null) {
			return null;
		}

		SubjectDto dto = new SubjectDto();
		BeanUtils.copyProperties(subject, dto);
		dto.setType(SubjectType.SHORT_ANSWER.getValue());
		dto.setType(SubjectType.SHORT_ANSWER.getValue());
		dto.setTypeLabel(SubjectType.SHORT_ANSWER.getName());
		if (findAnswer) {
			Answer answer = new Answer();
			answer.setAnswer(subject.getAnswer());
			dto.setAnswer(answer);
		}
		return dto;
	}

	@Override
	public List<SubjectDto> convert(List<SubjectShortAnswer> subjects, boolean findAnswer) {
		List<SubjectDto> list = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(subjects)) {
			list = subjects.stream().map(subject -> convert(subject, findAnswer)).collect(Collectors.toList());
		}
		return list;
	}
}
