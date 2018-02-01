package sven.com.apkpatchserver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sven.com.apkpatchserver.utils.FileUtils;

public class MainActivity extends AppCompatActivity  {
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 &&
                        PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//Android 6.0以上版本需要获取临时权限
                    requestPermissions(perms, PERMS_REQUEST_CODE);
                } else {
                    FileUtils.createDirs();
                    copyFileFromAsset(Constants.OLD_APK_PATH,Constants.OLD_APK_NAME);//首先把assets下的apk文件复制到sdcard上
                    copyFileFromAsset(Constants.NEW_APK_PATH,Constants.NEW_APK_NAME);//首先把assets下的apk文件复制到sdcard上
                }
            }
        });

        findViewById(R.id.diff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = DiffUtils.genDiff(Constants.OLD_APK_PATH, Constants.NEW_APK_PATH, Constants.PATCH_FILE);
            }
        });

    }



    /**
     * 如果sdcard没有文件就复制过去
     */
    private void copyFileFromAsset(String filePath, String assetName) {
        AssetManager assetManager = this.getAssets();
        try {
            File file = new File(filePath);
            if (!file.exists()) {//文件不存在才复制
                InputStream in = assetManager.open(assetName);
                OutputStream out = new FileOutputStream(filePath);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case PERMS_REQUEST_CODE:
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) {
                    FileUtils.createDirs();
                    copyFileFromAsset(Constants.FILE_PATH,Constants.OLD_APK_NAME);//首先把assets下的apk文件复制到sdcard上
                    copyFileFromAsset(Constants.FILE_PATH,Constants.NEW_APK_NAME);//首先把assets下的apk文件复制到sdcard上
                }
                break;
        }
    }
}
