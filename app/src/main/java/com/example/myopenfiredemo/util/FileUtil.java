package com.example.myopenfiredemo.util;

import java.io.File;

/**文件操作
 * Created by clement on 2017/4/25.
 */

public class FileUtil {
    //用于接收文件的文件夹名称
    public final static String RECEIVER_FOLDER_NAME = "/sdcard/openFireFile";

    /**获取保存接收文件的文件夹路径，如果文件夹不存在，则创建 （Android6.0以上需要添加权限）
     * @return
     */
    public static String getReceiverFolderPath(){
        File rootFile = new File(RECEIVER_FOLDER_NAME);
        //创建该文件夹
        if(!rootFile.exists()){
            rootFile.mkdirs();
        }
        return RECEIVER_FOLDER_NAME;
    }
}
