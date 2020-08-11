package com.github.tangyi.user.service;

import com.github.tangyi.api.user.module.UserStudent;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.user.mapper.UserStudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户学生Service
 *
 * @author tangyi
 * @date 2019/07/09 15:58
 */
@Service
public class UserStudentService extends CrudService<UserStudentMapper, UserStudent> {

    /**
     * 根据用户ID查询
     *
     * @param userId userId
     * @return List
     * @author tangyi
     * @date 2019/07/09 17:01:13
     */
    public List<UserStudent> getByUserId(@NotBlank String userId) {
        return this.dao.getByUserId(userId);
    }

    /**
     * 根据学生ID查询
     *
     * @param studentId studentId
     * @return UserStudent
     * @author tangyi
     * @date 2019/07/09 17:02:19
     */
    public UserStudent getByStudentId(@NotBlank String studentId) {
        return this.dao.getByStudentId(studentId);
    }

    /**
     * 根据用户id删除
     *
     * @param userId userId
     * @return int
     * @author tangyi
     * @date 2019/07/09 17:04:13
     */
    @Transactional
    public int deleteByUserId(@NotBlank String userId) {
        return this.dao.deleteByUserId(userId);
    }

    /**
     * 根据学生id删除
     *
     * @param studentId studentId
     * @return int
     * @author tangyi
     * @date 2019/07/09 17:04:59
     */
    @Transactional
    public int deleteByStudentId(@NotBlank String studentId) {
        return this.dao.deleteByStudentId(studentId);
    }
}
