package com.github.tangyi.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.service.ISpeechSynthesisService;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.model.SpeechSynthesis;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.mapper.SpeechSynthesisMapper;
import com.github.tangyi.user.service.attach.AttachmentService;
import com.github.tangyi.user.service.attach.QiNiuService;
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
public class SpeechSynthesisService extends CrudService<SpeechSynthesisMapper, SpeechSynthesis>
		implements ISpeechSynthesisService {

	private final AttachmentService attachmentService;

	private final QiNiuService qiNiuService;

	private final BaiduSpeechSynthesisService baiduSpeechSynthesisService;

	@Transactional
	public boolean addSynthesis(SpeechSynthesis speechSynthesis) throws Exception {
		synthesis(speechSynthesis);
		return insert(speechSynthesis) > 0;
	}

	@Transactional
	public boolean updateSynthesis(SpeechSynthesis speechSynthesis) throws Exception {
		speechSynthesis.setCommonValue(SysUtil.getUser(), SysUtil.getTenantCode());
		SpeechSynthesis old = get(speechSynthesis.getId());
		if (old != null && !old.getText().equals(speechSynthesis.getText())) {
			synthesis(speechSynthesis);
			deleteAudioFile(old);
		}
		return update(speechSynthesis) > 0;
	}

	@Transactional
	public void synthesis(SpeechSynthesis speechSynthesis) throws Exception {
		String tenantCode = SysUtil.getTenantCode();
		String user = SysUtil.getUser();
		speechSynthesis.setCommonValue(user, tenantCode);
		SynthesisHandlerContext context = new SynthesisHandlerContext();
		context.setGroupCode(AttachTypeEnum.SPEECH.getValue());
		// 截取前5个字符 + UUID作为文件名
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
		if (attachment != null) {
			try {
				qiNiuService.delete(attachment);
			} catch (Exception e) {
				log.error("delete attachment file failed", e);
				throw new RuntimeException(e);
			}
		}
	}

	@Transactional
	public Attachment uploadSpeech(MultipartFile file) throws IOException {
		return qiNiuService.upload(file, AttachTypeEnum.SPEECH.getValue(), SysUtil.getUser(), SysUtil.getTenantCode());
	}
}
