package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachGroupMapper extends CrudMapper<AttachGroup> {

	AttachGroup findByIdentifier(String groupIdentifier);
}
