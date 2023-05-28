package com.github.tangyi.user;

import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.user.service.attach.QiNiuAttachmentStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;

@AutoConfigureMockMvc
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QiNiuAttachmentStorageTests extends BaseTests {

    @Autowired
    private QiNiuAttachmentStorage qiNiuAttachmentStorage;

    @Test
    public void testMultiPartUpload() throws OssException {
        File targetFile = new File("");
        if (targetFile.exists()) {
            qiNiuAttachmentStorage.uploadChunks(targetFile, "exam/服务器更新.mov", false);
        }
        Assertions.assertTrue(true);
    }
}
