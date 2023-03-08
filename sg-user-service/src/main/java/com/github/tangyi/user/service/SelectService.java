package com.github.tangyi.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.dto.DeptDto;
import com.github.tangyi.api.common.IdNameDto;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.IDeptService;
import com.github.tangyi.api.user.service.IUserService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class SelectService {

	private final IUserService userService;

	private final IDeptService deptService;

	public PageInfo<IdNameDto> userList(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<IdNameDto> dtoPage = new PageInfo<>();
		List<IdNameDto> dtoList = Lists.newArrayList();
		PageInfo<User> userPage = userService.findPage(params, pageNum, pageSize);
		List<User> users = userPage.getList();
		if (CollectionUtils.isNotEmpty(users)) {
			for (User user : users) {
				IdNameDto dto = new IdNameDto();
				dto.setId(user.getId());
				dto.setName(user.getName());
				dtoList.add(dto);
			}
		}
		dtoPage.setList(dtoList);
		return dtoPage;
	}

	public List<DeptDto> deptList() {
		return deptService.deptList();
	}
}
