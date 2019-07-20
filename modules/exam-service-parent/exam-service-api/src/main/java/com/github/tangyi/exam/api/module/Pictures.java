package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 图片表
 *
 * @author tangyi
 * @date 2019/6/16 13:52
 */
@Data
public class Pictures extends BaseEntity<Pictures> {

    /**
     * 图片地址
     */
    private String pictureAddress;

    /**
     * 附件ID
     */
    private String attachmentId;
}
