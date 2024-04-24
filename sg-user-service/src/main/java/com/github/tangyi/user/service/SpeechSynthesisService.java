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

package com.github.tangyi.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.model.SpeechSynthesis;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.mapper.SpeechSynthesisMapper;
import com.github.tangyi.user.service.attach.AttachmentService;
import com.github.tangyi.user.speech.BaiduSpeechSynthesisService;
import com.github.tangyi.user.speech.SynthesisHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SpeechSynthesisService extends CrudService<SpeechSynthesisMapper, SpeechSynthesis> {

	private final AttachmentService attachmentService;
	private final AttachmentManager attachmentManager;
	private final BaiduSpeechSynthesisService baiduSpeechSynthesisService;

	@Transactional
	public boolean addSynthesis(SpeechSynthesis speechSynthesis) {
		synthesis(speechSynthesis);
		return insert(speechSynthesis) > 0;
	}

	@Transactional
	public boolean updateSynthesis(SpeechSynthesis speechSynthesis) {
		speechSynthesis.setCommonValue(SysUtil.getUser(), SysUtil.getTenantCode());
		SpeechSynthesis old = get(speechSynthesis.getId());
		if (old != null && !old.getText().equals(speechSynthesis.getText())) {
			synthesis(speechSynthesis);
			deleteAudioFile(old);
		}
		return update(speechSynthesis) > 0;
	}

	@Transactional
	public void synthesis(SpeechSynthesis speechSynthesis) {
		String tenantCode = SysUtil.getTenantCode();
		String user = SysUtil.getUser();
		speechSynthesis.setCommonValue(user, tenantCode);
		SynthesisHandlerContext context = new SynthesisHandlerContext();
		// 截取前 5 个字符 + UUID 作为文件名
		String fileName = speechSynthesis.getText().substring(0, Math.min(5, speechSynthesis.getText().length())) + "_"
				+ UUID.randomUUID() + ".mp3";
		context.setFileName(fileName);
		context.setTenantCode(tenantCode);
		context.setUser(user);
		baiduSpeechSynthesisService.synthesisAndUpLoad(speechSynthesis.getText(), context);
		Attachment attachment = context.getAttachment();
		Optional.ofNullable(attachment).ifPresent(attach -> speechSynthesis.setAttachId(attach.getId()));
	}

	@Override
	public PageInfo<SpeechSynthesis> findPage(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<SpeechSynthesis> pageInfo = super.findPage(params, pageNum, pageSize);
		if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
			for (SpeechSynthesis synthesis : pageInfo.getList()) {
				Optional.ofNullable(attachmentService.get(synthesis.getAttachId()))
						.ifPresent(attachment -> synthesis.setUrl(attachment.getUrl()));
			}
		}
		return pageInfo;
	}

	@Override
	@Transactional
	public int delete(SpeechSynthesis speechSynthesis) {
		super.delete(speechSynthesis);
		deleteAudioFile(speechSynthesis);
		return 1;
	}

	@Transactional
	public void deleteAudioFile(SpeechSynthesis speechSynthesis) {
		if (speechSynthesis.getAttachId() == null) {
			return;
		}
		Attachment attachment = attachmentService.get(speechSynthesis.getAttachId());
		try {
			attachmentManager.delete(attachment);
		} catch (Exception e) {
			log.error("delete attachment file failed", e);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public Attachment uploadSpeech(MultipartFile file) throws IOException {
		return attachmentManager.upload(MultipartFileUploadContext.of(AttachTypeEnum.SPEECH, file));
	}
}
