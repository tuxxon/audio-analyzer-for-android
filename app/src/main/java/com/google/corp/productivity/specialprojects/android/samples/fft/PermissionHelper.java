package com.google.corp.productivity.specialprojects.android.samples.fft;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {
    public static final int RECORDAUDIO_DIALOG_REQUEST_CODE = 127;
    public static final int RECORDAUDIO_REQUEST_CODE = 128;


    public static boolean checkRecordAndStoragePermissions(Activity activity, int requestCode) {
        if (!checkStoragePermissions(activity,requestCode)) return false;

        return checkRecordAudioPermission(activity, requestCode);
    }
    public static boolean checkStoragePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if(!checkReadStoragePermissions(activity, requestCode)) return false;
        }
        return checkWriteStoragePermissions(activity, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkReadStoragePermissions(Activity activity, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    requestCode);

            return false;
        }
        return true;
    }


    public static boolean checkWriteStoragePermissions(Activity activity, int requestCode) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);

            return false;
        }
        return true;
    }

    public static boolean checkRecordAudioPermission(Activity activity, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO}, requestCode);

            return false;
        }
        return true;
    }
    /**
     * In order to be able to draw over other apps, the permission android.permission.SYSTEM_ALERT_WINDOW have to be granted.
     * <p>
     * On < API 23 (MarshMallow) the permission was granted when the user installed the application (via AndroidManifest),
     * on > 23, however, it have to start a activity asking the user if he agrees.
     * <p>
     * This method just return if the app has permission to draw over other apps, and if it doesn't, it will try to get the permission.
     *
     * @return returns {@link Settings#canDrawOverlays(Context)}
     **/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkSystemAlertWindowPermission(Context context) {
        if (!Settings.canDrawOverlays(context)) {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return false;
        }else return true;
    }

}
