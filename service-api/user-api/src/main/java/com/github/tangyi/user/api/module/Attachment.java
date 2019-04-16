package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * 附件信息
 *
 * @author tangyi
 * @date 2018/10/30 20:47
 */
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

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachSize() {
        return attachSize;
    }

    public void setAttachSize(String attachSize) {
        this.attachSize = attachSize;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFastFileId() {
        return fastFileId;
    }

    public void setFastFileId(String fastFileId) {
        this.fastFileId = fastFileId;
    }

    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getBusiModule() {
        return busiModule;
    }

    public void setBusiModule(String busiModule) {
        this.busiModule = busiModule;
    }
}
