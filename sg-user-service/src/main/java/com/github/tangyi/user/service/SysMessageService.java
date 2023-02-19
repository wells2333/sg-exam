package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.api.user.service.ISysMessageService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.SysMessageMapper;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysMessageService extends CrudService<SysMessageMapper, SysMessage>
		implements ISysMessageService, UserCacheName {

	private static final Integer RECEIVER_TYPE_PART_USER = 1;

	private final SysMessageReceiverService receiverService;

	@Override
	@Cacheable(value = MESSAGE, key = "#id")
	public SysMessage get(Long id) {
		SysMessage sysMessage = super.get(id);
		if (sysMessage != null && RECEIVER_TYPE_PART_USER.equals(sysMessage.getReceiverType())) {
			sysMessage.setReceivers(getReceivers(id));
		}
		return sysMessage;
	}

	@Override
	public SysMessage getByMessageType(Integer type) {
		return this.dao.getByMessageType(type);
	}

	@Transactional
	public int addMessage(SysMessage sysMessage) {
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		sysMessage.setCommonValue(user, tenantCode);
		sysMessage.setSender(SysUtil.getUserName());
		int update = insert(sysMessage);
		Long messageId = sysMessage.getId();
		addMessageReceivers(messageId, sysMessage.getType(), sysMessage.getReceivers(), user, tenantCode);
		log.info("Add message done successfully, messageId: {}", messageId);
		return update;
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
		SysMessage dbMessage = this.get(sysMessage.getId());
		receiverService.deleteByMessageId(dbMessage.getId());
		addMessageReceivers(dbMessage.getId(), dbMessage.getType(), sysMessage.getReceivers(), sysMessage.getOperator(),
				sysMessage.getTenantCode());
		return super.update(sysMessage);
	}

	@Override
	@Transactional
	@CacheEvict(value = MESSAGE, key = "#sysMessage.id")
	public int delete(SysMessage sysMessage) {
		receiverService.deleteByMessageId(sysMessage.getId());
		return super.delete(sysMessage);
	}

	@Override
	@Transactional
	@CacheEvict(value = MESSAGE, allEntries = true)
	public int deleteAll(Long[] ids) {
		for (Long id : ids) {
			receiverService.deleteByMessageId(id);
		}
		return super.deleteAll(ids);
	}

	@Transactional
	public void addMessageReceivers(Long messageId, Integer type, List<Long> receiverIds, String user,
			String tenantCode) {
		if (CollectionUtils.isEmpty(receiverIds)) {
			return;
		}
		for (List<Long> temp : Lists.partition(receiverIds, 50)) {
			List<SysMessageReceiver> receivers = temp.stream().map(e -> {
				SysMessageReceiver receiver = new SysMessageReceiver();
				receiver.setCommonValue(user, tenantCode);
				receiver.setReceiverId(e);
				receiver.setMessageId(messageId);
				receiver.setType(type);
				return receiver;
			}).collect(Collectors.toList());
			receiverService.insertBatch(receivers);
			log.info("Add message receiver successfully, messageId: {}, receiver size: {}", messageId,
					receivers.size());
		}
	}

	private List<Long> getReceivers(Long messageId) {
		List<SysMessageReceiver> receivers = receiverService.getByMessageId(messageId);
		if (CollectionUtils.isNotEmpty(receivers)) {
			return receivers.stream().map(SysMessageReceiver::getReceiverId).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
}
