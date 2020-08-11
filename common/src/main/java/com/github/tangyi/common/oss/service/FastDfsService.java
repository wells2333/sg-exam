package com.github.tangyi.common.oss.service;

import com.github.tobato.fastdfs.domain.fdfs.GroupState;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadFileWriter;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * fastDfs服务
 *
 * @author tangyi
 * @date 2018-01-04 10:34
 */
@Slf4j
@AllArgsConstructor
@Service
public class FastDfsService {

    /**
     * 面向普通应用的文件操作接口
     */
    private final FastFileStorageClient fastFileStorageClient;

    /**
     * 支持断点续传的文件服务接口
     */
    private final AppendFileStorageClient appendFileStorageClient;

    /**
     * 目录服务(Tracker)客户端接口
     */
    private final TrackerClient trackerClient;

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param size        文件大小
     * @param extName     扩展名
     * @return 完整路径
     * @author tangyi
     * @date 2018/1/5 12:02
     */
    public String uploadFile(InputStream inputStream, long size, String extName) {
        return uploadFile(inputStream, size, extName, null);
    }

    /**
     * 上传文件
     *
     * @param inputStream
     * @param size        文件大小
     * @param extName     扩展名
     * @param metaDataSet 元数据集
     * @return 完整路径
     * @author tangyi
     * @date 2018/1/5 12:02
     */
    public String uploadFile(InputStream inputStream, long size, String extName, Set<MetaData> metaDataSet) {
        try {
            log.info("Attachment size: {}，extName: {}", size, extName);
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, size, extName, metaDataSet);
            log.info("Upload success, group: {}, path: {}", storePath.getGroup(), storePath.getPath());
            return storePath.getFullPath();
        } catch (Exception e) {
            log.error("Upload failed", e);
        }
        return null;
    }

    /**
     * 支持断点续传，适合上传大文件，需要指定组名
     *
     * @param groupName   组名
     * @param inputStream 附件输入流
     * @param size        附件大小
     * @param extName     附件扩展名
     * @return 完整路径
     * @author tangyi
     * @date 2018/3/8 15:51
     */
    public String uploadAppenderFile(String groupName, InputStream inputStream, long size, String extName) {
        try {
			log.info("Attachment size: {}，extName: {}", size, extName);
            StorePath storePath = appendFileStorageClient.uploadAppenderFile(groupName, inputStream, size, extName);
            log.info("上传文件成功，group：{}，path：{}", storePath.getGroup(), storePath.getPath());
            return storePath.getFullPath();
        } catch (Exception e) {
            log.error("上传文件失败！", e);
        }
        return null;
    }

    /**
     * 续传文件
     *
     * @param groupName   组名，如group1
     * @param path        路径名，M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @param inputStream 输入流
     * @param size        附件大小
     * @return 是否续传成功
     * @author tangyi
     * @date 2018/3/8 15:53
     */
    public boolean appendFile(String groupName, String path, InputStream inputStream, long size) {
        try {
            log.info("续传文件大小{}，组名{}，路径名{}", size, groupName, path);
            appendFileStorageClient.appendFile(groupName, path, inputStream, size);
            return true;
        } catch (Exception e) {
            log.error("续传文件失败！", e);
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @return 字节数组
     * @author tangyi
     * @date 2018/1/5 11:59
     */
    public byte[] downloadFile(String groupName, String path) {
        if (path.startsWith(groupName + "/"))
            path = path.split(groupName + "/")[1];
        try {
            log.info("下载文件，group：{}，path：{}", groupName, path);
            DownloadByteArray callback = new DownloadByteArray();
            return fastFileStorageClient.downloadFile(groupName, path, callback);
        } catch (Exception e) {
            log.error("下载文件失败!", e);
        }
        return null;
    }

    /**
     * 下载附件，并保存到指定的文件，适合下载大文件
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @param filePath  文件存放的路径，如：C:\attach\1.rar
     * @return 文件存放的路径
     * @author tangyi
     * @date 2018/3/9 10:10
     */
    public String downloadFile(String groupName, String path, String filePath) {
        if (path.startsWith(groupName + "/"))
            path = path.split(groupName + "/")[1];
        try {
            log.info("下载文件，group：{}，path：{}", groupName, path);
            DownloadFileWriter callback = new DownloadFileWriter(filePath);
            return fastFileStorageClient.downloadFile(groupName, path, callback);
        } catch (Exception e) {
            log.error("下载文件失败!", e);
        }
        return null;
    }

    /**
     * 下载文件，返回流
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @return 附件输入流
     * @author tangyi
     * @date 2018/1/5 12:00
     */
    public InputStream downloadStream(String groupName, String path) {
        try {
            byte[] content = downloadFile(groupName, path);
            return new ByteArrayInputStream(content);
        } catch (Exception e) {
            log.error("下载附件失败！", e);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @author tangyi
     * @date 2018/1/5 12:01
     */
    public void deleteFile(String groupName, String path) {
        if (path.startsWith(groupName + "/"))
            path = path.split(groupName + "/")[1];
        fastFileStorageClient.deleteFile(groupName, path);
        log.info("删除文件成功，group：{}，path：{}", groupName, path);
    }

    /**
     * 修改文件
     *
     * @param groupName   组名，如：group1
     * @param oldPath     旧的路径
     * @param inputStream 附件输入流
     * @param size        附件大小
     * @param extName     扩展名
     * @return 完整路径
     * @author tangyi
     * @date 2018/1/5 12:01
     */
    public String modify(String groupName, String oldPath, InputStream inputStream, long size, String extName) {
        String path = uploadFile(inputStream, size, extName);
        if (StringUtils.isBlank(path))
            return null;
        deleteFile(groupName, oldPath);
        return path;
    }

    /**
     * 获取一个组
     *
     * @return 组名
     * @author tangyi
     * @date 2018/3/9 10:43
     */
    public String getGroup() {
        StorageNode storageNode = trackerClient.getStoreStorage();
        if (storageNode != null)
            return storageNode.getGroupName();
        return null;
    }

    /**
     * 获取所有组
     *
     * @return 可用的组列表
     * @author tangyi
     * @date 2018/3/9 10:42
     */
    public List<String> listGroups() {
        List<String> groups = new ArrayList<String>();
        List<GroupState> groupStates = trackerClient.listGroups();
        if (CollectionUtils.isNotEmpty(groupStates)) {
            for (GroupState state : groupStates)
                groups.add(state.getGroupName());
        }
        return groups;
    }
}
