package com.xianzhi.baidumap.tools;

import android.os.Environment;

import java.io.File;

/**
 * @author LiMing
 * @Demo class FileUtils
 * @Description TODO
 * @date 2019-10-14 13:24
 */
public class FileUtils {

    /**
     *
     * @param appFolderName
     * @return
     */
    public static String initDirs(String appFolderName) {
        String path = getSdcardDir();
        if (path == null) {
            return "";
        }
        File f = new File(path, appFolderName);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return f.getAbsolutePath();
    }

    /**
     * 根路径
     * @return
     */
    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
}
