package com.github.tangyi.user.mapper.attach;

import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentMapper extends CrudMapper<Attachment> {

	Attachment findByHash(@Param("hash") String hash, @Param("tenantCode") String tenantCode);
}
