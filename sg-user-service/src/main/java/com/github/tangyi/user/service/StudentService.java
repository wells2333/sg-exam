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

package com.github.tangyi.user.service;

import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.dto.StudentDto;
import com.github.tangyi.api.user.model.Student;
import com.github.tangyi.api.user.model.UserStudent;
import com.github.tangyi.api.user.service.IStudentService;
import com.github.tangyi.api.user.service.IIdentifyService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.user.mapper.StudentMapper;
import com.github.tangyi.user.service.sys.UserStudentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class StudentService extends CrudService<StudentMapper, Student> implements IStudentService {

	private final IIdentifyService identifyService;
	private final UserStudentService userStudentService;

	@Transactional
	public int add(StudentDto studentDto) {
		String currentUser = SysUtil.getUser(), tenantCode = SysUtil.getTenantCode();
		Long userId = studentDto.getUserId();
		if (userId != null) {
			UserVo userVo = identifyService.findUserByIdentifier(null, currentUser, tenantCode);
			SgPreconditions.checkNull(userVo, "Failed to get user info.");
			userId = userVo.getId();
		}
		Student student = new Student();
		BeanUtils.copyProperties(studentDto, student);
		student.setCommonValue(currentUser, tenantCode);
		// 新增用户学生关系
		UserStudent userStudent = new UserStudent();
		userStudent.setCommonValue(currentUser, tenantCode);
		userStudent.setUserId(userId);
		userStudent.setStudentId(student.getId());
		if (studentDto.getRelationshipType() == null) {
			userStudent.setRelationshipType(UserServiceConstant.RELATIONSHIP_TYPE_FATHER);
		}
		userStudentService.insert(userStudent);
		return this.insert(student);
	}
}
