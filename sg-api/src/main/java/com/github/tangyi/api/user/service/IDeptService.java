package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.dto.DeptDto;
import com.github.tangyi.api.user.model.Dept;
import com.github.tangyi.common.service.ICrudService;
import com.github.tangyi.common.vo.DeptVo;

import java.util.List;

public interface IDeptService extends ICrudService<Dept> {

	List<DeptVo> findById(Long[] ids);

	List<DeptDto> deptList();
}
