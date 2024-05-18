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

package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.oss.exceptions.OssException;

import java.io.IOException;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface AttachmentStorage {

    Attachment prepare(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
                       String tenantCode, String hash);

    Attachment upload(MultipartFileUploadContext context) throws IOException;

    String uploadChunk(ChunkUploadContext context) throws IOException;

    Attachment mergeChunks(Attachment prepare, AttachGroup group, List<SysAttachmentChunk> chunks) throws CommonException;

    Attachment upload(BytesUploadContext context);

    boolean delete(Attachment attachment) throws IOException;

    boolean deleteAll(List<Attachment> attachments) throws IOException;

    String getDownloadUrl(AttachGroup group, String attachName, String hash);

    String getDownloadUrl(String fileName, long expire);

    String getPreviewUrl(Long id);

    Attachment getPreviewAttachment(Long id);

    Long defaultImage(String groupCode);
}
