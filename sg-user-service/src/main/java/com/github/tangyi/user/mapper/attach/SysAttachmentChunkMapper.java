package com.github.tangyi.user.mapper.attach;

import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAttachmentChunkMapper extends CrudMapper<SysAttachmentChunk> {

    List<SysAttachmentChunk> findByHash(@Param("hash") String hash, @Param("tenantCode") String tenantCode);

    int deleteByHash(@Param("hash") String hash, @Param("tenantCode") String tenantCode);
}
