package com.github.tangyi.user.uploader;

import com.github.tangyi.api.user.module.Attachment;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.user.service.AttachmentService;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author tangyi
 * @date 2020/04/05 13:37
 */
public abstract class AbstractUploader implements IUploader {

    @Override
    public int save(Attachment attachment) {
        return SpringContextHolder.getApplicationContext().getBean(AttachmentService.class).insert(attachment);
    }

    @Override
    public boolean delete(Attachment attachment) {
        return SpringContextHolder.getApplicationContext().getBean(AttachmentService.class).delete(attachment) > 0;
    }

    @Override
    public abstract Attachment upload(Attachment attachment, byte[] bytes);

    @Override
    public abstract InputStream download(Attachment attachment);

    @Override
    public String getDownloadUrl(Attachment attachment) {
        return null;
    }

    @Override
    public String getDownloadUrl(Attachment attachment, int expire) {
        return null;
    }

    /**
     * 获取附件存储目录
     *
     * @param attachment attachment
     * @param id         id
     * @return String
     */
    public String getFileRealDirectory(Attachment attachment, String id) {
        String applicationCode = attachment.getApplicationCode();
        String busiId = attachment.getBusiId();
        String fileName = attachment.getAttachName();
        String fileRealDirectory = SpringContextHolder.getApplicationContext().getBean(SysProperties.class).getAttachPath() + File.separator
                + applicationCode + File.separator;
        // 有分类就加上
        if (StringUtils.isNotBlank(attachment.getBusiModule())) {
            String busiModule = attachment.getBusiModule();
            fileRealDirectory = fileRealDirectory + busiModule + File.separator;
        }
        if (StringUtils.isNotBlank(attachment.getBusiType())) {
            String busiType = attachment.getBusiType();
            fileRealDirectory = fileRealDirectory + busiType + File.separator;
        }
        fileRealDirectory = fileRealDirectory + busiId;
        return fileRealDirectory;
    }
}
