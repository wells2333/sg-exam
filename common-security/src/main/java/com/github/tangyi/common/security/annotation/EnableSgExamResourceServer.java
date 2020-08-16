package com.github.tangyi.common.security.annotation;

import com.github.tangyi.common.security.selector.CustomResourceServerSelector;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.lang.annotation.*;

/**
 * 资源服务器配置
 *
 * @author tangyi
 * @date 2019-03-15 11:37
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableWebSecurity
@Import(CustomResourceServerSelector.class)
public @interface EnableSgExamResourceServer {
}
