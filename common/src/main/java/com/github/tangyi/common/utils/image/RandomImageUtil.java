package com.github.tangyi.common.utils.image;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author tangyi
 * @date 2022/5/1 10:33 上午
 */
public class RandomImageUtil {

	/**
	 * get random image name
	 * @return String
	 */
	public static String randomImage(int count, String type) {
		return (ThreadLocalRandom.current().nextInt(count) + 1) + type;
	}
}
