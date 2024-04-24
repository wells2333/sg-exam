/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.exam.service.subject;

import com.alibaba.fastjson.JSON;
import com.github.tangyi.api.exam.constants.ExamSubjectConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationSubject;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.api.exam.thread.IExecutorHolder;
import com.github.tangyi.common.excel.AbstractExcelImportListener;
import com.github.tangyi.common.excel.ExcelToolUtil;
import com.github.tangyi.common.utils.SnowFlakeId;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.exam.enums.SubjectLevel;
import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.excel.SubjectExcelModel;
import com.github.tangyi.exam.service.ExaminationSubjectService;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@AllArgsConstructor
public class SubjectImportExportService {

	private final SubjectsService subjectsService;
	private final ExaminationSubjectService examinationSubjectService;
	private final IExecutorHolder executorHolder;

	@Transactional
	public void importSubject(List<SubjectDto> subjects, Long examinationId, Long categoryId, String creator,
			String tenantCode) {
		log.info("Start to import subjects, size: {}", subjects.size());
		StopWatch start = StopWatchUtil.start();
		if (categoryId == null) {
			categoryId = ExamSubjectConstant.DEFAULT_CATEGORY_ID;
		}
		for (List<SubjectDto> pt : Lists.partition(subjects, 100)) {
			doImport(pt, examinationId, categoryId, creator, tenantCode);
		}
		log.info("Subjects has been imported, size: {}, took: {}", subjects.size(), StopWatchUtil.stop(start));
	}

	@Transactional
	public void importSubjectAsync(List<SubjectDto> subjects, Long examinationId, Long categoryId, String creator,
			String tenantCode) {
		log.info("Start to import subjects, size: {}", subjects.size());
		StopWatch start = StopWatchUtil.start();
		if (categoryId == null) {
			categoryId = ExamSubjectConstant.DEFAULT_CATEGORY_ID;
		}
		final Long finalCategoryId = categoryId;
		ListeningExecutorService executor = executorHolder.getImportExecutor();
		for (List<SubjectDto> pt : Lists.partition(subjects, 100)) {
			ListenableFuture<Boolean> future = executor.submit(() -> {
				try {
					doImport(pt, examinationId, finalCategoryId, creator, tenantCode);
					return Boolean.TRUE;
				} catch (Exception e) {
					return Boolean.FALSE;
				}
			});
			Futures.addCallback(future, new FutureCallback<>() {
				@Override
				public void onSuccess(@Nullable Boolean result) {
					log.info("Subjects has been imported, result: {}, size: {}", result, pt.size());
				}

				@Override
				public void onFailure(@NotNull Throwable e) {
					log.error("Failed to import subject, size: {}", pt.size(), e);
				}
			}, executor);
		}
		log.info("Subjects has been imported, size: {}, took: {}", subjects.size(), StopWatchUtil.stop(start));
	}

	@Transactional
	public void doImport(List<SubjectDto> subjects, Long examinationId, Long categoryId, String creator,
			String tenantCode) {
		for (SubjectDto subject : subjects) {
			if (examinationId != null) {
				subject.setExaminationId(examinationId);
			}
			subject.setCategoryId(categoryId);
			if (subject.getId() == null) {
				subject.setId(SnowFlakeId.newId());
				subject.setCommonValue(creator, tenantCode);
				subjectsService.insert(subject);
			} else {
				subject.setCommonValue(creator, tenantCode);
				subjectsService.update(subject);
			}
			log.info("Import subject finished, categoryId: {}, examinationId: {}, sort: {}, creator: {}", categoryId,
					examinationId, subject.getSort(), creator);
		}
	}

	public List<SubjectDto> export(Long[] ids, Long examinationId, Long categoryId) {
		List<SubjectDto> subjects = new ArrayList<>();
		ExaminationSubject es = new ExaminationSubject();
		List<ExaminationSubject> examinationSubjects = Lists.newArrayList();
		// 根据题目 ID 导出
		if (ArrayUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				es.setSubjectId(id);
				examinationSubjects.add(es);
			}
		} else if (examinationId != null) {
			// 根据考试 ID
			examinationSubjects = examinationSubjectService.findListByExaminationId(examinationId);
		} else if (categoryId != null) {
			// 根据分类 ID、类型导出
			examinationSubjects = examinationSubjectService.findListByCategoryId(categoryId);
		}
		if (CollectionUtils.isNotEmpty(examinationSubjects)) {
			for (ExaminationSubject temp : examinationSubjects) {
				SubjectDto dto = subjectsService.getSubject(temp.getSubjectId());
				dto.setExaminationId(temp.getExaminationId());
				subjects.add(dto);
			}
		}
		return subjects;
	}

	public Boolean importJSONSubject(Long categoryId, MultipartFile file) throws IOException {
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		try (InputStream inputStream = file.getInputStream()) {
			String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			ListeningExecutorService executor = executorHolder.getImportExecutor();
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
					log.info("Failed to import JSONSubject, categoryId: {}", categoryId, e);
					return Boolean.FALSE;
				}
			});
			Futures.addCallback(future, new FutureCallback<>() {
				@Override
				public void onSuccess(@Nullable Boolean result) {
					log.info("ImportJSONSubject finished, result: {}", result);
				}

				@Override
				public void onFailure(Throwable e) {
					log.error("Failed to import JSONSubject", e);
				}
			}, executor);
		}
		return Boolean.TRUE;
	}

	public Boolean importExcelSubject(Long categoryId, MultipartFile file) throws IOException {
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		Integer maxNo = subjectsService.findMaxSortByCategoryId(categoryId);
		AtomicInteger nextNo = new AtomicInteger(maxNo == null ? 0 : maxNo);
		ListeningExecutorService executor = executorHolder.getImportExecutor();
		// 数据读取到内存
		byte[] data = IOUtils.toByteArray(file.getInputStream());
		ListenableFuture<Boolean> future = executor.submit(() -> {
			try (InputStream in = new BufferedInputStream(new ByteArrayInputStream(data))) {
				Listener listener = new Listener(this, null, categoryId, user, tenantCode, nextNo);
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
				log.info("Import excel subject success, result: {}", result);
			}

			@Override
			public void onFailure(@Nullable Throwable e) {
				log.error("Import excel subject failed.", e);
			}
		}, executor);
		return Boolean.TRUE;
	}

	public List<SubjectDto> demoSubjects() {
		SubjectDto dto = new SubjectDto();
		dto.setSubjectName("《山行》是描绘了___的景色。");
		dto.setType(SubjectType.CHOICES.getValue());
		dto.setChoicesType(0);
		dto.setScore(5.0);
		dto.setLevel(SubjectLevel.NORMAL.getValue());
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
		List<String> lines = FileUtils.readLines(new File(file), StandardCharsets.UTF_8);
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
			dto.setType(SubjectType.CHOICES.getValue());
			dto.setChoicesType(0);
			dto.setLevel(SubjectLevel.NORMAL.getValue());
			list.add(dto);
		}
		return list;
	}

	public String remove(String value) {
		return value.substring(value.indexOf(".") + 1).trim();
	}

	private static final class Listener extends AbstractExcelImportListener<SubjectExcelModel> {

		private final SubjectImportExportService service;
		private final Long examinationId;
		private final Long categoryId;
		private final String creator;
		private final String tenantCode;
		private final AtomicInteger nextNo;

		public Listener(SubjectImportExportService service, Long examinationId, Long categoryId, String creator,
				String tenantCode, AtomicInteger nextNo) {
			this.service = service;
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
			for (SubjectExcelModel model : models) {
				SubjectDto dto = new SubjectDto();
				dto.setNewRecord(true);
				dto.setCommonValue(creator, tenantCode);
				BeanUtils.copyProperties(model, dto);
				dto.setSort(nextNo.incrementAndGet());
				List<SubjectOption> options = Lists.newArrayList();
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
				if (StringUtils.isNotEmpty(model.getOptionE())) {
					options.add(newOption("E", model.getOptionE(), 5));
				}
				if (StringUtils.isNotEmpty(model.getOptionF())) {
					options.add(newOption("F", model.getOptionF(), 6));
				}
				if (StringUtils.isNotEmpty(model.getOptionG())) {
					options.add(newOption("G", model.getOptionG(), 7));
				}
				if (StringUtils.isNotEmpty(model.getOptionH())) {
					options.add(newOption("H", model.getOptionH(), 8));
				}
				if (StringUtils.isNotEmpty(model.getOptionI())) {
					options.add(newOption("I", model.getOptionI(), 9));
				}
				dto.setOptions(options);
				Answer answer = new Answer();
				answer.setAnswer(model.getAnswer());
				dto.setAnswer(answer);
				subjects.add(dto);
			}
			service.importSubjectAsync(subjects, examinationId, categoryId, creator, tenantCode);
		}

		public SubjectOption newOption(String name, String content, int sort) {
			SubjectOption option = new SubjectOption();
			option.setOptionName(name);
			option.setOptionContent(content);
			option.setSort(sort);
			return option;
		}
	}
}
