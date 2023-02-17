package com.github.tangyi.common.utils;

import org.apache.lucene.util.StringHelper;

public class HashUtil {

	public static int getShardId(String id, int size) {
		return Math.floorMod(hash(id), size);
	}

	public static int hash(String routing) {
		byte[] bytesToHash = new byte[routing.length() * 2];
		for (int i = 0; i < routing.length(); ++i) {
			char c = routing.charAt(i);
			byte b1 = (byte) c;
			byte b2 = (byte) (c >>> 8);
			assert (b1 & 255 | (b2 & 255) << 8) == c;
			bytesToHash[i * 2] = b1;
			bytesToHash[i * 2 + 1] = b2;
		}
		return hash(bytesToHash, 0, bytesToHash.length);
	}

	public static int hash(byte[] bytes, int offset, int length) {
		return StringHelper.murmurhash3_x86_32(bytes, offset, length, 0);
	}
}
