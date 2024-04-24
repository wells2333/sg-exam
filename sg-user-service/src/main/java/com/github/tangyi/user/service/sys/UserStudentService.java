/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
