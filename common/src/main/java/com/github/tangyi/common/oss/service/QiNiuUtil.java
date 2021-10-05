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
 *
 * @author tangyi
 * @date 2019/12/8 20:25
 */
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
        try {
            QiNiuConfig qiNiuConfig = getQiNiuConfig();
            Response response = getUploadManager().put(uploadBytes, fileName, getQiNiuToken());
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
        return getDownloadUrl(fileName, getQiNiuConfig().getExpire());
    }

    /**
     * 获取图片url
     *
     * @param fileName fileName
     * @return String
     */
    public String getDownloadUrl(String fileName, int expire) throws UnsupportedEncodingException {
        QiNiuConfig qiNiuConfig = getQiNiuConfig();
        String encodedFileName = URLEncoder.encode(fileName, CommonConstant.UTF8).replace("+", "%20");
        String publicUrl = String.format("%s/%s", qiNiuConfig.getDomainOfBucket(), encodedFileName);
        Auth auth = getAuth();
        String url = "";
        if (expire == -1) {
            url = auth.privateDownloadUrl(publicUrl);
        } else {
            url = auth.privateDownloadUrl(publicUrl, expire);
        }
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }

    /**
     * 删除附件
     *
     * @param fileName fileName
     * @return boolean
     */
    public boolean delete(String fileName) {
        try {
            getBucketManager(getAuth(), new Configuration(Region.region2())).delete(getQiNiuConfig().getBucket(), fileName);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("delete attachment exception:{}", e.getMessage(), e);
            throw new OssException(e, "delete attachment exception");
        }
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
        Configuration config = new Configuration(Region.region2());
        return new UploadManager(config);
    }

    public BucketManager getBucketManager(Auth auth, Configuration config) {
        return new BucketManager(auth, config);
    }
}
