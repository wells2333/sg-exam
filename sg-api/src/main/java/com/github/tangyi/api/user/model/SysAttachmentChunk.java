/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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