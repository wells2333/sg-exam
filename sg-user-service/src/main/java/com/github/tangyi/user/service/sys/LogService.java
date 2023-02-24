package com.github.tangyi.user.service.sys;

import com.github.tangyi.api.user.service.ILogService;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.user.mapper.sys.LogMapper;
import org.springframework.stereotype.Service;

@Service
public class LogService extends CrudService<LogMapper, Log> implements ILogService {
}
