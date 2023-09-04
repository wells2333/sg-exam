package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;

public interface ISysAttachmentChunkService extends ICrudService<SysAttachmentChunk> {

    List<SysAttachmentChunk> findByHash(String hash, String tenantCode);

    int deleteByHash(String hash, String tenantCode);
}

