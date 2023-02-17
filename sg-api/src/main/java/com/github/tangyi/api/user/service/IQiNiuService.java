package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.qiniu.common.QiniuException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IQiNiuService {

	Attachment upload(MultipartFile file, String groupCode, String user, String tenantCode) throws IOException;

	Attachment upload(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
			String tenantCode);

	void upload(Attachment attachment, byte[] bytes);

	boolean delete(Attachment attachment) throws QiniuException;

	boolean deleteAll(List<Attachment> attachments) throws QiniuException;

	String getDownloadUrl(AttachGroup group, String attachName);

	String getDownloadUrl(String fileName, long expire);

	String getPreviewUrl(Long id);

	Attachment getPreviewAttachment(Long id);

	Long randomImage(String groupCode);
}
