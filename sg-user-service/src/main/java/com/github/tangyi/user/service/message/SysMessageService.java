package com.github.tangyi.user.service.message;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.ISysMessageService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.massage.SysMessageMapper;
import com.github.tangyi.user.service.sys.UserService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysMessageService extends CrudService<SysMessageMapper, SysMessage>
		implements ISysMessageService, UserCacheName {

	private static final Integer RECEIVER_TYPE_ALL_USER = 0;

	private static final Integer RECEIVER_TYPE_PART_USER = 1;

	private static final Integer RECEIVER_TYPE_DEPT = 2;

	private final SysMessageReceiverService receiverService;

	private final SysMessageReadService messageReadService;

	private final UserService userService;

	@Override
	@Cacheable(value = MESSAGE, key = "#id")
	public SysMessage get(Long id) {
		SysMessage sysMessage = super.get(id);
		if (sysMessage != null) {
			List<Long> receivers = getReceivers(id);
			if (CollectionUtils.isNotEmpty(receivers)) {
				if (RECEIVER_TYPE_PART_USER.equals(sysMessage.getReceiverType())) {
					sysMessage.setReceivers(receivers);
				} else if (RECEIVER_TYPE_DEPT.equals(sysMessage.getReceiverType())) {
					sysMessage.setReceiverDeptId(receivers.get(0).toString());
				}
			}
		}
		return sysMessage;
	}

	@Override
	public SysMessage getByMessageType(Integer type) {
		return this.dao.getByMessageType(type);
	}

	@Override
	public PageInfo<SysMessage> getPublishedMessage(Map<String, Object> params, int pageNum, int pageSize) {
		Long userId = SysUtil.getUserId();
		PageInfo<SysMessage> page = new PageInfo<>();
		List<SysMessage> list = Lists.newArrayListWithExpectedSize(pageSize);
		PageInfo<SysMessageReceiver> receiverPage = receiverService.getPublishedMessage(params, pageNum, pageSize);
		if (CollectionUtils.isNotEmpty(receiverPage.getList())) {
			getMessageByReceivers(userId, receiverPage.getList(), list);
		}
		List<SysMessage> allUserMessages = getPublishedMessageAllUsers(params);
		if (CollectionUtils.isNotEmpty(allUserMessages)) {
			list.addAll(allUserMessages);
		}
		User user = userService.get(userId);
		if (user != null && user.getDeptId() != null) {
			List<SysMessage> deptMessages = getPublishedMessageDept(params, user.getDeptId());
			if (CollectionUtils.isNotEmpty(deptMessages)) {
				list.addAll(deptMessages);
			}
		}
		list.sort((o1, o2) -> {
			if (o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime() > 0) {
				return 1;
			} else {
				return -1;
			}
		});
		BeanUtils.copyProperties(receiverPage, page);
		page.setList(list);
		return page;
	}

	@Transactional
	public int addMessage(SysMessage sysMessage) {
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		sysMessage.setCommonValue(user, tenantCode);
		sysMessage.setSender(SysUtil.getUserName());
		int update = insert(sysMessage);
		Long messageId = sysMessage.getId();
		addMessageReceivers(messageId, sysMessage.getType(), sysMessage.getReceivers(), sysMessage.getReceiverDeptId(),
				user, tenantCode);
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
		if (sysMessage.getStatus() == 1) {
			addMessageReceivers(dbMessage.getId(), dbMessage.getType(), sysMessage.getReceivers(),
					sysMessage.getReceiverDeptId(), sysMessage.getOperator(), sysMessage.getTenantCode());
		}
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
	public void addMessageReceivers(Long messageId, Integer type, List<Long> receiverIds, String receiverDeptId,
			String user, String tenantCode) {
		if (CollectionUtils.isNotEmpty(receiverIds)) {
			addMessageReceiverUsers(messageId, type, receiverIds, user, tenantCode);
		} else if (StringUtils.isNotEmpty(receiverDeptId)) {
			addMessageReceiverDept(messageId, type, receiverDeptId, user, tenantCode);
		}
	}

	@Transactional
	public void addMessageReceiverUsers(Long messageId, Integer type, List<Long> receiverIds, String user,
			String tenantCode) {
		for (List<Long> temp : Lists.partition(receiverIds, 50)) {
			List<SysMessageReceiver> receivers = temp.stream()
					.map(e -> SysMessageReceiver.of(user, tenantCode, messageId, e, type)).collect(Collectors.toList());
			receiverService.insertBatch(receivers);
			log.info("Add message receiver successfully, messageId: {}, receiver size: {}", messageId,
					receivers.size());
		}
	}

	@Transactional
	public void addMessageReceiverDept(Long messageId, Integer type, String receiverDeptId, String user,
			String tenantCode) {
		SysMessageReceiver receiver = SysMessageReceiver.of(user, tenantCode, messageId, Long.valueOf(receiverDeptId),
				type);
		receiverService.insertBatch(Collections.singletonList(receiver));
		log.info("Add message dept receiver successfully, messageId: {}, deptId: {}", messageId, receiverDeptId);
	}

	private List<Long> getReceivers(Long messageId) {
		List<SysMessageReceiver> receivers = receiverService.getByMessageId(messageId);
		if (CollectionUtils.isNotEmpty(receivers)) {
			return receivers.stream().map(SysMessageReceiver::getReceiverId).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private void getMessageByReceivers(Long userId, List<SysMessageReceiver> receivers, List<SysMessage> list) {
		for (SysMessageReceiver receiver : receivers) {
			SysMessage message = this.get(receiver.getMessageId());
			if (message != null) {
				if (messageReadService.findByMessageIdAndReceiverId(message.getId(), userId) != null) {
					message.setHasRead(true);
				}
				list.add(message);
			}
		}
	}

	private List<SysMessage> getPublishedMessageAllUsers(Map<String, Object> params) {
		SysMessage condition = SysMessage.of(SysUtil.getTenantCode(), 1, RECEIVER_TYPE_ALL_USER);
		Object type = params.get("type");
		if (type != null) {
			condition.setType(Integer.valueOf(type.toString()));
		}
		return this.findAllList(condition);
	}

	private List<SysMessage> getPublishedMessageDept(Map<String, Object> params, Long deptId) {
		List<SysMessage> list = Lists.newArrayList();
		List<SysMessageReceiver> receivers = receiverService.getPublishedMessage(deptId);
		if (CollectionUtils.isNotEmpty(receivers)) {
			Long[] messageIds = receivers.stream().map(SysMessageReceiver::getMessageId).distinct()
					.toArray(Long[]::new);
			List<SysMessage> messages = this.findListById(messageIds);
			if (CollectionUtils.isNotEmpty(messages)) {
				Object type = params.get("type");
				final Integer messageType = type != null ? Integer.valueOf(type.toString()) : null;
				messages.stream().filter(e -> {
					if (messageType != null) {
						return messageType.equals(e.getType()) && e.getReceiverType().equals(RECEIVER_TYPE_DEPT);
					}
					return e.getReceiverType().equals(RECEIVER_TYPE_DEPT);
				}).forEach(list::add);
			}
		}
		return list;
	}
}
