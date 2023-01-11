package com.github.tangyi.common.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomImageUtil {

	public static String randomImage(int count, String type) {
		return (ThreadLocalRandom.current().nextInt(count) + 1) + type;
	}
}
