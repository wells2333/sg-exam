package com.github.tangyi.user.api.feign.fallback;

import com.github.tangyi.common.core.model.Log;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.vo.AttachmentVo;
import com.github.tangyi.common.core.vo.DeptVo;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.user.api.dto.UserInfoDto;
import com.github.tangyi.user.api.feign.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志断路器实现
 *
 * @author tangyi
 * @date 2019/3/23 23:39
 */
@Component
public class UserServiceClientFallbackImpl implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientFallbackImpl.class);

    private Throwable throwable;

    /**
     * 根据用户名查询用户信息
     *
     * @param username username
     * @return UserVo
     */
    @Override
    public UserVo findUserByUsername(String username) {
        logger.error("feign 查询用户信息失败:{},{}", username, throwable);
        return null;
    }

    /**
     * 查询当前登录的用户信息
     *
     * @return ResponseBean
     */
    @Override
    public ResponseBean<UserInfoDto> info() {
        logger.error("feign 查询用户信息失败:{},{}", throwable);
        return null;
    }

    /**
     * 根据用户ID批量查询用户信息
     *
     * @param userVo userVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<UserVo>> findUserById(@RequestBody UserVo userVo) {
        logger.error("调用{}异常:{},{}", "findById", userVo, throwable);
        return null;
    }

    /**
     * 根据部门ID批量查询部门信息
     *
     * @param deptVo deptVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<DeptVo>> findDeptById(@RequestBody DeptVo deptVo) {
        logger.error("调用{}异常:{},{}", "findById", deptVo, throwable);
        return null;
    }

    /**
     * 根据附件ID删除附件
     *
     * @param id id
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> deleteAttachment(String id) {
        logger.error("调用{}异常:{},{}", "delete", id, throwable);
        return new ResponseBean<>(Boolean.FALSE);
    }

    /**
     * 根据附件ID批量查询附件信息
     *
     * @param attachmentVo attachmentVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<AttachmentVo>> findAttachmentById(AttachmentVo attachmentVo) {
        logger.error("调用{}异常:{},{}", "findById", attachmentVo, throwable);
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 保存日志
     *
     * @param log log
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> saveLog(Log log) {
        logger.error("feign 插入日志失败,{}", throwable);
        return null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
