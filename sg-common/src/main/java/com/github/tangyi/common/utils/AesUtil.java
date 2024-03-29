package com.github.tangyi.common.utils;

import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AesUtil {

	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";

	private AesUtil() {
	}

	public static String decryptAES(String data, String pass) throws Exception {
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
				new IvParameterSpec(pass.getBytes()));
		byte[] result = cipher.doFinal(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
		return new String(result, StandardCharsets.UTF_8);
	}
}

