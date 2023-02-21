package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.SysConfig;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;
import java.util.Map;

public interface ISysConfigService extends ICrudService<SysConfig> {

	SysConfig getByKey(String key, String tenantCode);

	List<SysConfig> batchGetByKey(List<String> keys, String tenantCode);

	Map<String, Object> getDefaultSysConfig();
}
