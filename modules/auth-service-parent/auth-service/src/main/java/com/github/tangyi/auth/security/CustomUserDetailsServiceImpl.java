package com.github.tangyi.auth.security;

import com.github.tangyi.auth.api.module.WxSession;
import com.github.tangyi.auth.model.CustomUserDetails;
import com.github.tangyi.auth.service.WxSessionService;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.enums.LoginType;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.exceptions.ServiceException;
import com.github.tangyi.common.core.exceptions.TenantNotFoundException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.DateUtils;
import com.github.tangyi.common.core.utils.ResponseUtil;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.common.security.core.CustomUserDetailsService;
import com.github.tangyi.common.security.core.GrantedAuthorityImpl;
import com.github.tangyi.common.security.mobile.MobileUser;
import com.github.tangyi.common.security.wx.WxUser;
import com.github.tangyi.user.api.dto.UserDto;
import com.github.tangyi.user.api.enums.IdentityType;
import com.github.tangyi.user.api.feign.UserServiceClient;
import com.github.tangyi.user.api.module.Tenant;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 从数据库获取用户信息
 *
 * @author tangyi
 * @date 2019-03-14 14:36
 */
@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserServiceClient userServiceClient;

    private final WxSessionService wxService;

    /**
     * 加载用户信息
     *
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException,TenantNotFoundException
     */
    @Override
    public UserDetails loadUserByIdentifierAndTenantCode(String username, String tenantCode) throws UsernameNotFoundException, TenantNotFoundException {
        long start = System.currentTimeMillis();
        Tenant tenant = this.validateTenantCode(tenantCode);
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByIdentifier(username, tenantCode);
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException("查询用户信息失败: " + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        if (userVo == null)
            throw new UsernameNotFoundException("用户不存在.");
        return new CustomUserDetails(username, userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(), start, LoginType.PWD);
    }

    /**
     * 根据社交账号查询
     *
     * @param social     social
     * @param tenantCode tenantCode
     * @param mobileUser mobileUser
     * @return UserDetails
     * @author tangyi
     * @date 2019/06/22 21:08
     */
    @Override
    public UserDetails loadUserBySocialAndTenantCode(String social, String tenantCode, MobileUser mobileUser) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        Tenant tenant = this.validateTenantCode(tenantCode);
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByIdentifier(social, IdentityType.PHONE_NUMBER.getValue(), tenantCode);
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException("查询用户信息失败: " + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        // 第一次登录
        if (userVo == null) {
            UserDto userDto = new UserDto();
            // 用户的基本信息
            if (mobileUser != null)
                BeanUtils.copyProperties(mobileUser, userDto);
            userDto.setIdentifier(social);
            userDto.setCredential(social);
            userDto.setIdentityType(IdentityType.PHONE_NUMBER.getValue());
            userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
            // 注册账号
            ResponseBean<Boolean> response = userServiceClient.registerUser(userDto);
            if (!ResponseUtil.isSuccess(response))
                throw new ServiceException("自动注册用户失败: " + response.getMsg());
            // 重新获取用户信息
            userVoResponseBean = userServiceClient.findUserByIdentifier(social, IdentityType.PHONE_NUMBER.getValue(), tenantCode);
            if (!ResponseUtil.isSuccess(userVoResponseBean))
                throw new ServiceException("查询用户信息失败: " + userVoResponseBean.getMsg());
            userVo = userVoResponseBean.getData();
        } else {
            // TODO 记录登录时间，IP等信息
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userVo, userDto);
            userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
            //userServiceClient.updateUser(userDto);
        }
        return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(), start, LoginType.SMS);
    }

    /**
     * 根据微信code和租户标识查询
     * 将code换成openId和sessionKey
     *
     * @param code       code
     * @param tenantCode tenantCode
     * @param wxUser     wxUser
     * @return UserDetails
     * @author tangyi
     * @date 2019/07/05 20:05:36
     */
    @Override
    public UserDetails loadUserByWxCodeAndTenantCode(String code, String tenantCode, WxUser wxUser) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        Tenant tenant = this.validateTenantCode(tenantCode);
        // 根据code获取openId和sessionKey
        WxSession wxSession = wxService.code2Session(code);
        if (wxSession == null)
            throw new CommonException("获取openId失败.");
        // 获取用户信息
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByIdentifier(wxSession.getOpenId(), IdentityType.WE_CHAT.getValue(), tenantCode);
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException("查询用户信息失败: " + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        // 为空说明是第一次登录，需要将用户信息增加到数据库里
        if (userVo == null) {
            UserDto userDto = new UserDto();
            // 用户的基本信息
            if (wxUser != null)
                BeanUtils.copyProperties(wxUser, userDto);
            userDto.setIdentifier(wxSession.getOpenId());
            userDto.setCredential(wxSession.getOpenId());
            userDto.setIdentityType(IdentityType.WE_CHAT.getValue());
            userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
            // 注册账号
            ResponseBean<Boolean> response = userServiceClient.registerUser(userDto);
            if (!ResponseUtil.isSuccess(response))
                throw new ServiceException("自动注册用户失败: " + response.getMsg());
            // 重新获取用户信息
            userVoResponseBean = userServiceClient.findUserByIdentifier(wxSession.getOpenId(), IdentityType.WE_CHAT.getValue(), tenantCode);
            if (!ResponseUtil.isSuccess(userVoResponseBean))
                throw new ServiceException("查询用户信息失败: " + userVoResponseBean.getMsg());
            userVo = userVoResponseBean.getData();
        } else {
            // TODO 更新sessionKey，记录登录时间，IP等信息
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userVo, userDto);
            //userDto.setCredential(wxSession.getSessionKey());
            userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
            //userServiceClient.updateUser(userDto);
        }
        return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(), start, LoginType.WECHAT);
    }

    /**
     * 校验租户标识
     *
     * @param tenantCode tenantCode
     * @return Tenant
     */
    private Tenant validateTenantCode(String tenantCode) throws TenantNotFoundException {
        if (StringUtils.isBlank(tenantCode))
            throw new TenantNotFoundException("租户code不能为空.");
        // 先获取租户信息
        ResponseBean<Tenant> tenantResponseBean = userServiceClient.findTenantByTenantCode(tenantCode);
        if (!ResponseUtil.isSuccess(tenantResponseBean))
            throw new ServiceException("查询租户信息失败: " + tenantResponseBean.getMsg());
        Tenant tenant = tenantResponseBean.getData();
        if (tenant == null)
            throw new TenantNotFoundException("租户不存在.");
        return tenant;
    }

    /**
     * 获取用户权限
     *
     * @param userVo userVo
     * @return Set
     * @author tangyi
     * @date 2019/03/17 14:41
     */
    private Set<GrantedAuthority> getAuthority(UserVo userVo) {
        return userVo.getRoleList()
                .stream()
                .map(role -> new GrantedAuthorityImpl(role.getRoleCode().toUpperCase()))
                .collect(Collectors.toSet());
    }
}
