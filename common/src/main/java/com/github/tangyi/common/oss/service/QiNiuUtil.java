package com.github.tangyi.common.oss.service;

import com.github.tangyi.common.oss.config.QiNiuConfig;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.utils.JsonMapper;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 七牛云
 * @author tangyi
 * @date 2019/12/8 20:25
 */
@Slf4j
public class QiNiuUtil {

	private Auth auth;

	private UploadManager uploadManager;

	private BucketManager bucketManager;

	private QiNiuConfig qiNiuConfig;

	private static QiNiuUtil instance;

	public synchronized static QiNiuUtil getInstance() {
		if (instance == null) {
			instance = new QiNiuUtil();
		}
		return instance;
	}

	public QiNiuUtil() {
		qiNiuConfig = SpringContextHolder.getApplicationContext().getBean(QiNiuConfig.class);
		if (StringUtils.isNotBlank(qiNiuConfig.getAccessKey()) && StringUtils.isNotBlank(qiNiuConfig.getSecretKey())) {
			instance = new QiNiuUtil();
			instance.auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
			Configuration config = new Configuration(Region.region2());
			instance.uploadManager = new UploadManager(config);
			instance.bucketManager = new BucketManager(instance.auth, config);
		}
	}

	/**
	 * 获取七牛云token
	 *
	 * @return String
	 */
	public String getQiNiuToken() {
		return auth.uploadToken(getInstance().qiNiuConfig.getBucket());
	}

	/**
	 * 上传七牛云
	 *
	 * @param uploadBytes 文件
	 * @param fileName         文件名 默认不指定key的情况下，以文件内容的hash值作为文件名
	 * @return String
	 */
	public String upload(byte[] uploadBytes, String fileName) {
		try {
			Response response = uploadManager.put(uploadBytes, fileName, getQiNiuToken());
			//解析上传成功的结果
			DefaultPutRet putRet = JsonMapper.getInstance().fromJson(response.bodyString(), DefaultPutRet.class);
			return qiNiuConfig.getDomainOfBucket() + "/" + putRet.key;
		} catch (QiniuException ex) {
			log.error("upload to qiniu exception: {}", ex.getMessage(), ex);
			throw new OssException(ex, "upload to qiniu exception");
		}
	}

	/**
	 * 获取图片url
	 *
	 * @param fileName fileName
	 * @return String
	 */
	public String getDownloadUrl(String fileName) throws UnsupportedEncodingException {
		String encodedFileName = URLEncoder.encode(fileName, CommonConstant.UTF8).replace("+", "%20");
		String publicUrl = String.format("%s/%s", qiNiuConfig.getDomainOfBucket(), encodedFileName);
		return auth.privateDownloadUrl(publicUrl, qiNiuConfig.getExpire());
	}

	/**
	 * 删除附件
	 * @param fileName fileName
	 * @return boolean
	 */
	public boolean delete(String fileName) {
		try {
			bucketManager.delete(qiNiuConfig.getBucket(), fileName);
			return Boolean.TRUE;
		} catch (Exception e) {
			log.error("delete attachment exception:{}", e.getMessage(), e);
			throw new OssException(e, "delete attachment exception");
		}
	}

	/**
	 * 获取域名
	 * @return String
	 */
	public String getDomainOfBucket() {
		return qiNiuConfig.getDomainOfBucket();
	}
}
