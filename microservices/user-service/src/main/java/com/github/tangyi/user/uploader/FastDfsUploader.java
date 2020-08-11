package com.github.tangyi.user.uploader;

import com.github.tangyi.api.user.module.Attachment;
import com.github.tangyi.common.oss.service.FastDfsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 上传到FastDfs
 * @author tangyi
 * @date 2020/04/05 13:36
 */
@Slf4j
@Service
public class FastDfsUploader extends AbstractUploader {

    @Autowired
    private FastDfsService fastDfsService;

    @Override
    public Attachment upload(Attachment attachment, byte[] bytes) {
        try {
            attachment.setAttachSize(String.valueOf(bytes.length));
            String fastFileId = fastDfsService.uploadFile(new ByteArrayInputStream(bytes), bytes.length, attachment.getAttachType());
            String groupName = fastFileId.substring(0, fastFileId.indexOf("/"));
            attachment.setFastFileId(fastFileId);
            attachment.setGroupName(groupName);
            return attachment;
        } catch (Exception e) {
            log.error("上传附件至网盘失败:"+ attachment.getAttachName() + e.getMessage());
            return null;
        }
    }

    @Override
    public InputStream download(Attachment attachment) {
        return fastDfsService.downloadStream(attachment.getGroupName(), attachment.getFastFileId());
    }

    @Override
    public boolean delete(Attachment attachment) {
        if (StringUtils.isNotEmpty(attachment.getGroupName()) && StringUtils.isNotEmpty(attachment.getFastFileId())) {
            fastDfsService.deleteFile(attachment.getGroupName(), attachment.getFastFileId());
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean deleteAll(Attachment attachment) {
        return false;
    }
}
