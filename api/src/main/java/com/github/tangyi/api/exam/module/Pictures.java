package com.github.tangyi.api.exam.module;

import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片表
 *
 * @author tangyi
 * @date 2019/6/16 13:52
 */
@EqualsAndHashCode(callSuper = true)
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
