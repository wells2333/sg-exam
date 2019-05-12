package com.github.tangyi.user.api.feign;

import com.github.tangyi.common.core.constant.ServiceConstant;
import com.github.tangyi.common.core.model.Log;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.vo.AttachmentVo;
import com.github.tangyi.common.core.vo.DeptVo;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.feign.config.CustomFeignConfig;
import com.github.tangyi.user.api.dto.UserInfoDto;
import com.github.tangyi.user.api.feign.factory.UserServiceClientFallbackFactory;
import com.github.tangyi.user.api.module.Menu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务
 *
 * @author tangyi
 * @date 2018-12-30 23:21
 */
@FeignClient(value = ServiceConstant.USER_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = UserServiceClientFallbackFactory.class)
public interface UserServiceClient {

    /**
     * 根据用户名获取用户详细信息
     *
     * @param username username
     * @return UserVo
     * @author tangyi
     * @date 2019/03/17 12:14
     */
    @GetMapping("/v1/user/findUserByUsername/{username}")
    UserVo findUserByUsername(@PathVariable("username") String username);

    /**
     * 获取当前用户的信息
     *
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/23 23:44
     */
    @GetMapping("/v1/user/info")
    ResponseBean<UserInfoDto> info();

    /**
     * 根据用户id获取用户
     *
     * @param userVo userVo
     * @return UserVo
     */
    @RequestMapping(value = "/v1/user/findById", method = RequestMethod.POST)
    ResponseBean<List<UserVo>> findUserById(@RequestBody UserVo userVo);

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author tangyi
     * @date 2019/05/09 22:04
     */
    @RequestMapping(value = "/v1/user/userCount", method = RequestMethod.POST)
    ResponseBean<Integer> findUserCount(@RequestBody UserVo userVo);

    /**
     * 根据部门id获取部门
     *
     * @param deptVo deptVo
     * @return ResponseBean
     */
    @RequestMapping(value = "/v1/dept/findById", method = RequestMethod.POST)
    ResponseBean<List<DeptVo>> findDeptById(@RequestBody DeptVo deptVo);

    /**
     * 根据ID删除附件
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2019/01/01 20:44
     */
    @DeleteMapping("/v1/attachment/{id}")
    ResponseBean<Boolean> deleteAttachment(@PathVariable(value = "id") String id);

    /**
     * 根据附件id获取附件
     *
     * @param attachmentVo attachmentVo
     * @return ResponseBean
     */
    @RequestMapping(value = "/v1/attachment/findById", method = RequestMethod.POST)
    ResponseBean<List<AttachmentVo>> findAttachmentById(@RequestBody AttachmentVo attachmentVo);

    /**
     * 保存日志
     *
     * @param log log
     * @return ResponseBean
     * @author tangyi
     * @date 2019/03/23 23:26
     */
    @PostMapping("/v1/log")
    ResponseBean<Boolean> saveLog(@RequestBody Log log);

    /**
     * 根据角色查找菜单
     *
     * @param role 角色
     * @return List
     * @author tangyi
     * @date 2019/04/08 20:42
     */
    @GetMapping("/v1/menu/findMenuByRole/{role}")
    List<Menu> findMenuByRole(@PathVariable("role") String role);

    /**
     * 查询所有菜单
     *
     * @return List
     * @author tangyi
     * @date 2019/04/26 11:48
     */
    @GetMapping("/v1/menu/findAllMenu")
    List<Menu> findAllMenu();
}
