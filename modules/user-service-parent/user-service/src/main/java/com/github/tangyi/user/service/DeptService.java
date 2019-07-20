package com.github.tangyi.user.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.user.api.module.Dept;
import com.github.tangyi.user.api.module.User;
import com.github.tangyi.user.mapper.DeptMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Dept dept = new Dept();
        dept.setIds(userList.stream().filter(tempUser -> tempUser.getDeptId() != null).map(User::getDeptId).distinct().toArray(String[]::new));
        return this.findListById(dept);
    }
}
