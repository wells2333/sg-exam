package com.github.tangyi.user.utils;

import com.github.tangyi.common.core.utils.SpringContextHolder;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.core.vo.RoleVo;
import com.github.tangyi.user.api.dto.UserInfoDto;
import com.github.tangyi.user.api.module.Role;
import com.github.tangyi.user.api.module.User;
import com.github.tangyi.user.api.module.UserAuths;
import com.github.tangyi.user.config.SysConfig;
import org.springframework.beans.BeanUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户工具类
 *
 * @author tangyi
 * @date 2018/11/26 22:32
 */
public class UserUtils {

    /**
     * 获取User属性的map
     *
     * @return LinkedHashMap
     * @author tangyi
     * @date 2018/11/26 22:35
     */
    public static LinkedHashMap<String, String> getUserMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "用户id");
        map.put("identifier", "账号");
        map.put("identityType", "账号类型");
        map.put("credential", "密码");
        map.put("name", "姓名");
        map.put("phone", "联系电话");
        map.put("email", "邮箱");
        //map.put("born", "出生日期");
        map.put("remark", "备注");
        map.put("status", "状态");
        map.put("deptId", "部门ID");
        map.put("applicationCode", "系统编码");
        map.put("tenantCode", "租户标识");
        return map;
    }

    /**
     * Role 转 RoleVo
     *
     * @param roles roles
     * @return List
     * @author tangyi
     * @date 2019/07/03 13:11:05
     */
    public static List<RoleVo> rolesToVo(List<Role> roles) {
        return roles.stream().map(role -> {
            RoleVo roleVo = new RoleVo();
            roleVo.setRoleCode(role.getRoleCode());
            roleVo.setRoleName(role.getRoleName());
            roleVo.setRoleDesc(role.getRoleDesc());
            return roleVo;
        }).collect(Collectors.toList());
    }

    /**
     * 转DTO
     *
     * @param userInfoDto userInfoDto
     * @param user        user
     * @param userAuths   userAuths
     * @return UserInfoDto
     * @author tangyi
     * @date 2019/07/03 20:23:15
     */
    public static void toUserInfoDto(UserInfoDto userInfoDto, User user, UserAuths userAuths) {
        BeanUtils.copyProperties(userAuths, userInfoDto);
        BeanUtils.copyProperties(user, userInfoDto);
    }

    /**
     * 是否为管理员
     *
     * @param identifier identifier
     * @return boolean
     * @author tangyi
     * @date 2019/07/04 00:25:11
     */
    public static boolean isAdmin(String identifier) {
        SysConfig sysConfig = SpringContextHolder.getApplicationContext().getBean(SysConfig.class);
        return identifier.equals(sysConfig.getAdminUser());
    }

    /**
     * 是否为管理员
     *
     * @return boolean
     * @author tangyi
     * @date 2019/07/04 00:25:11
     */
    public static boolean isAdmin() {
        return isAdmin(SysUtil.getUser());
    }
}
