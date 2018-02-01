package sven.com.practise31_ndk_bsdiff;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import sven.com.apkpatchserver.PatchUtils;
import sven.com.practise31_ndk_bsdiff.Utils.ApkUtils;
import sven.com.practise31_ndk_bsdiff.Utils.Constants;

public class MainActivity extends Activity {
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 &&
                PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//Android 6.0以上版本需要获取临时权限
            requestPermissions(perms, PERMS_REQUEST_CODE);
        }


        findViewById(R.id.patch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ApkUpdateTask().execute();
            }
        });
    }

    class ApkUpdateTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //1.下载差分包
                String oldfile = ApkUtils.getSourceApkPath(MainActivity.this, getPackageName());
                //2.合并得到最新版本的APK文件
                String newfile = Constants.OLD_2_NEW_APK;
                String patchfile = Constants.PATCH_FILE;
                PatchUtils.patch(oldfile, newfile, patchfile);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            //3.安装
            if (result) {
                Toast.makeText(MainActivity.this, "您正在进行无流量更新", Toast.LENGTH_SHORT).show();
                ApkUtils.installApk(MainActivity.this, Constants.OLD_2_NEW_APK);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case PERMS_REQUEST_CODE:




                break;
        }
    }
}
