package com.github.tangyi.api.user.enums;

import com.github.tangyi.api.user.constant.AttachmentConstant;
import lombok.Getter;

@Getter
public enum AttachTypeEnum {

	DEFAULT("default", AttachmentConstant.QI_NIU, "默认"), DEFAULT_IMAGE("default_image", AttachmentConstant.QI_NIU,
			"默认图片"), AVATAR("user_avatar", AttachmentConstant.QI_NIU, "用户头像"), SPEECH("speech",
			AttachmentConstant.QI_NIU, "语音合成"), EXAM_VIDEO("exam/video", AttachmentConstant.QI_NIU,
			"考务视频"), EXAM_IMAGE("exam/image", AttachmentConstant.QI_NIU, "考务图片"), OTHER("other",
			AttachmentConstant.QI_NIU, "其它");

	private final String value;

	private final Integer storageType;

	private final String desc;

	AttachTypeEnum(String value, Integer storageType, String desc) {
		this.value = value;
		this.storageType = storageType;
		this.desc = desc;
	}

	public static AttachTypeEnum matchByValue(String value) {
		for (AttachTypeEnum item : AttachTypeEnum.values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return OTHER;
	}
}
