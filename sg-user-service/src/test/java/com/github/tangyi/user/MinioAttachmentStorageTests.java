package com.github.tangyi.user;

import com.github.tangyi.api.user.attach.FileUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.user.service.attach.MinioAttachmentStorage;
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
public class MinioAttachmentStorageTests {

    @Autowired
    private MinioAttachmentStorage minioAttachmentStorage;

    @Test
    public void testUpload() {
        FileUploadContext context = new FileUploadContext();
        context.setTargetFile(new File("~/Downloads/视频/剪映/服务更新.mp4"));
        context.setGroup(AttachGroup.of(AttachTypeEnum.DEFAULT));
        context.setUser("admin");
        context.setTenantCode("gitee");
        Attachment attachment = minioAttachmentStorage.upload(context);
        Assertions.assertNotNull(attachment);
    }

    @Test
    public void testGetDownloadUrl() {
        String fileName = "2.jpeg";
        String url = minioAttachmentStorage.getDownloadUrl(fileName, -1);
        Assertions.assertNotNull(url);
        minioAttachmentStorage.doDelete(null, fileName);
    }
}
