package com.github.tangyi.exam.basic;

import com.github.tangyi.common.utils.zxing.QRCodeUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class ExamServiceApplicationTests {

	@Test
	public void testQrCode() throws Exception {
		String msg = new String("https://gitee.com/wells2333".getBytes(StandardCharsets.UTF_8),
				StandardCharsets.ISO_8859_1);
		QRCodeUtils.generateQRCodeImage(msg, 360, 360, "/Users/gaungyi.tan/Downloads/1.png");
	}
}
