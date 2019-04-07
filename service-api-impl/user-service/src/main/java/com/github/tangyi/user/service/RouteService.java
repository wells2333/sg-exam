package com.github.tangyi.user.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.model.Route;
import com.github.tangyi.user.mapper.RouteMapper;
import org.springframework.stereotype.Service;

/**
 * 路由service
 *
 * @author tangyi
 * @date 2019/4/2 15:01
 */
@Service
public class RouteService extends CrudService<RouteMapper, Route> {
}
