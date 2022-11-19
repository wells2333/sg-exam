package com.github.tangyi.exam.service.subject.converter;

import com.github.tangyi.api.exam.dto.SubjectDto;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/6/25 10:53 上午
 */
public interface ISubjectConverter<T> {

	T fromDto(SubjectDto dto);

	SubjectDto toDto(T subject);

	SubjectDto toDto(T subject, boolean findAnswer);

	List<SubjectDto> toDto(List<T> subjects, boolean findAnswer);
}
