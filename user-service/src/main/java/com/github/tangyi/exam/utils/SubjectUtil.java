package com.github.tangyi.exam.utils;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.excel.model.SubjectExcelModel;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 题目工具类
 *
 * @author tangyi
 * @version V1.0
 * @date 2018-11-28 12:56
 */
@Slf4j
public class SubjectUtil {

	private SubjectUtil() {
	}

	public static List<SubjectExcelModel> convertToExcelModel(List<SubjectDto> dtoList) {
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
	 *
	 * @param subjects subjects
	 * @return Map
	 * @author tangyi
	 * @date 2019/06/17 10:43
	 */
	public static Map<String, Long[]> groupByType(List<Subjects> subjects) {
		Map<String, Long[]> idMap = Maps.newHashMapWithExpectedSize(4);
		subjects.stream().collect(Collectors.groupingBy(Subjects::getType, Collectors.toList()))
				.forEach((type, temp) -> {
					if (SubjectTypeEnum.CHOICES.getValue().equals(type)) {
						idMap.put(SubjectTypeEnum.CHOICES.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectTypeEnum.JUDGEMENT.getValue().equals(type)) {
						idMap.put(SubjectTypeEnum.JUDGEMENT.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectTypeEnum.MULTIPLE_CHOICES.getValue().equals(type)) {
						idMap.put(SubjectTypeEnum.MULTIPLE_CHOICES.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectTypeEnum.SHORT_ANSWER.getValue().equals(type)) {
						idMap.put(SubjectTypeEnum.SHORT_ANSWER.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectTypeEnum.SPEECH.getValue().equals(type)) {
						idMap.put(SubjectTypeEnum.SPEECH.name(),
								temp.stream().map(Subjects::getSubjectId).distinct().toArray(Long[]::new));
					} else if (SubjectTypeEnum.VIDEO.getValue().equals(type)) {
						idMap.put(SubjectTypeEnum.VIDEO.name(),
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
