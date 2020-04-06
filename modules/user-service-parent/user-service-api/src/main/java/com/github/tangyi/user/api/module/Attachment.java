package com.github.tangyi.user.api.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tangyi.common.core.persistence.BaseEntity;
import com.github.tangyi.user.api.constant.AttachmentConstant;
import lombok.Data;

/**
 * 附件信息
 *
 * @author tangyi
 * @date 2018/10/30 20:47
 */
@Data
public class Attachment extends BaseEntity<Attachment> {

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件大小
     */
    private String attachSize;

    /**
     * 附件类型
     */
    private String attachType;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 文件ID
     */
    @JsonIgnore
    private String fastFileId;

    /**
     * 业务流水号
     */
    private String busiId;

    /**
     * 业务类型
     */
    private String busiType = AttachmentConstant.BUSI_TYPE_NORMAL_ATTACHMENT;

    /**
     * 业务模块
     */
    private String busiModule;

    /**
     * 预览地址
     */
    private String previewUrl;

    /**
     * 上传类型，1：本地目录，2：fastDfs，3：七牛云
     */
    private Integer uploadType;

    /**
     * 上传结果
     */
    private String uploadResult;
}
