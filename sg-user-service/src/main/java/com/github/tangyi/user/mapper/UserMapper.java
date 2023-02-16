package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.User;
import com.github.tangyi.common.base.CrudMapper;
import com.github.tangyi.common.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends CrudMapper<User> {

	Integer userCount(UserVo userVo);

	User findUserByPhone(@Param("phone") String phone, @Param("tenantCode") String tenantCode);
}
