package com.github.tangyi.user.service.message;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.api.user.service.ISysMessageReceiverService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.massage.SysMessageReceiverMapper;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class SysMessageReceiverService extends CrudService<SysMessageReceiverMapper, SysMessageReceiver>
		implements ISysMessageReceiverService, UserCacheName {

	@Override
	public List<SysMessageReceiver> getByMessageId(Long messageId) {
		return this.dao.getByMessageId(messageId);
	}

	@Override
	public List<SysMessageReceiver> getPublishedMessage(Long receiverId) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
		params.put("receiverId", receiverId);
		return this.dao.getPublishedMessage(params);
	}

	@Override
	public PageInfo<SysMessageReceiver> getPublishedMessage(Map<String, Object> params, int pageNum, int pageSize) {
		commonPageParam(params, pageNum, pageSize);
		return new PageInfo<>(this.dao.getPublishedMessage(params));
	}

	@Override
	public SysMessageReceiver get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(SysMessageReceiver sysMessageReceiver) {
		sysMessageReceiver.setCommonValue();
		return super.insert(sysMessageReceiver);
	}

	@Override
	@Transactional
	public int insertBatch(List<SysMessageReceiver> receivers) {
		return this.dao.insertBatch(receivers);
	}

	@Override
	@Transactional
	public int update(SysMessageReceiver sysMessageReceiver) {
		sysMessageReceiver.setCommonValue();
		return super.update(sysMessageReceiver);
	}

	@Override
	@Transactional
	public int delete(SysMessageReceiver sysMessageReceiver) {
		return super.delete(sysMessageReceiver);
	}

	@Override
	@Transactional
	public int deleteByMessageId(Long messageId) {
		return this.dao.deleteByMessageId(messageId);
	}

	@Override
	@Transactional
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
