package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.User;
import com.github.tangyi.common.service.ICrudService;
import com.github.tangyi.common.vo.UserVo;

import java.util.List;

public interface IUserService extends ICrudService<User> {

	List<UserVo> findUserVoListById(Long[] ids);
}
