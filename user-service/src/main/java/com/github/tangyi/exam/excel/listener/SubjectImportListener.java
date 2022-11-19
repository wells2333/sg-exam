package com.github.tangyi.exam.excel.listener;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.excel.AbstractExcelImportListener;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.excel.model.SubjectExcelModel;
import com.github.tangyi.exam.service.subject.ImportExportSubjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目导入
 * @author tangyi
 * @date 2019/12/10 20:52
 */
public class SubjectImportListener extends AbstractExcelImportListener<SubjectExcelModel> {

	private final ImportExportSubjectService importExportSubjectService;

	private final Long examinationId;

	private final Long categoryId;

	public SubjectImportListener(ImportExportSubjectService importExportSubjectService, Long examinationId,
			Long categoryId) {
		this.importExportSubjectService = importExportSubjectService;
		this.examinationId = examinationId;
		this.categoryId = categoryId;
	}

	@Override
	public void saveData(List<SubjectExcelModel> subjectExcelModelList) {
		logger.info("SaveData size: {}", subjectExcelModelList.size());
		List<SubjectDto> subjects = new ArrayList<>();
		String creator = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		subjectExcelModelList.forEach(subject -> {
			SubjectDto subjectDto = new SubjectDto();
			subjectDto.setCommonValue(creator, tenantCode);
			BeanUtils.copyProperties(subject, subjectDto);
			List<SubjectOption> subjectOptions = new ArrayList<>();
			if (StringUtils.isNotBlank(subject.getOptions())) {
				String[] options = subject.getOptions().split("\\$\\$");
				// $$A# 测试测试
				for (String option : options) {
					if (StringUtils.isNotBlank(option)) {
						String[] optionInfos = option.split("#");
						if (optionInfos.length >= 2) {
							// 去掉$$
							String optionName = optionInfos[0].trim();
							StringBuilder optionContent = new StringBuilder();
							if (optionInfos.length > 2) {
								for (int i = 1; i < optionInfos.length; i++) {
									optionContent.append(optionInfos[i].trim());
								}
							} else {
								optionContent = new StringBuilder(optionInfos[1].trim());
							}
							SubjectOption subjectOption = new SubjectOption();
							subjectOption.setOptionName(optionName);
							subjectOption.setOptionContent(optionContent.toString());
							subjectOptions.add(subjectOption);
						}
					}
				}
			}
			subjectDto.setOptions(subjectOptions);

			// 答案
			Answer answer = new Answer();
			answer.setAnswer(subject.getAnswer());
			subjectDto.setAnswer(answer);
			subjects.add(subjectDto);
		});
		importExportSubjectService.importSubject(subjects, examinationId, categoryId);
	}
}