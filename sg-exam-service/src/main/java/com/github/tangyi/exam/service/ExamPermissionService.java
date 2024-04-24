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

package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.dto.MemberDto;
import com.github.tangyi.api.exam.model.ExamPermission;
import com.github.tangyi.api.exam.service.IExamPermissionService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.constants.ExamConstant;
import com.github.tangyi.exam.mapper.ExamPermissionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ExamPermissionService extends CrudService<ExamPermissionMapper, ExamPermission>
		implements IExamPermissionService, ExamCacheName {

	@Override
	@Cacheable(value = EXAM_PERMISSION, key = "#id")
	public ExamPermission get(Long id) {
		return super.get(id);
	}

	@Override
	public List<ExamPermission> findPermissionList(Integer examType, Long examId) {
		return this.dao.findPermissionList(examType, examId);
	}

	@Override
	public Integer findCount(Integer examType, Long examId) {
		return this.dao.findCount(examType, examId);
	}

	@Override
	public MemberDto getMembers(Integer examType, Long examId) {
		MemberDto dto = new MemberDto();
		List<ExamPermission> permissions = this.findPermissionList(examType, examId);
		if (CollectionUtils.isNotEmpty(permissions)) {
			List<Long> ids = dto.getUserMembers();
			for (ExamPermission permission : permissions) {
				if (ExamConstant.PERMISSION_ID_TYPE_USER.equals(permission.getMemberType())) {
					ids.add(permission.getMemberId());
				} else if (ExamConstant.PERMISSION_ID_TYPE_DEPT.equals(permission.getMemberType())) {
					dto.setDeptMember(permission.getMemberId().toString());
				}
			}
		}
		return dto;
	}

	@Override
	@Transactional
	public int insert(ExamPermission permission) {
		permission.setCommonValue();
		return super.insert(permission);
	}

	@Override
	@Transactional
	public int insertBatch(List<ExamPermission> members) {
		int res = this.dao.insertBatch(members);
		log.info("Insert examination permission successfully, size: {}", members.size());
		return res;
	}

	@Transactional
	public void addPermissions(Long examId, List<Long> permissionIds, Integer examType, Integer memberType, String user,
			String tenantCode) {
		if (CollectionUtils.isEmpty(permissionIds)) {
			return;
		}

		List<ExamPermission> permissions = permissionIds.stream().map(id -> {
			ExamPermission p = new ExamPermission();
			p.setCommonValue(user, tenantCode);
			p.setExamType(examType);
			p.setExamId(examId);
			p.setMemberId(id);
			p.setMemberType(memberType);
			return p;
		}).collect(Collectors.toList());
		int res = this.insertBatch(permissions);
		log.info("Add permissions successfully, size: {}, update: {}", permissionIds.size(), res);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAM_PERMISSION, key = "#permission.id")
	public int update(ExamPermission permission) {
		permission.setCommonValue();
		return super.update(permission);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAM_PERMISSION, key = "#permission.id")
	public int delete(ExamPermission permission) {
		return super.delete(permission);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAM_PERMISSION, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Override
	@Transactional
	public void deletePermission(Integer examType, Long memberId) {
		this.dao.deletePermission(examType, memberId);
	}
}
