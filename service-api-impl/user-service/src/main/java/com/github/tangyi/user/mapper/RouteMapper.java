package com.github.tangyi.user.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.common.core.model.Route;
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
