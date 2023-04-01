package com.github.tangyi.exam.service.media;

import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.Attachment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ExamMediaService {

	private final AttachmentManager attachmentManager;

	public Attachment uploadVideo(MultipartFile file) {
		return upload(file, AttachTypeEnum.EXAM_VIDEO);
	}

	public Attachment uploadImage(MultipartFile file) {
		return upload(file, AttachTypeEnum.EXAM_IMAGE);
	}

	public Attachment upload(MultipartFile file, AttachTypeEnum type) {
		try {
			return attachmentManager.upload(MultipartFileUploadContext.of(type, file));
		} catch (IOException e) {
			log.error("upload exam media failed, type: {}", type.getDesc(), e);
		}
		return null;
	}

	public String imageUrl(Long id) {
		return attachmentManager.getPreviewUrl(id);
	}

	public String videoUrl(Long id) {
		return attachmentManager.getPreviewUrl(id);
	}
}
