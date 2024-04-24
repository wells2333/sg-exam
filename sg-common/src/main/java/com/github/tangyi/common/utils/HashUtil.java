/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.common.utils;

import org.apache.lucene.util.StringHelper;

public class HashUtil {

	private HashUtil() {
	}

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
