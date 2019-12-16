package com.github.tangyi.user.service;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.oss.service.QiNiuService;
import com.github.tangyi.user.api.module.Attachment;
import com.github.tangyi.user.mapper.AttachmentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * @author tangyi
 * @date 2018/10/30 20:55
 */
@Slf4j
@AllArgsConstructor
@Service
public class AttachmentService extends CrudService<AttachmentMapper, Attachment> {

	private final QiNiuService qiNiuService;

	/**
	 * 根据id查询
	 *
	 * @param attachment attachment
	 * @return Attachment
	 */
	@Cacheable(value = "attachment#" + CommonConstant.CACHE_EXPIRE, key = "#attachment.id")
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
	@CacheEvict(value = {"attachment", "attachment_preview"}, key = "#attachment.id")
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
		try {
			long start = System.currentTimeMillis();
			long attachSize = file.getSize();
			if (StringUtils.isNotBlank(file.getOriginalFilename())) {
				String fileName = new String(file.getOriginalFilename().getBytes(), StandardCharsets.UTF_8);
				String previewUrl = qiNiuService.upload(file.getBytes(), fileName);
				Attachment newAttachment = new Attachment();
				newAttachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
				newAttachment.setPreviewUrl(previewUrl);
				newAttachment.setAttachName(fileName);
				newAttachment.setGroupName(qiNiuService.getDomainOfBucket());
				newAttachment.setFastFileId(fileName);
				newAttachment.setAttachSize(Long.toString(attachSize));
				newAttachment.setBusiId(attachment.getBusiId());
				newAttachment.setBusiModule(attachment.getBusiModule());
				newAttachment.setBusiType(attachment.getBusiType());
				super.insert(newAttachment);
				log.info("Upload attachment success, fileName: {}, time: {}ms", file.getName(), System.currentTimeMillis() - start);
				return newAttachment;
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException(e);
		}
	}

	/**
	 * 下载
	 *
	 * @param attachment attachment
	 * @return InputStream
	 */
	public String download(Attachment attachment) throws Exception {
		// 下载附件
		return qiNiuService.getDownloadUrl(attachment.getAttachName());
	}

	/**
	 * 删除
	 *
	 * @param attachment attachment
	 * @return int
	 */
	@Override
	@Transactional
	@CacheEvict(value = {"attachment", "attachment_preview"}, key = "#attachment.id")
	public int delete(Attachment attachment) {
		qiNiuService.delete(attachment.getAttachName());
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
	@CacheEvict(value = {"attachment", "attachment_preview"}, allEntries = true)
	public int deleteAll(Long[] ids) {
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
	@Cacheable(value = "attachment_preview#" + CommonConstant.CACHE_EXPIRE, key = "#attachment.id")
	public String getPreviewUrl(Attachment attachment) {
		attachment = this.get(attachment);
		if (attachment == null)
			throw new CommonException("Attachment does not exist");
		String preview = attachment.getPreviewUrl();
		if (!preview.startsWith("http"))
			preview = "http://" + preview;
		log.debug("GetPreviewUrl id: {}, preview url: {}", attachment.getId(), preview);
		return preview;
	}
}
