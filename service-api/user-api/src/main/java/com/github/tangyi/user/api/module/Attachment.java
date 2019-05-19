package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
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
     * 组名称
     */
    private String groupName;

    /**
     * 文件ID
     */
    private String fastFileId;

    /**
     * 业务流水号
     */
    private String busiId;

    /**
     * 业务类型
     */
    private String busiType;

    /**
     * 业务模块
     */
    private String busiModule;
}
