package com.github.tangyi.user.enums;

import lombok.Getter;

/**
 * 附件存储类型
 * @author tangyi
 * @date 2020/04/05 14:01
 */
@Getter
public enum AttachUploaderEnum {

    FILE(1, "文件", "com.github.tangyi.user.uploader.FileUploader"), FAST_DFS(2, "FastDfs", "com.github.tangyi.user.uploader.FastDfsUploader"),
    QI_NIU(3, "七牛云", "com.github.tangyi.user.uploader.QiNiuUploader");

    private Integer value;

    private String desc;

    private String implClass;

    AttachUploaderEnum(int value, String desc, String implClass) {
        this.value = value;
        this.desc = desc;
        this.implClass = implClass;
    }

    public static AttachUploaderEnum matchByValue(Integer value) {
        for (AttachUploaderEnum item : AttachUploaderEnum.values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return FILE;
    }
}
