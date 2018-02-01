package sven.com.apkpatchserver;

import sven.com.apkpatchserver.utils.FileUtils;

/**
 * Created by wanghao on 2018/1/29.
 */

public class Constants {


    public static final String FILE_PATH = FileUtils.getExternalStoragePath();
    public static final String OLD_APK_NAME = "app-old.apk";
    public static final String OLD_APK_PATH = FILE_PATH + OLD_APK_NAME;


    public static final String NEW_APK_NAME = "app-new.apk";
    public static final String NEW_APK_PATH = FILE_PATH + NEW_APK_NAME;



    // patch包存储路径
    public static final String PATCH_FILE = FILE_PATH+ "diff.patch";


    public static final String OLD_2_NEW_APK =FILE_PATH + "Old2New.apk";

}
