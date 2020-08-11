package com.github.tangyi.common.config;

import com.github.tangyi.common.properties.SnowflakeProperties;
import com.github.tangyi.common.utils.SnowflakeIdWorker;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ID生成配置
 *
 * @author tangyi
 * @date 2019/4/26 11:17
 */
@Configuration
@AllArgsConstructor
public class SnowFlake {

	private final SnowflakeProperties snowflakeProperties;

	@Bean
	public SnowflakeIdWorker initTokenWorker() {
		return new SnowflakeIdWorker(
				Integer.parseInt(snowflakeProperties.getWorkId() == null ? "1" : snowflakeProperties.getWorkId()),
				Integer.parseInt(
						snowflakeProperties.getDataCenterId() == null ? "1" : snowflakeProperties.getDataCenterId()));
	}
}
