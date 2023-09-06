package com.github.tangyi.api.user.enums;

import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.common.utils.EnvUtils;
import lombok.Getter;

@Getter
public enum AttachTypeEnum {

	DEFAULT("default", "默认"),
	AVATAR("user_avatar", "用户头像"),
	SPEECH("speech", "语音合成"),
	EXAM_VIDEO("exam/video", "考务视频"),
	EXAM_SPEECH("exam/speech", "考务语音"),
	EXAM_IMAGE("exam/image", "考务图片"),
	OTHER("other", "其它");

	// 默认为 MinIO
	private final int defaultStorageType = EnvUtils.getInt("ATTACH_DEFAULT_STORAGE_TYPE", AttachmentConstant.MINIO);

	private final String value;

	private final Integer storageType;

	private final String desc;

	AttachTypeEnum(String value, String desc) {
		this.value = value;
		this.storageType = defaultStorageType;
		this.desc = desc;
	}

	public int getDefaultStorageType() {
		return defaultStorageType;
	}
}
