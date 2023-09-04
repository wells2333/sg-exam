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
