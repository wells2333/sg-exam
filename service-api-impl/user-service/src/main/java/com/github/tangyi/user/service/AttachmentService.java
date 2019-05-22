package com.github.tangyi.user.service;

import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.FileUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.user.api.module.Attachment;
import com.github.tangyi.user.mapper.AttachmentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author tangyi
 * @date 2018/10/30 20:55
 */
@Slf4j
@AllArgsConstructor
@Service
public class AttachmentService extends CrudService<AttachmentMapper, Attachment> {

    private final FastDfsService fastDfsService;

    /**
     * 上传
     *
     * @param file       file
     * @param attachment attachment
     * @return int
     */
    @Transactional
    public Attachment upload(MultipartFile file, Attachment attachment) {
        InputStream inputStream = null;
        try {
            long start = System.currentTimeMillis();
            inputStream = file.getInputStream();
            long attachSize = file.getSize();
            String fastFileId = fastDfsService.uploadFile(inputStream, attachSize, FileUtil.getFileNameEx(file.getOriginalFilename()));
            log.debug("fastFileId:{}", fastFileId);
            if (StringUtils.isBlank(fastFileId))
                throw new CommonException("上传失败！");
            Attachment newAttachment = new Attachment();
            newAttachment.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
            newAttachment.setGroupName(fastFileId.substring(0, fastFileId.indexOf("/")));
            newAttachment.setFastFileId(fastFileId);
            newAttachment.setAttachName(new String(file.getOriginalFilename().getBytes(), StandardCharsets.UTF_8));
            newAttachment.setAttachSize(Long.toString(attachSize));
            newAttachment.setBusiId(attachment.getBusiId());
            newAttachment.setBusiModule(attachment.getBusiModule());
            newAttachment.setBusiType(attachment.getBusiType());
            super.insert(newAttachment);
            log.info("上传文件{}成功，耗时：{}ms", file.getName(), System.currentTimeMillis() - start);
            return newAttachment;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    /**
     * 下载
     *
     * @param attachment attachment
     * @return InputStream
     */
    public InputStream download(Attachment attachment) {
        attachment = super.get(attachment);
        if (attachment == null)
            throw new CommonException("附件不存在！");
        // 下载附件
        return fastDfsService.downloadStream(attachment.getGroupName(), attachment.getFastFileId());
    }

    /**
     * 删除
     *
     * @param attachment attachment
     * @return int
     */
    @Override
    @Transactional
    public int delete(Attachment attachment) {
        try {
            fastDfsService.deleteFile(attachment.getGroupName(), attachment.getFastFileId());
            return super.delete(attachment);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return -1;
    }
}
