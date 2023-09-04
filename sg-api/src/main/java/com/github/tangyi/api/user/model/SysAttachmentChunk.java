package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 附件分块 sys_attachment_chunk
 */
@Data
@Table(name = "sys_attachment_chunk")
@EqualsAndHashCode(callSuper = true)
public class SysAttachmentChunk extends BaseEntity<SysAttachmentChunk> {

    /**
     * 分块名称
     */
    @Column(name = "chunk_name")
    private String chunkName;

    /**
     * 分块编号
     */
    @Column(name = "chunk_number")
    private Integer chunkNumber;

    /**
     * 分块大小
     */
    @Column(name = "chunk_data_size")
    private Integer chunkDataSize;

    /**
     * 分块状态，0：待上传，1：上传成功，2：上传失败，3：已合并
     */
    @Column(name = "chunk_status")
    private Integer chunkStatus;

    /**
     * 分块上传返回值
     */
    @Column(name = "chunk_upload_res")
    private String chunkUploadRes;

    /**
     * 哈希值
     */
    @Column(name = "hash")
    private String hash;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}