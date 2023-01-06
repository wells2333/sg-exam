package com.github.tangyi.generator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gen")
@ConditionalOnExpression("!'${gen}'.isEmpty()")
public class GenConfig {

	/** 作者 */
	public String author;

	/** 生成包路径 */
	public String packageName;

	/** 生成文件路径 */
	public String genDir;

	/** 自动去除表前缀，默认是false */
	public boolean autoRemovePre;

	/** 表前缀(类名不会包含表前缀) */
	public String tablePrefix;
}
