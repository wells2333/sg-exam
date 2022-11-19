package com.github.tangyi.common.config;

import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.SnowflakeIdWorker;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tangyi
 * @date 2022/11/19 10:26 上午
 */
@Configuration
@AllArgsConstructor
public class SnowflakeConfig {

	public static final int SNOW_FLAKE_WORK_ID = EnvUtils.getInt("SNOW_FLAKE_WORK_ID", 1);

	public static final int SNOW_FLAKE_DATA_CENTER_ID = EnvUtils.getInt("SNOW_FLAKE_DATA_CENTER_ID", 1);

	@Bean
	public SnowflakeIdWorker initTokenWorker() {
		return new SnowflakeIdWorker(SNOW_FLAKE_WORK_ID, SNOW_FLAKE_DATA_CENTER_ID);
	}
}
