package com.github.tangyi.common.core.utils;

/**
 * 文件工具类
 *
 * @author tangyi
 * @date 2018/10/30 22:05
 */
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
}
