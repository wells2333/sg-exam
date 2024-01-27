package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectChoices;
import com.github.tangyi.exam.enums.SubjectType;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectChoicesConverter implements ISubjectConverter<SubjectChoices> {

	@Override
	public SubjectDto convert(SubjectChoices subject) {
		return this.convert(subject, true);
	}

	@Override
	public SubjectDto convert(SubjectChoices subject, boolean findAnswer) {
		if (subject == null) {
			return null;
		}

		SubjectDto dto = new SubjectDto();
		BeanUtils.copyProperties(subject, dto);
		SubjectType type = SubjectType.matchByValue(subject.getChoicesType());
		dto.setType(type.getValue());
		dto.setTypeLabel(type.getName());
		if (findAnswer) {
			Answer a = new Answer();
			a.setAnswer(subject.getAnswer());
			dto.setAnswer(a);
		}
		return dto;
	}

	@Override
	public List<SubjectDto> convert(List<SubjectChoices> subjects, boolean findAnswer) {
		List<SubjectDto> list = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(subjects)) {
			list = subjects.stream().map(s -> convert(s, findAnswer)).collect(Collectors.toList());
		}
		return list;
	}
}
