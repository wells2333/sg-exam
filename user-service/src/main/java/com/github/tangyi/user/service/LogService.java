package com.github.tangyi.user.service;

import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.user.mapper.LogMapper;
import org.springframework.stereotype.Service;

/**
 * 日志service
 *
 * @author tangyi
 * @date 2018/10/31 20:43
 */
@Service
public class LogService extends CrudService<LogMapper, Log> {
}
