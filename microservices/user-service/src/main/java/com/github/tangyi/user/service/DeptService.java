package com.github.tangyi.user.service;

import cn.hutool.core.collection.CollUtil;
import com.github.tangyi.api.user.dto.DeptDto;
import com.github.tangyi.api.user.module.Dept;
import com.github.tangyi.api.user.module.User;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TreeUtil;
import com.github.tangyi.user.mapper.DeptMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    /**
     * 删除部门
     *
     * @param dept dept
     * @return int
     */
    @Transactional
    @Override
    public int delete(Dept dept) {
        // 删除部门
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
        return this.findListById(userList.stream().filter(tempUser -> tempUser.getDeptId() != null).map(User::getDeptId).distinct().toArray(Long[]::new));
    }

	/**
	 * 查询树形部门集合
	 *
	 * @return List
	 * @author tangyi
	 * @date 2018/10/25 12:57
	 */
	public List<DeptDto> depts() {
		Dept dept = new Dept();
		dept.setApplicationCode(SysUtil.getSysCode());
		dept.setTenantCode(SysUtil.getTenantCode());
		// 查询部门集合
		Stream<Dept> deptStream = findList(dept).stream();
		if (Optional.ofNullable(deptStream).isPresent()) {
			List<DeptDto> deptTreeList = deptStream.map(DeptDto::new).collect(Collectors.toList());
			// 排序、构建树形结构
			return TreeUtil
					.buildTree(CollUtil.sort(deptTreeList, Comparator.comparingInt(DeptDto::getSort)), CommonConstant.ROOT);
		}
		return new ArrayList<>();
	}
}
