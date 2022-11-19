package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectSpeech;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 *
 * @author tangyi
 * @date 2022/6/25 11:12 上午
 */
@Component
public class SubjectSpeechConverter implements ISubjectConverter<SubjectSpeech> {

	@Override
	public SubjectSpeech fromDto(SubjectDto dto) {
		SubjectSpeech subject = new SubjectSpeech();
		BeanUtils.copyProperties(dto, subject);
		return subject;
	}

	@Override
	public SubjectDto toDto(SubjectSpeech subject) {
		return this.toDto(subject, true);
	}

	@Override
	public SubjectDto toDto(SubjectSpeech subject, boolean findAnswer) {
		if (subject == null) {
			return null;
		}
		SubjectDto subjectDto = new SubjectDto();
		BeanUtils.copyProperties(subject, subjectDto);
		subjectDto.setType(SubjectTypeEnum.SPEECH.getValue());
		subjectDto.setTypeLabel(SubjectTypeEnum.SPEECH.getName());
		if (findAnswer) {
			Answer answer = new Answer();
			answer.setAnswer(subject.getAnswer());
			subjectDto.setAnswer(answer);
		}
		return subjectDto;
	}

	@Override
	public List<SubjectDto> toDto(List<SubjectSpeech> subjects, boolean findAnswer) {
		List<SubjectDto> subjectDtoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(subjects)) {
			subjectDtoList = subjects.stream().map(subject -> toDto(subject, findAnswer)).collect(Collectors.toList());
		}
		return subjectDtoList;
	}
}
