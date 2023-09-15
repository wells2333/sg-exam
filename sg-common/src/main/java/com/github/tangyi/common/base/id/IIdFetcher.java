package com.github.tangyi.common.base.id;

import java.util.List;
import java.util.Map;

public interface IIdFetcher {

	List<Long> fetchAll(long minId, Map<String, Object> params);

	List<Long> fetchPage(long minId, Map<String, Object> params);
}
