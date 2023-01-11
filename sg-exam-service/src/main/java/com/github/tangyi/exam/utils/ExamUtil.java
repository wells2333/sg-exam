package com.github.tangyi.exam.utils;

import com.github.tangyi.api.exam.dto.ExaminationRecordDto;
import com.github.tangyi.api.exam.dto.SimpleSubjectDto;
import com.github.tangyi.api.exam.dto.SimpleSubjectOptionDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.excel.model.ExamRecordExcelModel;
import com.github.tangyi.exam.excel.model.SubjectExcelModel;
import com.github.tangyi.exam.handler.AnswerHandleResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ExamUtil {

	private static final String REGEX_COMMA = "^,*|,*$";

	private ExamUtil() {
	}

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

	public static AnswerHandleResult addAll(List<AnswerHandleResult> results) {
		AnswerHandleResult result = new AnswerHandleResult();
		int score = 0;
		int correctNum = 0;
		int inCorrectNum = 0;
		for (AnswerHandleResult tempResult : results) {
			if (tempResult != null) {
				score += tempResult.getScore();
				correctNum += tempResult.getCorrectNum();
				inCorrectNum += tempResult.getInCorrectNum();
			}
		}
		result.setScore(score);
		result.setCorrectNum(correctNum);
		result.setInCorrectNum(inCorrectNum);
		return result;
	}

	/**
	 * 替换收尾的逗号
	 * @param str str
	 * @return String
	 */
	public static String replaceComma(String str) {
		if (StringUtils.isNotBlank(str)) {
			str = str.replaceAll(REGEX_COMMA, "");
		}
		return str;
	}

	public static Map<Integer, List<Answer>> distinctAnswer(List<Answer> answers, Map<Long, Integer> typeMap) {
		Map<Integer, List<Answer>> map = Maps.newHashMap();
		map.putAll(answers.stream()
				.collect(Collectors.groupingBy(s -> typeMap.get(s.getSubjectId()), Collectors.toList())));
		return map;
	}

	public static List<ExamRecordExcelModel> convertExamRecord(List<ExaminationRecordDto> examinationRecords) {
		List<ExamRecordExcelModel> examRecordExcelModels = new ArrayList<>(examinationRecords.size());
		examinationRecords.forEach(examinationRecord -> {
			ExamRecordExcelModel examRecordExcelModel = new ExamRecordExcelModel();
			BeanUtils.copyProperties(examinationRecord, examRecordExcelModel);
			examRecordExcelModels.add(examRecordExcelModel);
		});
		return examRecordExcelModels;
	}

	public static List<SubjectExcelModel> convertSubject(List<SubjectDto> dtoList) {
		List<SubjectExcelModel> subjectExcelModels = new ArrayList<>(dtoList.size());
		dtoList.forEach(subject -> {
			SubjectExcelModel subjectExcelModel = new SubjectExcelModel();
			BeanUtils.copyProperties(subject, subjectExcelModel);
			if (CollectionUtils.isNotEmpty(subject.getOptions())) {
				for (SubjectOption option : subject.getOptions()) {
					switch (option.getOptionName()) {
						case "A" -> subjectExcelModel.setOptionA(option.getOptionContent());
						case "B" -> subjectExcelModel.setOptionB(option.getOptionContent());
						case "C" -> subjectExcelModel.setOptionC(option.getOptionContent());
						case "D" -> subjectExcelModel.setOptionD(option.getOptionContent());
						default -> {
						}
					}
				}
			}
			subjectExcelModel.setAnswer(subject.getAnswer().getAnswer());
			subjectExcelModels.add(subjectExcelModel);
		});
		return subjectExcelModels;
	}

	/**
	 * 遍历关系集合，按类型分组题目ID，返回map
	 */
	public static Map<String, Long[]> groupByType(List<Subjects> subjects) {
		Map<String, Long[]> idMap = Maps.newHashMapWithExpectedSize(4);
		subjects.stream().collect(Collectors.groupingBy(Subjects::getType, Collectors.toList()))
				.forEach((type, temp) -> {
					if (SubjectType.CHOICES.getValue().equals(type)) {
						idMap.put(SubjectType.CHOICES.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectType.JUDGEMENT.getValue().equals(type)) {
						idMap.put(SubjectType.JUDGEMENT.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectType.MULTIPLE_CHOICES.getValue().equals(type)) {
						idMap.put(SubjectType.MULTIPLE_CHOICES.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectType.SHORT_ANSWER.getValue().equals(type)) {
						idMap.put(SubjectType.SHORT_ANSWER.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectType.SPEECH.getValue().equals(type)) {
						idMap.put(SubjectType.SPEECH.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectType.VIDEO.getValue().equals(type)) {
						idMap.put(SubjectType.VIDEO.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else {
						log.error("unknown subject type: {}", type);
					}
				});
		return idMap;
	}

	public static Map<Long, Integer> toMap(List<Subjects> subjects) {
		return subjects.stream().collect(Collectors.toMap(Subjects::getSubjectId, Subjects::getType));
	}
}
