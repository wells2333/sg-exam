package com.github.tangyi.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.exceptions.InvalidValidateCodeException;
import com.github.tangyi.common.exceptions.ValidateCodeExpiredException;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 *
 * @author tangyi
 * @date 2019/3/18 16:40
 */
@AllArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidateCodeFilter implements Filter {

	private final RedisTemplate redisTemplate;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// 请求的URI
		String uri = request.getRequestURI();
		if (HttpMethod.POST.matches(request.getMethod()) && StrUtil
				.containsAnyIgnoreCase(uri, SecurityConstant.OAUTH_TOKEN_URL, SecurityConstant.REGISTER,
						SecurityConstant.MOBILE_TOKEN_URL)) {
			String grantType = request.getParameter(SecurityConstant.GRANT_TYPE);
			// 授权类型为密码模式、手机号、注册才校验验证码
			if (CommonConstant.GRANT_TYPE_PASSWORD.equals(grantType) || CommonConstant.GRANT_TYPE_MOBILE
					.equals(grantType) || StrUtil.containsAnyIgnoreCase(uri, SecurityConstant.REGISTER)) {
				try {
					// 校验验证码
					checkCode(request, getLoginType(grantType));
				} catch (Exception e) {
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * 校验验证码
	 *
	 * @param request request
	 * @param loginType         loginType
	 * @throws InvalidValidateCodeException
	 */
	@SuppressWarnings({"unchecked"})
	private void checkCode(HttpServletRequest request, LoginTypeEnum loginType) throws InvalidValidateCodeException {
		// 验证码
		String code = request.getParameter("code");
		if (StrUtil.isBlank(code))
			throw new InvalidValidateCodeException("请输入验证码");
		// 获取随机码
		String randomStr = request.getParameter("randomStr");
		// 随机数为空，则获取手机号
		if (StrUtil.isBlank(randomStr))
			randomStr = request.getParameter("mobile");
		String key = CommonConstant.DEFAULT_CODE_KEY + loginType.getType() + "@" + randomStr;
		// 验证码过期
		if (!redisTemplate.hasKey(key))
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		Object codeObj = redisTemplate.opsForValue().get(key);
		if (codeObj == null)
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		String saveCode = codeObj.toString();
		if (StrUtil.isBlank(saveCode)) {
			redisTemplate.delete(key);
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		}
		if (!StrUtil.equals(saveCode, code)) {
			redisTemplate.delete(key);
			throw new InvalidValidateCodeException("验证码错误");
		}
		redisTemplate.delete(key);
	}

	/**
	 * 获取登录类型
	 *
	 * @param grantType grantType
	 * @return LoginType
	 */
	private LoginTypeEnum getLoginType(String grantType) {
		if (CommonConstant.GRANT_TYPE_PASSWORD.equals(grantType))
			return LoginTypeEnum.PWD;
		if (CommonConstant.GRANT_TYPE_MOBILE.equals(grantType))
			return LoginTypeEnum.SMS;
		return LoginTypeEnum.PWD;
	}
}
