package com.github.tangyi.exam.excel.listener;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.excel.AbstractExcelImportListener;
import com.github.tangyi.exam.excel.model.SubjectExcelModel;
import com.github.tangyi.exam.service.subject.SubjectImportExportService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class SubjectImportListener extends AbstractExcelImportListener<SubjectExcelModel> {

	private final SubjectImportExportService importExportService;

	private final Long examinationId;

	private final Long categoryId;

	private final String creator;

	private final String tenantCode;

	private final int nextNo;

	public SubjectImportListener(SubjectImportExportService importExportService, Long examinationId,
			Long categoryId, String creator, String tenantCode, int nextNo) {
		this.importExportService = importExportService;
		this.examinationId = examinationId;
		this.categoryId = categoryId;
		this.creator = creator;
		this.tenantCode = tenantCode;
		this.nextNo = nextNo;
	}

	@Override
	public void saveData(List<SubjectExcelModel> models) {
		logger.info("SaveData size: {}, creator: {}, tenantCode: {}", models.size(), creator, tenantCode);
		List<SubjectDto> subjects = Lists.newArrayListWithExpectedSize(models.size());
		int sort = this.nextNo;
		for (SubjectExcelModel model : models) {
			SubjectDto dto = new SubjectDto();
			dto.setNewRecord(true);
			dto.setCommonValue(creator, tenantCode);
			BeanUtils.copyProperties(model, dto);
			dto.setSort(sort++);
			List<SubjectOption> options = Lists.newArrayListWithExpectedSize(4);
			if (StringUtils.isNotEmpty(model.getOptionA())) {
				options.add(newOption("A", model.getOptionA(), 1));
			}
			if (StringUtils.isNotEmpty(model.getOptionB())) {
				options.add(newOption("B", model.getOptionB(), 2));
			}
			if (StringUtils.isNotEmpty(model.getOptionC())) {
				options.add(newOption("C", model.getOptionC(), 3));
			}
			if (StringUtils.isNotEmpty(model.getOptionD())) {
				options.add(newOption("D", model.getOptionD(), 4));
			}
			dto.setOptions(options);
			Answer answer = new Answer();
			answer.setAnswer(model.getAnswer());
			dto.setAnswer(answer);
			subjects.add(dto);
		}
		importExportService.importSubjectAsync(subjects, examinationId, categoryId, creator, tenantCode);
	}

	public SubjectOption newOption(String name, String content, int sort) {
		SubjectOption option = new SubjectOption();
		option.setOptionName(name);
		option.setOptionContent(content);
		option.setSort(sort);
		return option;
	}
}