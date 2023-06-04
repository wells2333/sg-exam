package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;

import java.util.List;

public interface ISubjectConverter<T> {

	SubjectDto convert(T subject);

	SubjectDto convert(T subject, boolean findAnswer);

	List<SubjectDto> convert(List<T> subjects, boolean findAnswer);
}
