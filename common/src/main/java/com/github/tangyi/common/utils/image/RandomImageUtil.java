package com.github.tangyi.common.utils.image;

import java.util.concurrent.ThreadLocalRandom;

public class RandomImageUtil {

	/**
	 * get random image name
	 * @return String
	 */
	public static String randomImage(int count, String type) {
		return (ThreadLocalRandom.current().nextInt(count) + 1) + type;
	}
}
