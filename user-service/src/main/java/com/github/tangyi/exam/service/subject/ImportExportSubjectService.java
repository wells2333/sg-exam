package com.github.tangyi.exam.service.subject;

import com.alibaba.fastjson.JSON;
import com.github.tangyi.api.exam.constants.ExamSubjectConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.utils.Id;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author tangyi
 * @date 2022/4/14 6:55 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class ImportExportSubjectService {

	private final SubjectsService subjectsService;

	private final ExaminationSubjectService examinationSubjectService;

	/**
	 * 导入题目
	 *
	 * @param subjects      subjects
	 * @param examinationId examinationId
	 * @param categoryId    categoryId
	 * @return int
	 * @author tangyi
	 * @date 2019/06/17 14:39
	 */
	@Transactional
	public List<SubjectDto> importSubject(List<SubjectDto> subjects, Long examinationId, Long categoryId) {
		List<SubjectDto> list = Lists.newArrayList();
		String creator = SysUtil.getUser(), tenantCode = SysUtil.getTenantCode();
		// 暂时循环遍历保存
		for (SubjectDto subject : subjects) {
			if (examinationId != null) {
				subject.setExaminationId(examinationId);
			}
			if (categoryId == null) {
				categoryId = ExamSubjectConstant.DEFAULT_CATEGORY_ID;
			}
			subject.setCategoryId(categoryId);
			if (subject.getId() == null) {
				// 重新生成ID
				subject.setId(Id.nextId());
				subject.setCommonValue(creator, tenantCode);
				list.add(subjectsService.insert(subject));
			} else {
				subject.setCommonValue(creator, tenantCode);
				list.add(subjectsService.update(subject));
			}
		}
		return list;
	}

	/**
	 * 导出
	 *
	 * @param ids           ids
	 * @param examinationId examinationId
	 * @param categoryId    categoryId
	 * @return List
	 */
	public List<SubjectDto> export(Long[] ids, Long examinationId, Long categoryId) {
		List<SubjectDto> subjects = new ArrayList<>();
		ExaminationSubject examinationSubject = new ExaminationSubject();
		List<ExaminationSubject> examinationSubjects = new ArrayList<>();
		// 根据题目id导出
		if (ArrayUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				examinationSubject.setSubjectId(id);
				examinationSubjects.addAll(examinationSubjectService.findListBySubjectId(examinationSubject));
			}
		} else if (examinationId != null) {
			// 根据考试ID
			examinationSubjects = examinationSubjectService.findListByExaminationId(examinationId);
		} else if (categoryId != null) {
			// 根据分类ID、类型导出
			examinationSubjects = examinationSubjectService.findListByCategoryId(examinationSubject);
		}
		if (CollectionUtils.isNotEmpty(examinationSubjects)) {
			for (ExaminationSubject es : examinationSubjects) {
				SubjectDto subjectDto = subjectsService.getSubject(es.getSubjectId());
				subjectDto.setExaminationId(es.getExaminationId());
				subjects.add(subjectDto);
			}
		}
		return subjects;
	}

	/**
	 * 导入json格式
	 * @param categoryId categoryId
	 * @param file file
	 * @return Boolean
	 */
	public Boolean importJSONSubject(Long categoryId, MultipartFile file) throws IOException {
		try (InputStream inputStream = file.getInputStream()) {
			String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			List<SubjectDto> subjects = JSON.parseArray(json, SubjectDto.class);
			if (CollectionUtils.isEmpty(subjects)) {
				log.error("importJSONSubject, subjects is empty");
				return Boolean.FALSE;
			}
			// 查询出最大的序号
			int nextNo = subjectsService.nextSubjectNo(categoryId);
			for (SubjectDto subject : subjects) {
				subject.setId(null);
				subject.setNewRecord(true);
				subject.setSort(nextNo++);
				subject.setUpdateTime(null);
				subject.setIsDeleted(null);
			}
			importSubject(subjects, null, categoryId);
		}
		return Boolean.TRUE;
	}

	/**
	 * 导入txt 格式的题目
	 *
	 * @param categoryId categoryId
	 * @param file       file
	 */
	public Boolean importTxt(Long categoryId, MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
			if (CollectionUtils.isNotEmpty(lines)) {
				List<SubjectDto> subjects = new ArrayList<>();
				String creator = SysUtil.getUser();
				String tenantCode = SysUtil.getTenantCode();
				for (int i = 0; i < lines.size(); i += 7) {
					SubjectDto subjectDto = new SubjectDto();
					subjectDto.setCommonValue(creator, tenantCode);
					subjectDto.setSubjectName(lines.get(i));
					subjectDto.setCategoryId(categoryId);
					List<SubjectOption> subjectOptions = new ArrayList<>();
					initSubjectOption(lines.get(i + 1), subjectOptions);
					initSubjectOption(lines.get(i + 2), subjectOptions);
					initSubjectOption(lines.get(i + 3), subjectOptions);
					initSubjectOption(lines.get(i + 4), subjectOptions);

					subjectDto.setOptions(subjectOptions);
					// 答案
					Answer answer = new Answer();
					answer.setAnswer(lines.get(i + 5));
					subjectDto.setAnswer(answer);
					subjectDto.setScore(Double.parseDouble(lines.get(i + 6)));
					subjects.add(subjectDto);
				}
				importSubject(subjects, null, categoryId);
			}
		} catch (Exception e) {
			log.error("importTxt failed", e);
		}
		return Boolean.TRUE;
	}

	public SubjectOption initSubjectOption(String optionName, String optionContent) {
		SubjectOption subjectOption = new SubjectOption();
		subjectOption.setOptionName(optionName);
		subjectOption.setOptionContent(optionContent);
		return subjectOption;
	}

	public void initSubjectOption(String option, List<SubjectOption> subjectOptions) {
		String pattern = ".";
		if (option.contains(pattern)) {
			String optionName = option.substring(0, option.indexOf(pattern));
			String optionContent = option.substring(option.indexOf(pattern) + 1);
			SubjectOption subjectOption = initSubjectOption(optionName, optionContent);
			subjectOptions.add(subjectOption);
		}
	}
}
