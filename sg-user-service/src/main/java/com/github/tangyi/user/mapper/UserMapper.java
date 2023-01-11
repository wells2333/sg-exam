package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.User;
import com.github.tangyi.common.base.CrudMapper;
import com.github.tangyi.common.vo.UserVo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends CrudMapper<User> {

    /**
     * 查询用户数量
     */
    Integer userCount(UserVo userVo);
}
