package com.github.tangyi.auth.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.AbstractOAuth2SecurityExceptionHandler;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * accessDeniedHandler，return ResponseBean
 *
 * @author tangyi
 * @date 2019-11-11 21:24
 */
@Slf4j
public class CustomOAuth2AccessDeniedHandler extends AbstractOAuth2SecurityExceptionHandler
		implements AccessDeniedHandler {

	private WebResponseExceptionTranslator<?> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

	private CustomOAuth2ExceptionRenderer exceptionRenderer = new CustomOAuth2ExceptionRenderer();

	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException)
			throws IOException {
		try {
			ResponseEntity<?> result = exceptionTranslator.translate(authException);
			result = enhanceResponse(result, authException);
			exceptionRenderer.handleResponseBeanResponse(result, new ServletWebRequest(request, response));
			response.flushBuffer();
		} catch (ServletException e) {
			log.error(e.getMessage(), e);
		} catch (IOException | RuntimeException e) {
			throw e;
		} catch (Exception e) {
			// Wrap other Exceptions. These are not expected to happen
			throw new RuntimeException(e);
		}
	}
}
