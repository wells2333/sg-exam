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
import com.github.tangyi.common.exceptions.CommonException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AttachmentManager {

    Attachment upload(MultipartFileUploadContext context) throws IOException;

    Attachment upload(BytesUploadContext context);

    Attachment prepareUploadChunks(AttachGroup group, Attachment attachment);

    Boolean uploadChunk(ChunkUploadContext context) throws IOException;

    Attachment mergeChunks(String hash) throws CommonException;

    boolean delete(Attachment attachment) throws IOException;

    boolean deleteAll(AttachGroup group, List<Attachment> attachments) throws IOException;

    String getDownloadUrl(AttachGroup group, String attachName, String hash);

    String getPreviewUrl(Long id);

	String getPreviewUrlIgnoreException(Long id);

	Attachment getPreviewAttachment(Long id);

    Long defaultImage(String groupCode);
}
