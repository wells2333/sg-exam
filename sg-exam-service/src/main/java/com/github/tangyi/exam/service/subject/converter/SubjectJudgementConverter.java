package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectJudgement;
import com.github.tangyi.exam.enums.SubjectType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectJudgementConverter implements ISubjectConverter<SubjectJudgement> {

	@Override
	public SubjectJudgement fromDto(SubjectDto dto) {
		SubjectJudgement subject = new SubjectJudgement();
		BeanUtils.copyProperties(dto, subject);
		return subject;
	}

	@Override
	public SubjectDto toDto(SubjectJudgement subject) {
		return this.toDto(subject, true);
	}

	@Override
	public SubjectDto toDto(SubjectJudgement subject, boolean findAnswer) {
		if (subject == null) {
			return null;
		}
		SubjectDto subjectDto = new SubjectDto();
		BeanUtils.copyProperties(subject, subjectDto);
		subjectDto.setType(SubjectType.JUDGEMENT.getValue());
		subjectDto.setType(SubjectType.JUDGEMENT.getValue());
		subjectDto.setTypeLabel(SubjectType.JUDGEMENT.getName());
		if (findAnswer) {
			Answer answer = new Answer();
			answer.setAnswer(subject.getAnswer());
			subjectDto.setAnswer(answer);
		}
		return subjectDto;
	}

	@Override
	public List<SubjectDto> toDto(List<SubjectJudgement> subjects, boolean findAnswer) {
		List<SubjectDto> subjectDtoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(subjects)) {
			subjectDtoList = subjects.stream().map(subject -> this.toDto(subject, findAnswer))
					.collect(Collectors.toList());
		}
		return subjectDtoList;
	}
}
