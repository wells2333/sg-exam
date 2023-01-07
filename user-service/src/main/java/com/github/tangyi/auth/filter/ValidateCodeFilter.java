package com.github.tangyi.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.exceptions.InvalidValidateCodeException;
import com.github.tangyi.common.exceptions.ValidateCodeExpiredException;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.RUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

	private final RedisTemplate redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 忽略验证码
		if (Status.OPEN.equals(request.getParameter("ignoreCode"))) {
			filterChain.doFilter(request, response);
		} else {
			doCheckCode(request, response, filterChain);
		}
	}

	public void doCheckCode(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		String uri = request.getRequestURI();
		// 密码登录、手机号登录、注册才校验验证码
		if (HttpMethod.POST.matches(request.getMethod()) && StrUtil.containsAnyIgnoreCase(uri,
				SecurityConstant.REGISTER, SecurityConstant.MOBILE_LOGIN_URL)) {
			try {
				checkCode(request);
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				RUtil.out(response, R.error(e.getMessage()));
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	/**
	 * 校验验证码
	 *
	 * @param request request
	 */
	@SuppressWarnings({"unchecked"})
	private void checkCode(HttpServletRequest request) throws InvalidValidateCodeException {
		// 验证码
		String code = request.getParameter("code");
		if (StrUtil.isBlank(code)) {
			throw new InvalidValidateCodeException("请输入验证码");
		}
		// 获取随机码
		String randomStr = request.getParameter("randomStr");
		// 随机数为空，则获取手机号
		if (StrUtil.isBlank(randomStr)) {
			randomStr = request.getParameter("mobile");
		}
		String key = CommonConstant.VERIFICATION_CODE_KEY + randomStr;
		// 验证码过期
		if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		}
		Object codeObj = redisTemplate.opsForValue().get(key);
		if (codeObj == null) {
			throw new ValidateCodeExpiredException(SecurityConstant.EXPIRED_ERROR);
		}
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
}
