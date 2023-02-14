package com.github.tangyi.user;

import com.github.tangyi.common.utils.AesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AesTests {

	@Test
	public void testDecryptAES() throws Exception {
		String res = AesUtil.decryptAES("lBTqrKS0kZixOFXeZ0HRng==", "1234567887654321").trim();
		Assertions.assertNotNull(res);
		Assertions.assertEquals(res, "123456");
	}
}
