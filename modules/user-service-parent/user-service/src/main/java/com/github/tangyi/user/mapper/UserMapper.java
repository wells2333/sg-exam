package com.github.tangyi.user.mapper;

import com.github.tangyi.common.basic.vo.UserVo;
import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.user.api.module.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户mapper接口
 *
 * @author tangyi
 * @date 2018-08-25 15:27
 */
@Mapper
public interface UserMapper extends CrudMapper<User> {

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return Integer
     */
    Integer userCount(UserVo userVo);
}
