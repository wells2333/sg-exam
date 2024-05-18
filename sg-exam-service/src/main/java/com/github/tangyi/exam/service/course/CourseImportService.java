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

package com.github.tangyi.exam.service.course;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.tangyi.api.exam.dto.CourseImportDto;
import com.github.tangyi.api.exam.model.ExamCourseChapter;
import com.github.tangyi.api.exam.model.ExamCourseSection;
import com.github.tangyi.api.exam.service.IExamCourseChapterService;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.service.IAttachGroupService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TxUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.constants.ExamConstant;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@AllArgsConstructor
public class CourseImportService {

	private final PlatformTransactionManager txManager;
	private final AttachmentManager attachmentManager;
	private final IAttachGroupService attachGroupService;
	private final IExamCourseChapterService chapterService;
	private final ExamCourseSectionService sectionService;

	public List<CourseImportDto> extractChapter(MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		Preconditions.checkNotNull(filename);
		if (!filename.endsWith(".zip")) {
			throw new IllegalStateException("Unsupported file type: " + filename);
		}

		return this.extractZip(file, filename);
	}

	@CacheEvict(value = ExamCacheName.COURSE, key = "#courseId")
	public boolean importChapter(Long courseId, List<CourseImportDto> dtoList) {
		Integer maxSort = this.chapterService.findMaxSortByCourseId(courseId);
		TransactionStatus status = TxUtil.startTransaction(txManager);
		try {
			// 插入章节
			AtomicLong sort = new AtomicLong(maxSort == null ? 0 : maxSort);
			for (CourseImportDto dto : dtoList) {
				if (StringUtils.isEmpty(dto.getContent())) {
					continue;
				}

				ExamCourseChapter chapter = new ExamCourseChapter();
				chapter.setCommonValue();
				chapter.setCourseId(courseId);
				chapter.setSort(sort.incrementAndGet());
				// 章标题固定为：第 xxx 章
				chapter.setTitle("第 " + chapter.getSort() + " 章");
				this.chapterService.insert(chapter);

				ExamCourseSection section = new ExamCourseSection();
				section.setCommonValue();
				section.setChapterId(chapter.getId());
				section.setSort(1L);
				section.setTitle(dto.getTitle());
				this.insertSection(section, dto);
			}
			txManager.commit(status);
		} catch (Exception e) {
			log.error("Failed to commit import chapter transaction.", e);
			txManager.rollback(status);
			throw e;
		}
		return true;
	}

	@CacheEvict(value = ExamCacheName.CHAPTER, key = "#chapterId")
	public boolean importSection(Long chapterId, List<CourseImportDto> dtoList) {
		Integer maxSort = this.sectionService.findMaxSortByChapterId(chapterId);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = txManager.getTransaction(def);
		try {
			// 插入节
			AtomicLong sort = new AtomicLong(maxSort == null ? 0 : maxSort);
			for (CourseImportDto dto : dtoList) {
				if (StringUtils.isEmpty(dto.getContent())) {
					continue;
				}

				ExamCourseSection section = new ExamCourseSection();
				section.setCommonValue();
				section.setChapterId(chapterId);
				section.setSort(sort.incrementAndGet());
				section.setTitle(dto.getTitle());
				this.insertSection(section, dto);
			}
			txManager.commit(status);
		} catch (Exception e) {
			log.error("Failed to commit import section transaction.", e);
			txManager.rollback(status);
			throw e;
		}
		return true;
	}

	private void insertSection(ExamCourseSection section, CourseImportDto dto) {
		if (CourseImportDto.PartType.VIDEO.equals(dto.getType())) {
			// 类型为视频
			section.setContentType(ExamConstant.SECTION_TYPE_VIDEO);
			// 章节的内容为视频 URL
			section.setVideoUrl(dto.getContent());
		} else {
			// 默认类型为 PDF
			section.setContentType(ExamConstant.SECTION_TYPE_PDF);
			// 章节的内容为 PDF URL
			section.setContent(dto.getContent());
		}
		this.sectionService.insert(section);
	}

	private List<CourseImportDto> extractZip(MultipartFile file, String filename) throws IOException {
		String tmpdir = System.getProperty("java.io.tmpdir") + File.separator + System.nanoTime();
		File tmpdirFile = new File(tmpdir);
		try {
			FileUtils.forceMkdir(tmpdirFile);
			File zipFile = new File(tmpdir + File.separator + filename);
			log.info("Extracting zip file: " + zipFile.getAbsolutePath());
			try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(zipFile)) {
				FileCopyUtils.copy(in, out);
			}
			Preconditions.checkState(zipFile.exists(), "The zip file does not exist.");
			try {
				log.info("Try to unzip use charset utf-8, filename: {}", filename);
				ZipUtil.unzip(zipFile, tmpdirFile, CharsetUtil.CHARSET_UTF_8);
			} catch (Exception e) {
				log.info("Unzip use charset utf-8 failed, try to unzip use gbk again , msg: {}", e.getMessage());
				if (e instanceof UtilException && StringUtils.contains(e.getMessage(), "invalid CEN header")) {
					ZipUtil.unzip(zipFile, tmpdirFile, CharsetUtil.CHARSET_GBK);
				} else {
					throw e;
				}
			}
			return this.handleUnzipFiles(tmpdirFile);
		} finally {
			FileUtils.deleteDirectory(tmpdirFile);
		}
	}

	private List<CourseImportDto> handleUnzipFiles(File tmpdirFile) {
		List<CourseImportDto> dtoList = Lists.newArrayList();
		AttachGroup group = attachGroupService.findByGroupCode(
				AttachGroup.of(AttachTypeEnum.EXAM_VIDEO).getGroupCode());
		File[] files = tmpdirFile.listFiles();
		Preconditions.checkNotNull(files);
		// 遍历所有文件
		for (File f : files) {
			String fileName = f.getName();
			Integer partType = null;
			if (fileName.endsWith(".mp4")) {
				partType = CourseImportDto.PartType.VIDEO;
			} else if (fileName.endsWith(".pdf")) {
				partType = CourseImportDto.PartType.PDF;
			}

			if (partType == null) {
				continue;
			}

			Attachment a = this.doHandleUnzipFile(f, group);
			if (a != null) {
				// 生成章节内容
				String title = FilenameUtils.getBaseName(f.getName());
				dtoList.add(new CourseImportDto(partType, title, a.getUrl()));
			}
		}
		return dtoList;
	}

	private Attachment doHandleUnzipFile(File f, AttachGroup group) {
		Attachment a;
		try {
			String filename = f.getName();
			String extension = FilenameUtils.getExtension(filename);
			// 重新生成文件名
			String renewName = String.format("%s_%s.%s", SecureUtil.md5(filename), System.nanoTime(), extension);
			BytesUploadContext c = new BytesUploadContext();
			c.setGroup(group);
			c.setFileName(renewName);
			c.setBytes(FileUtils.readFileToByteArray(f));
			c.setOriginalFilename(renewName);
			c.setUser(SysUtil.getUser());
			c.setTenantCode(SysUtil.getTenantCode());
			// 上传文件
			a = this.attachmentManager.upload(c);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return a;
	}
}
