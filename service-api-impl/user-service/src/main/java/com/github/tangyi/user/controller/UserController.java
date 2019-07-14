package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.*;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.user.api.dto.UserDto;
import com.github.tangyi.user.api.dto.UserInfoDto;
import com.github.tangyi.user.api.module.*;
import com.github.tangyi.user.service.DeptService;
import com.github.tangyi.user.service.UserAuthsService;
import com.github.tangyi.user.service.UserRoleService;
import com.github.tangyi.user.service.UserService;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tangyi
 * @date 2018-08-25 16:20
 */
@Slf4j
@AllArgsConstructor
@Api("用户信息管理")
@RestController
@RequestMapping(value = "/v1/user")
public class UserController extends BaseController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final DeptService deptService;

    private final UserAuthsService userAuthsService;

    /**
     * 根据id获取
     *
     * @param id id
     * @return ResponseBean
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户id获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<User> user(@PathVariable String id) {
        User user = new User();
        user.setId(id);
        return new ResponseBean<>(userService.get(user));
    }

    /**
     * 获取当前用户信息（角色、权限）
     *
     * @return 用户名
     */
    @GetMapping("info")
    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户详细信息")
    @ApiImplicitParam(name = "identityType", value = "账号类型", required = true, dataType = "String")
    public ResponseBean<UserInfoDto> userInfo(@RequestParam(required = false) String identityType, OAuth2Authentication authentication) {
        try {
            UserVo userVo = new UserVo();
            if (StringUtils.isNotEmpty(identityType))
                userVo.setIdentityType(Integer.valueOf(identityType));
            userVo.setIdentifier(authentication.getName());
            userVo.setTenantCode(SysUtil.getTenantCode());
            return new ResponseBean<>(userService.findUserInfo(userVo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("获取当前登录用户详细信息");
        }
    }

    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @param tenantCode   tenantCode
     * @return UserVo
     */
    @GetMapping("/findUserByIdentifier/{identifier}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户name获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "identityType", value = "用户授权类型", dataType = "Integer"),
            @ApiImplicitParam(name = "tenantCode", value = "租户标识", required = true, dataType = "String"),
    })
    public UserVo findUserByIdentifier(@PathVariable String identifier, @RequestParam(required = false) Integer identityType, @RequestParam @NotBlank String tenantCode) {
        return userService.findUserByIdentifier(identityType, identifier, tenantCode);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param userVo   userVo
     * @return PageInfo
     * @author tangyi
     * @date 2018/8/26 22:56
     */
    @GetMapping("userList")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "userVo", value = "用户信息", dataType = "UserVo")
    })
    public PageInfo<UserDto> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                      @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                      UserVo userVo) {
        PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
        List<UserDto> userDtos = Lists.newArrayList();
        userVo.setTenantCode(SysUtil.getTenantCode());
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setName(name);
        PageInfo<User> page = userService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), user);
        List<User> users = page.getList();
        if (CollectionUtils.isNotEmpty(users)) {
            // 批量查询账户
            List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
            // 批量查找部门
            List<Dept> deptList = deptService.getListByUsers(users);
            // 查询用户角色关联关系
            List<UserRole> userRoles = userRoleService.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
            // 批量查找角色
            List<Role> finalRoleList = userService.getUsersRoles(users);
            // 组装数据
            users.forEach(tempUser -> userDtos.add(userService.getUserDtoByUserAndUserAuths(tempUser, userAuths, deptList, userRoles, finalRoleList)));
        }
        PageUtil.copyProperties(page, userDtoPageInfo);
        userDtoPageInfo.setList(userDtos);
        return userDtoPageInfo;
    }

    /**
     * 创建用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author tangyi
     * @date 2018/8/26 14:34
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "创建用户", notes = "创建用户")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("新增用户")
    public ResponseBean<Boolean> addUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseBean<>(userService.createUser(userDto) > 0);
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author tangyi
     * @date 2018/8/26 15:06
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新用户信息", notes = "根据用户id更新用户的基本信息、角色信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("修改用户")
    public ResponseBean<Boolean> updateUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseBean<>(userService.updateUser(userDto));
        } catch (Exception e) {
            log.error("更新用户信息失败！", e);
            throw new CommonException("更新用户信息失败！");
        }
    }

    /**
     * 更新用户的基本信息
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author tangyi
     * @date 2018/10/30 10:06
     */
    @PutMapping("updateInfo")
    @ApiOperation(value = "更新用户基本信息", notes = "根据用户id更新用户的基本信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("更新用户基本信息")
    public ResponseBean<Boolean> updateInfo(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(userService.update(user) > 0);
    }

    /**
     * 修改密码
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/21 20:09
     */
    @PutMapping("updatePassword")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("更新用户密码")
    public ResponseBean<Boolean> updatePassword(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.updatePassword(userDto) > 0);
    }

    /**
     * 更新头像
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author tangyi
     * @date 2019/06/21 18:08
     */
    @PutMapping("updateAvatar")
    @ApiOperation(value = "更新用户头像", notes = "根据用户id更新用户的头像信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("更新用户头像")
    public ResponseBean<Boolean> updateAvatar(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.updateAvatar(userDto) > 0);
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return ResponseBean
     * @author tangyi
     * @date 2018/8/26 15:28
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @Log("删除用户")
    public ResponseBean<Boolean> deleteUser(@PathVariable String id) {
        try {
            User user = new User();
            user.setId(id);
            user = userService.get(user);
            user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            return new ResponseBean<>(userService.delete(user) > 0);
        } catch (Exception e) {
            log.error("删除用户信息失败！", e);
            throw new CommonException("删除用户信息失败！");
        }
    }

    /**
     * 导出
     *
     * @param userVo userVo
     * @author tangyi
     * @date 2018/11/26 22:11
     */
    @PostMapping("export")
    @PreAuthorize("hasAuthority('sys:user:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导出用户", notes = "根据用户id导出用户")
    @ApiImplicitParam(name = "userVo", value = "用户信息", required = true, dataType = "UserVo")
    @Log("导出用户")
    public void exportUser(@RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "用户信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<User> users;
            if (StringUtils.isNotEmpty(userVo.getIdString())) {
                User user = new User();
                user.setIds(Stream.of(userVo.getIdString().split(",")).filter(StringUtils::isNotBlank).distinct().toArray(String[]::new));
                users = userService.findListById(user);
            } else {
                // 导出本租户下的全部用户
                User user = new User();
                user.setTenantCode(SysUtil.getTenantCode());
                users = userService.findList(user);
            }
            if (CollectionUtils.isEmpty(users))
                throw new CommonException("无用户数据.");
            // 查询用户授权信息
            List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
            // 组装数据，转成dto
            List<UserInfoDto> userInfoDtos = users.stream().map(tempUser -> {
                UserInfoDto userDto = new UserInfoDto();
                userAuths.stream()
                        .filter(userAuth -> userAuth.getUserId().equals(tempUser.getId()))
                        .findFirst()
                        .ifPresent(userAuth -> UserUtils.toUserInfoDto(userDto, tempUser, userAuth));
                return userDto;
            }).collect(Collectors.toList());
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(userInfoDtos), UserUtils.getUserMap());
        } catch (Exception e) {
            log.error("导出用户数据失败！", e);
            throw new CommonException("导出用户数据失败！");
        }
    }

    /**
     * 导入数据
     *
     * @param file file
     * @return ResponseBean
     * @author tangyi
     * @date 2018/11/28 12:44
     */
    @PostMapping("import")
    @PreAuthorize("hasAuthority('sys:user:import') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导入数据", notes = "导入数据")
    @Log("导入用户")
    public ResponseBean<Boolean> importUser(@ApiParam(value = "要上传的文件", required = true) MultipartFile file, HttpServletRequest request) {
        try {
            log.debug("开始导入用户数据");
            List<UserInfoDto> userInfoDtos = MapUtil.map2Java(UserInfoDto.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), UserUtils.getUserMap()));
            if (CollectionUtils.isEmpty(userInfoDtos))
                throw new CommonException("无用户数据导入.");
            return new ResponseBean<>(userService.importUsers(userInfoDtos));
        } catch (Exception e) {
            log.error("导入用户数据失败！", e);
            throw new CommonException("导入用户数据失败！");
        }
    }

    /**
     * 批量删除
     *
     * @param user user
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/4 9:58
     */
    @PostMapping("deleteAll")
    @PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "批量删除用户", notes = "根据用户id批量删除用户")
    @ApiImplicitParam(name = "user", value = "用户信息", dataType = "User")
    @Log("批量删除用户")
    public ResponseBean<Boolean> deleteAllUsers(@RequestBody User user) {
        try {
            boolean success = Boolean.FALSE;
            if (StringUtils.isNotEmpty(user.getIdString()))
                success = userService.deleteAll(user.getIdString().split(",")) > 0;
            return new ResponseBean<>(success);
        } catch (Exception e) {
            log.error("删除用户失败！", e);
            throw new CommonException("删除用户失败！");
        }
    }

    /**
     * 根据ID查询
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/31 21:16
     */
    @PostMapping(value = "findById")
    @ApiOperation(value = "根据ID查询用户", notes = "根据ID查询用户")
    @ApiImplicitParam(name = "userVo", value = "用户信息", required = true, paramType = "UserVo")
    public ResponseBean<List<UserVo>> findById(@RequestBody UserVo userVo) {
        return new ResponseBean<>(userService.findUserVoListById(userVo));
    }

    /**
     * 注册
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author tangyi
     * @date 2019/01/10 22:35
     */
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "授权类型（password、mobile）", required = true, defaultValue = "password", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "randomStr", value = "随机数", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query")
    })
    @PostMapping("register")
    @Log("注册用户")
    public ResponseBean<Boolean> register(@RequestBody @Valid UserDto userDto) {
        return new ResponseBean<>(userService.register(userDto));
    }

    /**
     * 检查账号是否存在
     *
     * @param identityType identityType
     * @param identifier   identifier
     * @param tenantCode   tenantCode
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/23 15:35
     */
    @ApiOperation(value = "检查账号是否存在", notes = "检查账号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identityType", value = "用户唯一标识类型", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tenantCode", value = "租户标识", required = true, dataType = "String"),
    })
    @GetMapping("checkExist/{identifier}")
    public ResponseBean<Boolean> checkExist(@PathVariable("identifier") String identifier, @RequestParam Integer identityType, @RequestHeader(SecurityConstant.TENANT_CODE_HEADER) String tenantCode) {
        return new ResponseBean<>(userService.checkIdentifierIsExist(identityType, identifier, tenantCode));
    }

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author tangyi
     * @date 2019/05/09 22:09
     */
    @PostMapping("userCount")
    public ResponseBean<Integer> userCount(UserVo userVo) {
        return new ResponseBean<>(userService.userCount(userVo));
    }

    /**
     * 重置密码
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author tangyi
     * @date 2019/6/7 12:00
     */
    @PutMapping("/resetPassword")
    @PreAuthorize("hasAuthority('sys:user:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "重置密码", notes = "根据用户id重置密码")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("重置密码")
    public ResponseBean<Boolean> resetPassword(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.resetPassword(userDto));
    }
}
