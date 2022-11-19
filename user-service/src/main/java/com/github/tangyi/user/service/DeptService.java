package com.github.tangyi.user.service;

import cn.hutool.core.collection.CollUtil;
import com.beust.jcommander.internal.Lists;
import com.github.tangyi.api.user.dto.DeptDto;
import com.github.tangyi.api.user.model.Dept;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TreeUtil;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.user.mapper.DeptMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 部门service
 *
 * @author tangyi
 * @date 2018/8/26 22:46
 */
@Service
public class DeptService extends CrudService<DeptMapper, Dept> {

	@Transactional
	@Override
	public int delete(Dept dept) {
		return super.delete(dept);
	}

	/**
	 * 根据用户批量查询
	 *
	 * @param userList userList
	 * @return List
	 * @author tangyi
	 * @date 2019/07/03 22:06:50
	 */
	public List<Dept> getListByUsers(List<User> userList) {
		return this.findListById(
				userList.stream().map(User::getDeptId).filter(Objects::nonNull).distinct().toArray(Long[]::new));
	}

	/**
	 * 查询树形部门集合
	 *
	 * @return List
	 * @author tangyi
	 * @date 2018/10/25 12:57
	 */
	public List<DeptDto> deptList() {
		Dept dept = new Dept();
		dept.setTenantCode(SysUtil.getTenantCode());
		Stream<Dept> deptStream = this.findList(dept).stream();
		if (Optional.ofNullable(deptStream).isPresent()) {
			List<DeptDto> deptTreeList = deptStream.map(DeptDto::new).collect(Collectors.toList());
			return TreeUtil.buildTree(CollUtil.sort(deptTreeList, Comparator.comparingInt(DeptDto::getSort)),
					CommonConstant.ROOT);
		}
		return Lists.newArrayList();
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
		return null;
	}
}
