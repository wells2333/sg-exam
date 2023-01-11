package com.github.tangyi.api.user.enums;

import lombok.Getter;

@Getter
public enum AttachTypeEnum {

	DEFAULT("default", "默认"), DEFAULT_IMAGE("default_image", "默认图片"), AVATAR("user_avatar", "用户头像"), SPEECH("speech",
			"语音合成"), EXAM_VIDEO("exam/video", "考务视频"), EXAM_IMAGE("exam/image", "考务图片"), OTHER("other", "其它");

	private final String value;

	private final String desc;

	AttachTypeEnum(String value, String desc) {
		this.value = value;
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
