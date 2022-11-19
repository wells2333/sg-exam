package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 附件分组mapper
 *
 * @author tangyi
 * @date 2018/10/30 20:55
 */
@Repository
public interface AttachGroupMapper extends CrudMapper<AttachGroup> {

	AttachGroup findByIdentifier(String groupIdentifier);
}
