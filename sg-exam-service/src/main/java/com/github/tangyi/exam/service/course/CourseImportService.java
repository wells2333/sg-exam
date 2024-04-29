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

import cn.hutool.core.util.ZipUtil;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.service.IAttachGroupService;
import com.github.tangyi.common.utils.SysUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.pdf.pdfbox.core.base.Document;
import org.dromara.pdf.pdfbox.core.ext.extractor.DocumentExtractor;
import org.dromara.pdf.pdfbox.handler.DocumentHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
@AllArgsConstructor
public class CourseImportService {

	private final AttachmentManager attachmentManager;
	private final IAttachGroupService attachGroupService;

	public Map<Integer, List<String>> extractPdfText(InputStream in) {
		Document document = DocumentHandler.getInstance().load(in);
		DocumentExtractor extractor = new DocumentExtractor(document);
		return extractor.extractText(IntStream.range(0, document.getTotalPageNumber()).toArray());
	}

	/**
	 * 简单的处理 PDF 解析的文本，解析成章节的数据结构
	 */
	public List<Part> extractPdfTextToSection(InputStream in) {
		Map<Integer, List<String>> page2text = this.extractPdfText(in);
		if (MapUtils.isEmpty(page2text)) {
			return Collections.emptyList();
		}

		List<Part> sections = Lists.newArrayList();
		StringBuilder builder = new StringBuilder();
		String title = "";
		for (Map.Entry<Integer, List<String>> e : page2text.entrySet()) {
			String[] strArray = e.getValue().get(0).split("\n");
			if (strArray.length == 1) {
				title = strArray[0];
				sections.add(new Part(PartType.PDF, title, ""));
				continue;
			}

			// 这一页的第一行
			String first = strArray[0];
			// 统计后面三行的长度
			int length = strArray[1].length();
			if (strArray.length >= 3) {
				length += strArray[2].length();
			}
			if (strArray.length >= 4) {
				length += strArray[3].length();
			}
			// 比较平均长度，较小的认为是标题
			if (first.length() < (length / 3)) {
				String str = builder.toString();
				if (!str.isEmpty()) {
					sections.add(new Part(PartType.PDF, title, str));
					builder.setLength(0);
				}
				title = first;
			}

			for (String s : strArray) {
				if (StringUtils.isNotEmpty(s) && !s.equals(title) && !s.contains("第") && !s.contains("页")) {
					builder.append(s).append("\n");
				}
			}
		}

		if (!builder.isEmpty()) {
			sections.add(new Part(PartType.PDF, title, builder.toString()));
		}
		return sections;
	}

	public List<Part> extractChapter(MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		Preconditions.checkNotNull(filename);
		if (filename.endsWith(".pdf")) {
			try (InputStream in = file.getInputStream()) {
				return extractPdfTextToSection(in);
			}
		} else if (filename.endsWith(".zip")) {
			return this.extractZip(file, filename);
		}

		throw new IllegalStateException("Unsupported file type: " + filename);
	}

	private List<Part> extractZip(MultipartFile file, String filename) throws IOException {
		List<Part> parts = Lists.newArrayList();
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
			ZipUtil.unzip(zipFile, tmpdirFile);
			this.handleVideoFiles(tmpdirFile, parts);
		} finally {
			FileUtils.deleteDirectory(tmpdirFile);
		}
		return parts;
	}

	private void handleVideoFiles(File tmpdirFile, List<Part> parts) throws IOException {
		AttachGroup group = attachGroupService.findByGroupCode(
				AttachGroup.of(AttachTypeEnum.EXAM_VIDEO).getGroupCode());
		File[] files = tmpdirFile.listFiles();
		Preconditions.checkNotNull(files);
		// 遍历所有 mp4 文件
		for (File file : files) {
			Attachment a = this.doHandleVideoFile(file, group);
			if (a != null) {
				// 生成章节内容
				String title = FilenameUtils.getBaseName(a.getAttachName());
				String content = a.getUrl();
				Part part = new Part(PartType.VIDEO, title, content);
				parts.add(part);
			}
		}
	}

	private Attachment doHandleVideoFile(File f, AttachGroup group) {
		String fileName = f.getName();
		if (!fileName.endsWith(".mp4")) {
			return null;
		}

		Attachment a;
		try {
			BytesUploadContext c = new BytesUploadContext();
			c.setGroup(group);
			c.setFileName(fileName);
			c.setBytes(FileUtils.readFileToByteArray(f));
			c.setOriginalFilename(fileName);
			c.setUser(SysUtil.getUser());
			c.setTenantCode(SysUtil.getTenantCode());
			// 上传文件
			a = this.attachmentManager.upload(c);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return a;
	}

	@Data
	public static final class Part {

		private Integer type;
		private String title;
		private String content;

		public Part(Integer type, String title, String content) {
			this.type = type;
			this.title = title;
			this.content = content;
		}
	}

	public static final class PartType {
		public static final Integer PDF = 1;
		public static final Integer VIDEO = 0;
	}
}
