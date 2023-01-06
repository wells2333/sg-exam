package com.github.tangyi.common.oss.utils;

import com.github.tangyi.common.oss.config.QiNiuConfig;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.utils.JsonMapper;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.common.utils.StopWatchUtil;
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
import org.apache.commons.lang3.time.StopWatch;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class QiNiuUtil {

	private static class QiNiuUtilInstance {
		public static QiNiuUtil instance = new QiNiuUtil();
	}

	public static QiNiuUtil getInstance() {
		return QiNiuUtilInstance.instance;
	}

	public QiNiuUtil() {
	}

	/**
	 * 获取七牛云token
	 *
	 * @return String
	 */
	public String getQiNiuToken() {
		return getAuth().uploadToken(getQiNiuConfig().getBucket());
	}

	/**
	 * 上传七牛云
	 *
	 * @param uploadBytes 文件
	 * @param fileName    文件名 默认不指定key的情况下，以文件内容的hash值作为文件名
	 * @return String
	 */
	public String upload(byte[] uploadBytes, String fileName) {
		StopWatch watch = StopWatchUtil.start();
		try {
			QiNiuConfig qiNiuConfig = getQiNiuConfig();
			Response response = getUploadManager().put(uploadBytes, fileName, getQiNiuToken());
			// 解析上传成功的结果
			DefaultPutRet putRet = JsonMapper.getInstance().fromJson(response.bodyString(), DefaultPutRet.class);
			return qiNiuConfig.getDomainOfBucket() + "/" + putRet.key;
		} catch (QiniuException ex) {
			log.error("upload failed", ex);
			throw new OssException(ex, "upload failed");
		} finally {
			log.info("update finished, fileName: {}, took: {}", fileName, StopWatchUtil.stop(watch));
		}
	}

	/**
	 * 获取图片url
	 *
	 * @param fileName fileName
	 * @return String
	 */
	public String getDownloadUrl(String fileName) throws Exception {
		return getDownloadUrl(fileName, getQiNiuConfig().getExpire());
	}

	/**
	 * 获取图片url
	 *
	 * @param fileName fileName
	 * @return String
	 */
	public String getDownloadUrl(String fileName, int expire) {
		QiNiuConfig qiNiuConfig = getQiNiuConfig();
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
		String publicUrl = String.format("%s/%s", qiNiuConfig.getDomainOfBucket(), encodedFileName);
		return getAuth().privateDownloadUrl(publicUrl, expire);
	}

	/**
	 * 删除附件
	 *
	 * @param fileName fileName
	 * @return boolean
	 */
	public boolean delete(String fileName) throws QiniuException {
		log.info("start to delete attachment, fileName={}", fileName);
		getBucketManager(getAuth(), new Configuration(Region.region2())).delete(getQiNiuConfig().getBucket(), fileName);
		log.info("delete attachment success, fileName={}", fileName);
		return Boolean.TRUE;
	}

	public QiNiuConfig getQiNiuConfig() {
		return SpringContextHolder.getApplicationContext().getBean(QiNiuConfig.class);
	}

	public Auth getAuth() {
		QiNiuConfig qiNiuConfig = getQiNiuConfig();
		if (StringUtils.isNotBlank(qiNiuConfig.getAccessKey()) && StringUtils.isNotBlank(qiNiuConfig.getSecretKey())) {
			return Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
		}
		return null;
	}

	public UploadManager getUploadManager() {
		Configuration config = new Configuration(Region.autoRegion());
		return new UploadManager(config);
	}

	public BucketManager getBucketManager(Auth auth, Configuration config) {
		return new BucketManager(auth, config);
	}
}
