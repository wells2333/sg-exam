package com.github.tangyi.exam.service.subject;

import com.alibaba.fastjson.JSON;
import com.github.tangyi.api.exam.constants.ExamSubjectConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.utils.Id;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.enums.SubjectLevelEnum;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.excel.listener.SubjectImportListener;
import com.github.tangyi.exam.excel.model.SubjectExcelModel;
import com.github.tangyi.exam.service.ExaminationSubjectService;
import com.github.tangyi.user.service.CommonExecutorService;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SubjectImportExportService {

	private final SubjectsService subjectsService;

	private final ExaminationSubjectService examinationSubjectService;

	private final CommonExecutorService commonExecutorService;

	@Transactional
	public void importSubject(List<SubjectDto> subjects, Long examinationId, Long categoryId, String creator,
			String tenantCode) {
		log.info("start to import subjects, size: {}", subjects.size());
		StopWatch start = StopWatchUtil.start();
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
				subjectsService.insert(subject);
			} else {
				subject.setCommonValue(creator, tenantCode);
				subjectsService.update(subject);
			}
		}
		log.info("import subjects success, size: {}, took: {}", subjects.size(), StopWatchUtil.stop(start));
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
		List<ExaminationSubject> examinationSubjects = Lists.newArrayList();
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
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		try (InputStream inputStream = file.getInputStream()) {
			String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			ListeningExecutorService executor = commonExecutorService.getImportExecutor();
			ListenableFuture<Boolean> future = executor.submit(() -> {
				try {
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
					importSubject(subjects, null, categoryId, user, tenantCode);
					return Boolean.TRUE;
				} catch (Exception e) {
					log.info("importJSONSubject failed, categoryId: {}", categoryId, e);
					return Boolean.FALSE;
				}
			});
			Futures.addCallback(future, new FutureCallback<>() {
				@Override
				public void onSuccess(@Nullable Boolean result) {
					log.info("importJSONSubject success, result: {}", result);
				}

				@Override
				public void onFailure(Throwable e) {
					log.error("importJSONSubject failed", e);
				}
			}, executor);
		}
		return Boolean.TRUE;
	}

	/**
	 * 导入EXCEL格式
	 * @param categoryId categoryId
	 * @param file file
	 * @return Boolean
	 */
	public Boolean importExcelSubject(Long categoryId, MultipartFile file) throws IOException {
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		ListeningExecutorService executor = commonExecutorService.getImportExecutor();
		// 数据读取到内存
		byte[] data = IOUtils.toByteArray(file.getInputStream());
		ListenableFuture<Boolean> future = executor.submit(() -> {
			try (InputStream in = new BufferedInputStream(new ByteArrayInputStream(data))) {
				int nextNo = subjectsService.nextSubjectNo(categoryId);
				SubjectImportListener listener = new SubjectImportListener(this, null, categoryId, user, tenantCode,
						nextNo);
				ExcelToolUtil.readExcel(in, SubjectExcelModel.class, listener);
			} catch (Exception e) {
				log.error("importExcelSubject failed, categoryId: {}", categoryId, e);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		});
		Futures.addCallback(future, new FutureCallback<>() {
			@Override
			public void onSuccess(@Nullable Boolean result) {
				log.info("importExcelSubject success, result: {}", result);
			}

			@Override
			public void onFailure(Throwable e) {
				log.error("importExcelSubject failed", e);
			}
		}, executor);
		return Boolean.TRUE;
	}

	/**
	 * 题目模板
	 * @return List
	 */
	public List<SubjectDto> demoSubjects() {
		SubjectDto dto = new SubjectDto();
		dto.setSubjectName("《山行》是描绘了___的景色.");
		dto.setType(SubjectTypeEnum.CHOICES.getValue());
		dto.setChoicesType(0);
		dto.setScore(5.0);
		dto.setLevel(SubjectLevelEnum.NORMAL.getValue());
		Answer answer = new Answer();
		answer.setAnswer("A");
		dto.setAnswer(answer);

		List<SubjectOption> options = Lists.newArrayListWithExpectedSize(4);
		SubjectOption optionA = new SubjectOption();
		optionA.setOptionName("C");
		optionA.setOptionContent("春天");
		optionA.setSort(1);
		options.add(optionA);

		SubjectOption optionB = new SubjectOption();
		optionB.setOptionName("B");
		optionB.setOptionContent("夏天");
		optionB.setSort(1);
		options.add(optionB);

		SubjectOption optionC = new SubjectOption();
		optionC.setOptionName("C");
		optionC.setOptionContent("秋天");
		optionC.setSort(1);
		options.add(optionC);

		SubjectOption optionD = new SubjectOption();
		optionD.setOptionName("D");
		optionD.setOptionContent("冬天");
		optionD.setSort(1);
		options.add(optionD);

		dto.setOptions(options);
		return Lists.newArrayList(dto);
	}

	public List<SubjectDto> demoTxtSubjects() throws IOException {
		String file = "";
		List<String> lines = FileUtils.readLines(new File(file));
		List<SubjectDto> list = Lists.newArrayList();
		for (int i = 0; i < lines.size(); i = i + 7) {
			String subjectName = lines.get(i);
			String optionAValue = remove(lines.get(i + 1));
			String optionBValue = remove(lines.get(i + 2));
			String optionCValue = remove(lines.get(i + 3));
			String optionDValue = remove(lines.get(i + 4));
			String answerValue = lines.get(i + 5);
			String score = lines.get(i + 6);

			SubjectDto dto = new SubjectDto();
			dto.setSubjectName(subjectName);

			List<SubjectOption> options = Lists.newArrayListWithExpectedSize(4);
			SubjectOption optionA = new SubjectOption();
			optionA.setOptionName("A");
			optionA.setOptionContent(optionAValue);
			optionA.setSort(1);

			SubjectOption optionB = new SubjectOption();
			optionB.setOptionName("B");
			optionB.setOptionContent(optionBValue);
			optionB.setSort(2);

			SubjectOption optionC = new SubjectOption();
			optionC.setOptionName("C");
			optionC.setOptionContent(optionCValue);
			optionC.setSort(3);

			SubjectOption optionD = new SubjectOption();
			optionD.setOptionName("D");
			optionD.setOptionContent(optionDValue);
			optionD.setSort(4);

			options.add(optionA);
			options.add(optionB);
			options.add(optionC);
			options.add(optionD);
			dto.setOptions(options);
			Answer answer = new Answer();
			answer.setAnswer(answerValue);
			dto.setAnswer(answer);
			dto.setScore(Double.parseDouble(score));
			dto.setType(SubjectTypeEnum.CHOICES.getValue());
			dto.setChoicesType(0);
			dto.setLevel(SubjectLevelEnum.NORMAL.getValue());
			list.add(dto);
		}
		return list;
	}

	public String remove(String value) {
		return value.substring(value.indexOf(".") + 1).trim();
	}
}
