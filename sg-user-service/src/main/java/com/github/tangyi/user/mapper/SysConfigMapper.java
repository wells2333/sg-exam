package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.SysConfig;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysConfigMapper extends CrudMapper<SysConfig> {

	SysConfig getByKey(@Param("key") String key, @Param("tenantCode") String tenantCode);

	List<SysConfig> batchGetByKey(@Param("keys") List<String> keys, @Param("tenantCode") String tenantCode);
}
