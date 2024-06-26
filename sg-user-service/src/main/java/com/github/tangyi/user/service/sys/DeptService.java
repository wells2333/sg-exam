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

import cn.hutool.core.collection.CollUtil;
import com.beust.jcommander.internal.Lists;
import com.github.tangyi.api.user.dto.DeptDto;
import com.github.tangyi.api.user.model.Dept;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.IDeptService;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TreeUtil;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.user.mapper.sys.DeptMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DeptService extends CrudService<DeptMapper, Dept> implements IDeptService {

	@Transactional
	@Override
	public int delete(Dept dept) {
		return super.delete(dept);
	}

	public List<Dept> getListByUsers(List<User> userList) {
		if (CollectionUtils.isEmpty(userList)) {
			return Collections.emptyList();
		}
		Long[] deptIds = userList.stream().map(User::getDeptId).filter(Objects::nonNull).distinct().toArray(Long[]::new);
		return this.findListById(deptIds);
	}

	/**
	 * 查询树形部门集合
	 */
	@Override
	public List<DeptDto> deptList(Map<String, Object> condition) {
		Dept dept = new Dept();
		Optional.ofNullable(condition.get("deptName")).ifPresent(deptName -> dept.setDeptName(deptName.toString()));
		dept.setTenantCode(SysUtil.getTenantCode());
		Stream<Dept> deptStream = this.findList(dept).stream();
		if (Optional.ofNullable(deptStream).isPresent()) {
			List<DeptDto> deptTreeList = deptStream.map(DeptDto::new).collect(Collectors.toList());
			return TreeUtil.buildTree(CollUtil.sort(deptTreeList, Comparator.comparingInt(DeptDto::getSort)),
					CommonConstant.ROOT);
		}
		return Collections.emptyList();
	}

	public List<DeptVo> findById(Long[] ids) {
		Stream<Dept> deptStream = findListById(ids).stream();
		if (Optional.ofNullable(deptStream).isPresent()) {
			return deptStream.map(tempDept -> {
				DeptVo tempDeptVo = new DeptVo();
				BeanUtils.copyProperties(tempDept, tempDeptVo);
				return tempDeptVo;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
}
