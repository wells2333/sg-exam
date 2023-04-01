package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.AttachmentStorage;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.qiniu.common.QiniuException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LocalAttachmentStorage implements AttachmentStorage {

	@Override
	public Attachment upload(MultipartFileUploadContext context) throws IOException {
		return null;
	}

	@Override
	public Attachment upload(BytesUploadContext context) {
		return null;
	}

	@Override
	public boolean delete(Attachment attachment) throws QiniuException {
		return false;
	}

	@Override
	public boolean deleteAll(List<Attachment> attachments) throws QiniuException {
		return false;
	}

	@Override
	public String getDownloadUrl(AttachGroup group, String attachName) {
		return null;
	}

	@Override
	public String getDownloadUrl(String fileName, long expire) {
		return null;
	}

	@Override
	public String getPreviewUrl(Long id) {
		return null;
	}

	@Override
	public Attachment getPreviewAttachment(Long id) {
		return null;
	}

	@Override
	public Long randomAttachmentId(String groupCode) {
		return null;
	}
}
