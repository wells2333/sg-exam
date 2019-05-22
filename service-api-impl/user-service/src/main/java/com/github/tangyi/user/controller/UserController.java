package com.github.tangyi.user.controller;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.*;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.log.annotation.Log;
import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.user.api.constant.RoleConstant;
import com.github.tangyi.user.api.dto.UserDto;
import com.github.tangyi.user.api.dto.UserInfoDto;
import com.github.tangyi.user.api.module.Dept;
import com.github.tangyi.user.api.module.Role;
import com.github.tangyi.user.api.module.User;
import com.github.tangyi.user.api.module.UserRole;
import com.github.tangyi.user.service.DeptService;
import com.github.tangyi.user.service.RoleService;
import com.github.tangyi.user.service.UserRoleService;
import com.github.tangyi.user.service.UserService;
import com.github.tangyi.user.utils.UserUtils;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final DeptService deptService;

    private final RoleService roleService;

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
        if (StringUtils.isNotEmpty(id)) {
            user.setId(id);
            user = userService.get(user);
        }
        return new ResponseBean<>(user);
    }

    /**
     * 获取当前用户信息（角色、权限）
     *
     * @return 用户名
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户详细信息")
    public ResponseBean<UserInfoDto> user(Principal principal) {
        UserVo userVo = new UserVo();
        userVo.setUsername(principal.getName());
        return new ResponseBean<>(userService.findUserInfo(userVo));
    }

    /**
     * 根据用户名获取用户详细信息
     *
     * @param username username
     * @return UserVo
     */
    @GetMapping("/findUserByUsername/{username}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户name获取用户详细信息")
    @ApiImplicitParam(name = "username", value = "用户name", required = true, dataType = "String", paramType = "path")
    public UserVo findUserByUsername(@PathVariable String username) {
        return userService.selectUserVoByUsername(username);
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
    @RequestMapping("userList")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "userVo", value = "用户信息", dataType = "UserVo")
    })
    public PageInfo<User> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                   @RequestParam(value = "username", required = false, defaultValue = "") String username,
                                   UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        // 用户名查询条件
        user.setUsername(username);
        PageInfo<User> page = userService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), user);
        List<User> users = page.getList();
        if (CollectionUtils.isNotEmpty(users)) {
            Dept dept = new Dept();
            // 流处理获取部门ID集合，转成字符串数组
            dept.setIds(users.stream().filter(tempUser -> tempUser.getDeptId() != null).map(User::getDeptId).distinct().toArray(String[]::new));
            // 批量查找部门
            List<Dept> deptList = deptService.findListById(dept);
            // 流处理获取用户ID集合，根据用户ID批量查找角色
            List<UserRole> userRoles = userRoleService.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
            List<Role> roleList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(userRoles)) {
                Role role = new Role();
                // 获取角色ID集合，转成字节数组
                role.setIds(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(String[]::new));
                // 批量查找角色
                roleList = roleService.findListById(role);
            }
            // 遍历用户集合，设置部门、角色
            List<Role> finalRoleList = roleList;
            users.forEach(tempUser -> {
                // 设置部门信息
                if (CollectionUtils.isNotEmpty(deptList)) {
                    // 用户所属部门
                    Dept userDept = deptList.stream()
                            // 按部门ID找到部门信息
                            .filter(tempDept -> tempDept.getId().equals(tempUser.getDeptId()))
                            .findFirst().orElse(null);
                    if (userDept != null) {
                        tempUser.setDeptName(userDept.getDeptName());
                        tempUser.setDeptId(userDept.getId());
                    }
                }
                // 设置角色信息
                if (CollectionUtils.isNotEmpty(userRoles)) {
                    List<Role> userRoleList = new ArrayList<>();
                    userRoles.stream()
                            // 过滤
                            .filter(tempUserRole -> tempUser.getId().equals(tempUserRole.getUserId()))
                            .forEach(tempUserRole -> finalRoleList.stream()
                                    .filter(role -> role.getId().equals(tempUserRole.getRoleId()))
                                    .forEach(userRoleList::add));
                    tempUser.setRoleList(userRoleList);
                }
            });
        }
        return page;
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
    @PreAuthorize("hasAuthority('sys:user:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "创建用户", notes = "创建用户")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("新增用户")
    public ResponseBean<Boolean> addUser(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        // 设置默认密码
        if (StringUtils.isEmpty(userDto.getPassword()))
            userDto.setPassword(CommonConstant.DEFAULT_PASSWORD);
        user.setPassword(encoder.encode(userDto.getPassword()));
        // 保存用户
        return new ResponseBean<>(userService.insert(user) > 0);
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
    @PreAuthorize("hasAuthority('sys:user:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "更新用户信息", notes = "根据用户id更新用户的基本信息、角色信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log("修改用户")
    public ResponseBean<Boolean> updateUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseBean<>(userService.updateUser(userDto));
        } catch (Exception e) {
            log.error("更新用户信息失败！", e);
        }
        return new ResponseBean<>(Boolean.FALSE);
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
        // 新密码不为空
        if (StringUtils.isNotEmpty(userDto.getNewPassword())) {
            if (!encoder.matches(userDto.getOldPassword(), userDto.getPassword())) {
                return new ResponseBean<>(Boolean.FALSE, "新旧密码不匹配");
            } else {
                // 新旧密码一致，修改密码
                userDto.setPassword(encoder.encode(userDto.getNewPassword()));
            }
        }
        return new ResponseBean<>(userService.update(userDto) > 0);
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
    @PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @Log("删除用户")
    public ResponseBean<Boolean> deleteUser(@PathVariable String id) {
        try {
            User user = userService.get(id);
            user.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
            userService.delete(user);
        } catch (Exception e) {
            log.error("删除用户信息失败！", e);
        }
        return new ResponseBean<>(Boolean.FALSE);
    }

    /**
     * 导出
     *
     * @param userVo userVo
     * @author tangyi
     * @date 2018/11/26 22:11
     */
    @PostMapping("export")
    @PreAuthorize("hasAuthority('sys:user:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
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
                // 按逗号切割ID，流处理获取ID集合，去重，转成字符串数组
                user.setIds(Stream.of(userVo.getIdString().split(",")).filter(StringUtils::isNotBlank).distinct().toArray(String[]::new));
                users = userService.findListById(user);
            } else {    // 导出全部用户
                users = userService.findList(new User());
            }
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(users), UserUtils.getUserMap());
        } catch (Exception e) {
            log.error("导出用户数据失败！", e);
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
    @RequestMapping("import")
    @PreAuthorize("hasAuthority('sys:user:import') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "导入数据", notes = "导入数据")
    @Log("导入用户")
    public ResponseBean<Boolean> importUser(@ApiParam(value = "要上传的文件", required = true) MultipartFile file, HttpServletRequest request) {
        try {
            log.debug("开始导入用户数据");
            List<User> users = MapUtil.map2Java(User.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), UserUtils.getUserMap()));
            if (CollectionUtils.isNotEmpty(users)) {
                for (User user : users) {
                    if (userService.update(user) < 1)
                        userService.insert(user);
                }
            }
            return new ResponseBean<>(Boolean.TRUE);
        } catch (Exception e) {
            log.error("导入用户数据失败！", e);
        }
        return new ResponseBean<>(Boolean.FALSE);
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
    @PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "', '" + SecurityConstant.ROLE_TEACHER + "')")
    @ApiOperation(value = "批量删除用户", notes = "根据用户id批量删除用户")
    @ApiImplicitParam(name = "user", value = "用户信息", dataType = "User")
    @Log("批量删除用户")
    public ResponseBean<Boolean> deleteAllUsers(@RequestBody User user) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(user.getIdString()))
                success = userService.deleteAll(user.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除用户失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据ID查询
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author tangyi
     * @date 2018/12/31 21:16
     */
    @RequestMapping(value = "findById", method = RequestMethod.POST)
    @ApiOperation(value = "根据ID查询用户", notes = "根据ID查询用户")
    @ApiImplicitParam(name = "userVo", value = "用户信息", required = true, paramType = "UserVo")
    public ResponseBean<List<UserVo>> findById(@RequestBody UserVo userVo) {
        ResponseBean<List<UserVo>> returnT = null;
        User user = new User();
        user.setIds(userVo.getIds());
        Stream<User> userStream = userService.findListById(user).stream();
        if (Optional.ofNullable(userStream).isPresent()) {
            List<UserVo> userVoList = userStream.map(tempUser -> {
                UserVo tempUserVo = new UserVo();
                BeanUtils.copyProperties(tempUser, tempUserVo);
                return tempUserVo;
            }).collect(Collectors.toList());
            returnT = new ResponseBean<>(userVoList);
        }
        return returnT;
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
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @PostMapping("register")
    @Log("注册用户")
    public ResponseBean<Boolean> register(UserDto userDto) {
        boolean success = false;
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        // 设置默认密码
        if (StringUtils.isEmpty(userDto.getPassword()))
            userDto.setPassword(CommonConstant.DEFAULT_PASSWORD);
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setStatus(CommonConstant.DEL_FLAG_NORMAL.toString());
        // 保存用户
        if (userService.insert(user) > 0) {
            // 分配默认角色
            Role role = new Role();
            role.setIsDefault(RoleConstant.IS_DEFAULT_ROLE);
            // 查询默认角色
            Stream<Role> roleStream = roleService.findList(role).stream();
            if (Optional.ofNullable(roleStream).isPresent()) {
                Role defaultRole = roleStream.findFirst().orElse(null);
                if (defaultRole != null) {
                    UserRole userRole = new UserRole();
                    userRole.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(defaultRole.getId());
                    // 保存用户角色关系
                    success = userRoleService.insert(userRole) > 0;
                }
            }
        }
        return new ResponseBean<>(success);
    }

    /**
     * 检查用户是否存在
     *
     * @param username username
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/23 15:35
     */
    @ApiOperation(value = "检查用户是否存在", notes = "检查用户名是否存在")
    @ApiImplicitParam(name = "username", value = "用户name", required = true, dataType = "String", paramType = "path")
    @GetMapping("checkExist/{username}")
    public ResponseBean<Boolean> checkUsernameIsExist(@PathVariable("username") String username) {
        boolean exist = Boolean.FALSE;
        if (StringUtils.isNotEmpty(username))
            exist = userService.selectUserVoByUsername(username) != null;
        return new ResponseBean<>(exist);
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
}
