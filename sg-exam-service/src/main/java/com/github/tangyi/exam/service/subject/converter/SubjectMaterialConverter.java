package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectMaterial;
import com.github.tangyi.exam.enums.SubjectType;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectMaterialConverter implements ISubjectConverter<SubjectMaterial> {

	@Override
	public SubjectDto convert(SubjectMaterial subject) {
		return this.convert(subject, true);
	}

	@Override
	public SubjectDto convert(SubjectMaterial subject, boolean findAnswer) {
		if (subject == null) {
			return null;
		}

		SubjectDto dto = new SubjectDto();
		BeanUtils.copyProperties(subject, dto);
		dto.setType(SubjectType.FILL_BLANK.getValue());
		dto.setType(SubjectType.FILL_BLANK.getValue());
		dto.setTypeLabel(SubjectType.FILL_BLANK.getName());
		if (findAnswer) {
			Answer answer = new Answer();
			answer.setAnswer(subject.getAnswer());
			dto.setAnswer(answer);
		}
		return dto;
	}

	@Override
	public List<SubjectDto> convert(List<SubjectMaterial> subjects, boolean findAnswer) {
		List<SubjectDto> list = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(subjects)) {
			list = subjects.stream().map(subject -> this.convert(subject, findAnswer)).collect(Collectors.toList());
		}
		return list;
	}
}
