package com.github.tangyi.exam.utils;

import com.github.tangyi.api.exam.dto.SimpleSubjectDto;
import com.github.tangyi.api.exam.dto.SimpleSubjectOptionDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/4/5 9:14 上午
 */
public class ExaminationUtil {

	public static ExaminationSubject createEs(Long examinationId, Long subjectId) {
		ExaminationSubject es = new ExaminationSubject();
		es.setExaminationId(examinationId);
		es.setSubjectId(subjectId);
		return es;
	}

	public static List<SimpleSubjectDto> simpleSubject(List<SubjectDto> dtoList) {
		List<SimpleSubjectDto> simples = Lists.newArrayListWithExpectedSize(dtoList.size());
		for (SubjectDto dto : dtoList) {
			SimpleSubjectDto simple = new SimpleSubjectDto();
			simple.setId(dto.getId());
			simple.setSubjectName(dto.getSubjectName());
			simple.setTypeLabel(dto.getTypeLabel());
			simple.setType(dto.getType());
			simple.setChoicesType(dto.getChoicesType());
			simple.setScore(dto.getScore());
			simple.setLevel(dto.getLevel());
			simple.setSort(dto.getSort());

			List<SimpleSubjectOptionDto> optionDtos = Lists.newArrayList();
			List<SubjectOption> options = dto.getOptions();
			if (CollectionUtils.isNotEmpty(options)) {
				for (SubjectOption option : options) {
					SimpleSubjectOptionDto optionDto = new SimpleSubjectOptionDto();
					optionDto.setOptionName(option.getOptionName());
					optionDto.setOptionContent(option.getOptionContent());
					optionDto.setSort(option.getSort());
					optionDtos.add(optionDto);
				}
			}
			simple.setOptions(optionDtos);
			simples.add(simple);
		}
		return simples;
	}
}
