package com.github.tangyi.user.service.message;

import com.github.tangyi.api.user.model.SysMessageRead;
import com.github.tangyi.api.user.service.ISysMessageReadService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.user.mapper.massage.SysMessageReadMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SysMessageReadService extends CrudService<SysMessageReadMapper, SysMessageRead>
		implements ISysMessageReadService, ExamCacheName {

	@Override
	public SysMessageRead get(Long id) {
		return super.get(id);
	}

	@Override
	public SysMessageRead findByMessageIdAndReceiverId(Long messageId, Long receiverId) {
		return this.dao.findByMessageIdAndReceiverId(messageId, receiverId);
	}

	@Override
	@Transactional
	public int insert(SysMessageRead sysMessageRead) {
		sysMessageRead.setCommonValue();
		return super.insert(sysMessageRead);
	}

	@Override
	@Transactional
	public int update(SysMessageRead sysMessageRead) {
		sysMessageRead.setCommonValue();
		return super.update(sysMessageRead);
	}

	@Override
	@Transactional
	public int delete(SysMessageRead sysMessageRead) {
		return super.delete(sysMessageRead);
	}

	@Override
	@Transactional
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
