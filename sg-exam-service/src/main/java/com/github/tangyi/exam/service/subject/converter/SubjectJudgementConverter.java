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
	public SubjectDto convert(SubjectJudgement subject) {
		return this.convert(subject, true);
	}

	@Override
	public SubjectDto convert(SubjectJudgement subject, boolean findAnswer) {
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
	public List<SubjectDto> convert(List<SubjectJudgement> subjects, boolean findAnswer) {
		List<SubjectDto> subjectDtoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(subjects)) {
			subjectDtoList = subjects.stream().map(subject -> this.convert(subject, findAnswer))
					.collect(Collectors.toList());
		}
		return subjectDtoList;
	}
}
