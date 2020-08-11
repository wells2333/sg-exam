package com.github.tangyi.api.user.client;

import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.dto.UserInfoDto;
import com.github.tangyi.api.user.module.Menu;
import com.github.tangyi.api.user.module.Tenant;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.model.ResponseBean;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.vo.AttachmentVo;
import com.github.tangyi.common.vo.DeptVo;
import com.github.tangyi.common.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务断路器实现
 *
 * @author tangyi
 * @date 2019/3/23 23:39
 */
@Slf4j
@Component
public class UserServiceClientFallbackImpl implements UserServiceClient {

	@Autowired
	private SysProperties sysProperties;

	private Throwable throwable;

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param identifier identifier
	 * @param tenantCode 租户标识
	 * @param tenantCode 租户标识
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<UserVo> findUserByIdentifier(String identifier, String tenantCode) {
		log.error("Feign findUserByIdentifier circuit breaker: {}, {}, {}", tenantCode, identifier, throwable);
		return null;
	}

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param identifier   identifier
	 * @param identityType identityType
	 * @param tenantCode   租户标识
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<UserVo> findUserByIdentifier(String identifier, Integer identityType, String tenantCode) {
		log.error("Feign findUserByIdentifier circuit breaker: {}, {}, {}, {}", tenantCode, identityType, identifier,
				throwable);
		return null;
	}

	/**
	 * 查询当前登录的用户信息
	 *
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<UserInfoDto> info() {
		log.error("Feign get user info circuit breaker", throwable);
		return null;
	}

	/**
	 * 根据用户ID批量查询用户信息
	 *
	 * @param ids ids
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<List<UserVo>> findUserById(@RequestBody Long[] ids) {
		log.error("Call findUserById circuit breaker: {}", ids, throwable);
		return null;
	}

	/**
	 * 查询用户数量
	 *
	 * @param userVo userVo
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<Integer> findUserCount(UserVo userVo) {
		log.error("Call findUserCount circuit breaker: {}", userVo, throwable);
		return new ResponseBean<>(0);
	}

	/**
	 * 根据部门ID批量查询部门信息
	 *
	 * @param ids ids
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<List<DeptVo>> findDeptById(@RequestBody Long[] ids) {
		log.error("Call findDeptById circuit breaker: {}", ids, throwable);
		return null;
	}

	/**
	 * 根据附件ID删除附件
	 *
	 * @param id id
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<Boolean> deleteAttachment(Long id) {
		log.error("Call deleteAttachment circuit breaker: {}", id, throwable);
		return new ResponseBean<>(Boolean.FALSE);
	}

	/**
	 * 根据附件ID批量查询附件信息
	 *
	 * @param ids ids
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<List<AttachmentVo>> findAttachmentById(Long[] ids) {
		log.error("Call findAttachmentById circuit breaker: {}", ids, throwable);
		return new ResponseBean<>(new ArrayList<>());
	}

	/**
	 * 保存日志
	 *
	 * @param logInfo logInfo
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<Boolean> saveLog(Log logInfo) {
		log.error("Feign save log circuit breaker", throwable);
		return null;
	}

	/**
	 * 根据角色查找菜单
	 *
	 * @param tenantCode 租户标识
	 * @param role       角色
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<List<Menu>> findMenuByRole(String role, String tenantCode) {
		log.error("Feign findMenuByRole circuit breaker, {}, {}", tenantCode, throwable);
		return new ResponseBean<>(new ArrayList<>());
	}

	/**
	 * 查询所有菜单
	 *
	 * @param tenantCode 租户标识
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<List<Menu>> findAllMenu(String tenantCode) {
		log.error("Feign findAllMenu circuit breaker, {}, {}", tenantCode, throwable);
		return new ResponseBean<>(new ArrayList<>());
	}

	/**
	 * 根据租户标识查询租户详细信息
	 *
	 * @param tenantCode 租户标识
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<Tenant> findTenantByTenantCode(String tenantCode) {
		log.error("Feign findTenantByTenantCode circuit breaker, {}, {}", tenantCode, throwable);
		return null;
	}

	/**
	 * 根据社交账号获取用户详细信息
	 *
	 * @param social     social
	 * @param tenantCode 租户标识
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<UserVo> findUserBySocial(String social, String tenantCode) {
		log.error("Feign findUserBySocial circuit breaker, {}, {}, {}", social, tenantCode, throwable);
		return null;
	}

	/**
	 * 注册用户
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<Boolean> registerUser(UserDto userDto) {
		log.error("Feign registerUser circuit breaker, {}, {}, {}", userDto.getIdentityType(), userDto.getIdentifier(),
				throwable);
		return null;
	}

	/**
	 * 更新用户登录信息
	 *
	 * @param userDto userDto
	 * @return ResponseBean
	 */
	@Override
	public ResponseBean<Boolean> updateLoginInfo(UserDto userDto) {
		log.error("Feign updateLoginInfo circuit breaker, {}, {}, {}", userDto.getIdentityType(),
				userDto.getIdentifier(), throwable);
		return null;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
}
