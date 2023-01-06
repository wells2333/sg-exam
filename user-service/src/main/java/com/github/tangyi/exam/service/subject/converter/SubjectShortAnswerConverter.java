package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectShortAnswer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectShortAnswerConverter implements ISubjectConverter<SubjectShortAnswer> {

	@Override
	public SubjectShortAnswer fromDto(SubjectDto dto) {
		SubjectShortAnswer subject = new SubjectShortAnswer();
		BeanUtils.copyProperties(dto, subject);
		return subject;
	}

	@Override
	public SubjectDto toDto(SubjectShortAnswer subject) {
		return this.toDto(subject, true);
	}

	@Override
	public SubjectDto toDto(SubjectShortAnswer subject, boolean findAnswer) {
		if (subject == null) {
			return null;
		}
		SubjectDto subjectDto = new SubjectDto();
		BeanUtils.copyProperties(subject, subjectDto);
		subjectDto.setType(SubjectTypeEnum.SHORT_ANSWER.getValue());
		subjectDto.setType(SubjectTypeEnum.SHORT_ANSWER.getValue());
		subjectDto.setTypeLabel(SubjectTypeEnum.SHORT_ANSWER.getName());
		if (findAnswer) {
			Answer answer = new Answer();
			answer.setAnswer(subject.getAnswer());
			subjectDto.setAnswer(answer);
		}
		return subjectDto;
	}

	@Override
	public List<SubjectDto> toDto(List<SubjectShortAnswer> subjects, boolean findAnswer) {
		List<SubjectDto> subjectDtoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(subjects)) {
			subjectDtoList = subjects.stream().map(subject -> toDto(subject, findAnswer)).collect(Collectors.toList());
		}
		return subjectDtoList;
	}
}
