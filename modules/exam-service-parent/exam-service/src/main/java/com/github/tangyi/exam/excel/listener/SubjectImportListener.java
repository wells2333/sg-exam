package com.github.tangyi.exam.excel.listener;

import com.github.tangyi.common.core.utils.excel.AbstractExcelImportListener;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.excel.model.SubjectExcelModel;
import com.github.tangyi.exam.service.SubjectService;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目导入
 * @author tangyi
 * @date 2019/12/10 20:52
 */
public class SubjectImportListener extends AbstractExcelImportListener<SubjectExcelModel> {

	private SubjectService subjectService;

	private Long examinationId;

	private Long categoryId;

	public SubjectImportListener(SubjectService subjectService, Long examinationId, Long categoryId) {
		this.subjectService = subjectService;
		this.examinationId = examinationId;
		this.categoryId = categoryId;
	}

	@Override
	public void saveData(List<SubjectExcelModel> subjectExcelModelList) {
		logger.info("saveData size: {}", subjectExcelModelList.size());
		List<SubjectDto> subjects = new ArrayList<>();
		subjectExcelModelList.forEach(subject -> {
			SubjectDto subjectDto = new SubjectDto();
			BeanUtils.copyProperties(subject, subjectDto);
			subjects.add(subjectDto);
		});
		subjectService.importSubject(subjects, examinationId, categoryId);
	}
}