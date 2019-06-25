package com.github.tangyi.user.service;

import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.FileUtil;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.user.api.module.Attachment;
import com.github.tangyi.user.config.SysConfig;
import com.github.tangyi.user.mapper.AttachmentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    private final SysConfig sysConfig;

    /**
     * 根据id查询
     *
     * @param attachment attachment
     * @return Attachment
     */
    @Cacheable(value = "attachment", key = "#attachment.id")
    @Override
    public Attachment get(Attachment attachment) {
        return super.get(attachment);
    }

    /**
     * 根据id更新
     *
     * @param attachment attachment
     * @return int
     */
    @Override
    @Transactional
    @CacheEvict(value = "attachment", key = "#attachment.id")
    public int update(Attachment attachment) {
        return super.update(attachment);
    }

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
            if (StringUtils.isBlank(fastFileId))
                throw new CommonException("上传失败！");
            Attachment newAttachment = new Attachment();
            newAttachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
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
    @CacheEvict(value = "attachment", key = "#attachment.id")
    public int delete(Attachment attachment) {
        fastDfsService.deleteFile(attachment.getGroupName(), attachment.getFastFileId());
        return super.delete(attachment);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     */
    @Override
    @Transactional
    @CacheEvict(value = "attachment", allEntries = true)
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }

    /**
     * 获取附件的预览地址
     *
     * @param attachment attachment
     * @return String
     * @author tangyi
     * @date 2019/06/21 17:45
     */
    public String getPreviewUrl(Attachment attachment) {
        attachment = this.get(attachment);
        if (attachment == null)
            throw new CommonException("附件不存在.");
        String preview = attachment.getPreviewUrl();
        if (StringUtils.isBlank(preview))
            preview = sysConfig.getFdfsHttpHost() + "/" + attachment.getFastFileId();
        log.debug("id为：{}的附件的预览地址：{}", attachment.getId(), preview);
        return preview;
    }
}
