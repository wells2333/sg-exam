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

package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.api.user.service.ISysAttachmentChunkService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.user.mapper.attach.SysAttachmentChunkMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SysAttachmentChunkService extends CrudService<SysAttachmentChunkMapper, SysAttachmentChunk> implements ISysAttachmentChunkService, ExamCacheName {

    @Override
    public SysAttachmentChunk get(Long id) {
        return super.get(id);
    }

    @Override
    public List<SysAttachmentChunk> findByHash(String hash, String tenantCode) {
        return this.dao.findByHash(hash, tenantCode);
    }

    @Override
    @Transactional
    public int insert(SysAttachmentChunk sysAttachmentChunk) {
        sysAttachmentChunk.setCommonValue();
        return super.insert(sysAttachmentChunk);
    }

    @Override
    @Transactional
    public int update(SysAttachmentChunk sysAttachmentChunk) {
        sysAttachmentChunk.setCommonValue();
        return super.update(sysAttachmentChunk);
    }

    @Override
    @Transactional
    public int delete(SysAttachmentChunk sysAttachmentChunk) {
        return super.delete(sysAttachmentChunk);
    }

    @Override
    @Transactional
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }

    @Override
    @Transactional
    public int deleteByHash(String hash, String tenantCode) {
        return this.dao.deleteByHash(hash, tenantCode);
    }
}
