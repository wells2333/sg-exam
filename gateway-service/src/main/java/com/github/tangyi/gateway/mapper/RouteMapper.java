package com.github.tangyi.gateway.mapper;

import com.github.tangyi.gateway.module.Route;
import com.github.tangyi.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Route mapper
 *
 * @author tangyi
 * @date 2019/4/2 15:00
 */
@Mapper
public interface RouteMapper extends CrudMapper<Route> {
}
