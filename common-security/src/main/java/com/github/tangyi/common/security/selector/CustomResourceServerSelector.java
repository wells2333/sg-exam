package com.github.tangyi.common.security.selector;

import com.github.tangyi.common.security.config.CustomResourceServerConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 资源服务器配置
 *
 * @author tangyi
 * @date 2019-03-15 13:20
 */
public class CustomResourceServerSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{CustomResourceServerConfig.class.getName()};
	}
}
