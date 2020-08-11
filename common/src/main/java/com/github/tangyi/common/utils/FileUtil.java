package com.github.tangyi.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 文件工具类
 *
 * @author tangyi
 * @date 2018/10/30 22:05
 */
@Slf4j
public class FileUtil {

    /**
     * 获取文件后缀名
     *
     * @param fileName fileName
     * @return String
     * @author tangyi
     * @date 2018/10/30 22:05
     */
    public static String getFileNameEx(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length()))) {
                return fileName.substring(dot + 1, fileName.length());
            }
        }
        return "";
    }

    /**
     * 创建目录
     *
     * @param descDirName descDirName
     * @return String
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            log.debug("dir " + descDirNames + " already exits!");
            return false;
        }
        if (descDir.mkdirs()) {
            log.debug("dir " + descDirNames + " create success!");
            return true;
        } else {
            log.debug("dir " + descDirNames + " create failure!");
            return false;
        }
    }

    /**
     *
     * 删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.debug(dirNames + " dir not exists!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        if (files != null) {
            for (File file : files) {
                // 删除子文件
                if (file.isFile()) {
                    flag = FileUtil.deleteFile(file.getAbsolutePath());
                    // 如果删除文件失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
                // 删除子目录
                else if (file.isDirectory()) {
                    flag = FileUtil.deleteDirectory(file
                            .getAbsolutePath());
                    // 如果删除子目录失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
            }
        }

        if (!flag) {
            log.debug("delete dir failure!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            log.debug("delete dir " + dirName + " success!");
            return true;
        } else {
            log.debug("delete dir " + dirName + " failure!");
            return false;
        }
    }

    /**
     *
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("delete file " + fileName + " success!");
                return true;
            } else {
                log.debug("delete file " + fileName + " failure!");
                return false;
            }
        } else {
            log.debug(fileName + " not exists!");
            return true;
        }
    }
}
