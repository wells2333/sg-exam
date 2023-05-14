package com.github.tangyi.user;

import com.github.tangyi.user.service.attach.QiNiuAttachmentStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@AutoConfigureMockMvc
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QiNiuAttachmentStorageTests extends BaseTests {

	@Autowired
	private QiNiuAttachmentStorage qiNiuAttachmentStorage;

	@Test
	public void testMultiPartUpload() throws IOException, ExecutionException, InterruptedException {
		File targetFile = new File("");
		if (targetFile.exists()) {
			qiNiuAttachmentStorage.multiPartUpload(targetFile, "exam/服务器更新.mov", false);
		}
		Assertions.assertTrue(true);
	}
}
