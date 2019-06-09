package com.github.tangyi.user.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.user.api.module.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户mapper接口
 *
 * @author tangyi
 * @date 2018-08-25 15:27
 */
@Mapper
public interface UserMapper extends CrudMapper<User> {

    UserVo selectUserVo();

    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username   用户名
     * @param tenantCode 租户标识
     * @return userVo
     */
    UserVo selectUserVoByUsername(@Param("username") String username, @Param("tenantCode") String tenantCode);

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return Integer
     */
    Integer userCount(UserVo userVo);
}
