package com.github.tangyi.exam.service.image;

import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.RandomImageUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RandomImageService {

	private final SysProperties sysProperties;

	/**
	 * get random image url
	 * @return String
	 */
	public String randomImage() {
		return RandomImageUtil.randomImage(sysProperties.getDefaultImageCount(), sysProperties.getDefaultImageType());
	}
}
