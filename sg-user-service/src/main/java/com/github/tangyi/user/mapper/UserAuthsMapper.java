package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthsMapper extends CrudMapper<UserAuths> {

    UserAuths getByIdentifier(UserAuths userAuths);

	UserAuths getByUserId(UserAuths userAuths);

    List<UserAuths> getListByUserIds(@Param("userIds") Long[] userIds);

    int deleteByIdentifier(UserAuths userAuths);

    int deleteByUserId(UserAuths userAuths);

    int insertBatch(List<UserAuths> userAuths);
}
