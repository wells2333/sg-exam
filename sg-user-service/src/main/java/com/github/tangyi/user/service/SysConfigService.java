package com.github.tangyi.user.service;

import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.model.SysConfig;
import com.github.tangyi.api.user.service.ISysConfigService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.constants.ConfigKey;
import com.github.tangyi.user.mapper.SysConfigMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class SysConfigService extends CrudService<SysConfigMapper, SysConfig>
		implements ISysConfigService, UserCacheName, ConfigKey {

	private static final List<String> SYS_DEFAULT_KEYS = Lists.newArrayList(SYS_WEB_NAME, SYS_WEB_MAIN_TITLE,
			SYS_WEB_SUB_TITLE_ONE, SYS_WEB_SUB_TITLE_TWO, SYS_AVATAR, SYS_ADMIN_MAIN_TITLE, SYS_ADMIN_SUB_TITLE,
			SYS_WXAPP_AVATAR, SYS_WXAPP_MAIN_TITLE, SYS_WXAPP_SUB_TITLE);

	@Override
	@Cacheable(value = SYS_CONFIG, key = "#id")
	public SysConfig get(Long id) {
		return super.get(id);
	}

	@Override
	public SysConfig getByKey(String key, String tenantCode) {
		return this.dao.getByKey(key, tenantCode);
	}

	@Override
	public List<SysConfig> batchGetByKey(List<String> keys, String tenantCode) {
		return this.dao.batchGetByKey(keys, tenantCode);
	}

	@Override
	public Map<String, Object> getDefaultSysConfig() {
		Map<String, Object> map = Maps.newHashMapWithExpectedSize(SYS_DEFAULT_KEYS.size());
		List<SysConfig> list = this.batchGetByKey(SYS_DEFAULT_KEYS, TenantConstant.DEFAULT_TENANT_CODE);
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysConfig config : list) {
				map.put(config.getConfigKey(), config.getConfigValue());
			}
		}
		return map;
	}

	@Override
	@Transactional
	public int insert(SysConfig sysConfig) {
		sysConfig.setCommonValue();
		return super.insert(sysConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = SYS_CONFIG, key = "#sysConfig.id")
	public int update(SysConfig sysConfig) {
		sysConfig.setCommonValue();
		return super.update(sysConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = SYS_CONFIG, key = "#sysConfig.id")
	public int delete(SysConfig sysConfig) {
		return super.delete(sysConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = SYS_CONFIG, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
