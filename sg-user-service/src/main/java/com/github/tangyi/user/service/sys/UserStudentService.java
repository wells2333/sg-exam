package com.github.tangyi.user.service.sys;

import com.github.tangyi.api.user.model.UserStudent;
import com.github.tangyi.api.user.service.IUserStudentService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.user.mapper.sys.UserStudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Service
public class UserStudentService extends CrudService<UserStudentMapper, UserStudent> implements IUserStudentService {

	public List<UserStudent> getByUserId(@NotBlank String userId) {
		return this.dao.getByUserId(userId);
	}

	public UserStudent getByStudentId(@NotBlank String studentId) {
		return this.dao.getByStudentId(studentId);
	}

	@Transactional
	public int deleteByUserId(@NotBlank String userId) {
		return this.dao.deleteByUserId(userId);
	}

	@Transactional
	public int deleteByStudentId(@NotBlank String studentId) {
		return this.dao.deleteByStudentId(studentId);
	}
}
