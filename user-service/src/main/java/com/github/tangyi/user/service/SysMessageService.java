package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.SysMessageMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SysMessageService extends CrudService<SysMessageMapper, SysMessage> implements UserCacheName {

	@Override
	@Cacheable(value = MESSAGE, key = "#id")
	public SysMessage get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(SysMessage sysMessage) {
		sysMessage.setCommonValue();
		return super.insert(sysMessage);
	}

	@Override
	@Transactional
	@CacheEvict(value = MESSAGE, key = "#sysMessage.id")
	public int update(SysMessage sysMessage) {
		sysMessage.setCommonValue();
		return super.update(sysMessage);
	}

	@Override
	@Transactional
	@CacheEvict(value = MESSAGE, key = "#sysMessage.id")
	public int delete(SysMessage sysMessage) {
		return super.delete(sysMessage);
	}

	@Override
	@Transactional
	@CacheEvict(value = MESSAGE, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
