package com.github.tangyi.exam.service.subject.media;

import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.user.enums.AttachTypeEnum;
import com.github.tangyi.user.service.QiNiuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 *
 * @author tangyi
 * @date 2022/11/8 8:40 下午
 */
@Slf4j
@AllArgsConstructor
@Service
public class SubjectMediaService {

	private final QiNiuService qiNiuService;

	public Attachment uploadVideo(MultipartFile file) {
		try {
			return qiNiuService.upload(file, AttachTypeEnum.EXAM_VIDEO.getValue(), SysUtil.getUser(),
					SysUtil.getTenantCode());
		} catch (IOException e) {
			log.error("upload video failed", e);
		}
		return null;
	}
}
